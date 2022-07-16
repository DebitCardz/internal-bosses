package me.tech.internalbosses.api.premade.abilities;

import me.tech.internalbosses.api.boss.SpawnedInternalBoss;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbility;
import me.tech.internalbosses.api.boss.abilities.InternalBossAttackedAbility;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * 50% chance of a fireball being chucked at a player.
 */
public class FireballAbility implements InternalBossAttackedAbility {
    @Override
    public String getId() {
        return "fireball";
    }

    @Override
    public void onBossDamaged(Player attacker, SpawnedInternalBoss boss, EntityDamageByEntityEvent bukkitEvent) {
        if(!InternalBossAbility.successfulRandom(50)) {
            return;
        }

        Location loc1 = boss.getEntity().getLocation();

        Fireball fireball = (Fireball) loc1.getWorld().spawnEntity(loc1, EntityType.FIREBALL);
        fireball.setVelocity(attacker.getLocation().add(0.0, 3.0, 0.0).getDirection().multiply(2));
    }
}
