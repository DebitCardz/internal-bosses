# Internal Bosses
Internal Bosses is a quick boss library I wrote for fun, it has some pretty basic features to make simple bosses.

This project is bundled with its own API that you can hook into to create your own custom bosses, examples can be seen in `example-plugin`.

# API Example
## Custom Ability
```java
/**
 * InternalBossAttackedAbility specifies this ability will only
 * activate when the boss has been attacked by a player.
 */
public class FireballAbility implements InternalBossAttackedAbility {
    @Override
    public String getId() {
        return "fireball";
    }

    // Executed whenever they are hit by a player.
    @Override
    public void onBossDamaged(Player attacker, SpawnedInternalBoss boss, EntityDamageByEntityEvent bukkitEvent) {
        // 50% chance of this failing to run.
        if(!InternalBossAbility.successfulRandom(50)) {
            return;
        }
        
        // Run whatever you need to here.
        Location loc1 = boss.getEntity().getLocation();

        Fireball fireball = (Fireball) loc1.getWorld().spawnEntity(loc1, EntityType.FIREBALL);
        fireball.setVelocity(attacker.getLocation().add(0.0, 3.0, 0.0).getDirection().multiply(2));
    }
}
```
## Registering Ability
```java
public final class ExamplePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        ServicesManager sm =  getServer().getServicesManager();

        InternalBossesAPI ibAPI = sm.load(InternalBossesAPI.class);
        // Fail to load hook plugin if the API is not found.
        if(ibAPI == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // Register it then we can apply it to any boss
        // that we want to create.
        ibAPI.addAbility(new FireballAbility());
    }
}
```
