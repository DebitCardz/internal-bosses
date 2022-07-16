package me.tech.internalbosses.exampleplugin;

import me.tech.internalbosses.api.InternalBossesAPI;
import me.tech.internalbosses.api.boss.InternalBossBuilder;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbility;
import me.tech.internalbosses.api.boss.loot.InternalBossItemLoot;
import me.tech.internalbosses.api.boss.loot.InternalBossLootBag;
import me.tech.internalbosses.api.premade.abilities.FireballAbility;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ExamplePlugin extends JavaPlugin implements Listener {
    private InternalBossesAPI ibAPI;

    @Override
    public void onEnable() {
        ServicesManager sm =  getServer().getServicesManager();

        ibAPI = sm.load(InternalBossesAPI.class);
        if(ibAPI == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        createTestBoss();
    }

    private void createTestBoss() {
        // Register a new boss under the id 'test-boss'.
        ibAPI.addBoss(
                "test-boss",
                InternalBossBuilder.builder()
                        .name("<red>Test Zombie")
                        .health(45.0)
                        .entityType(EntityType.HUSK)

                        .loot(getLootBags())
                        .ability(getFireballAbility())

                        .equipmentSlot(EquipmentSlot.HAND, new ItemStack(Material.DIAMOND_SWORD))
                        .equipmentSlot(EquipmentSlot.HEAD, new ItemStack(Material.CHAINMAIL_HELMET))

                        .glowing(false)
                        .alertOnSummon(true)
                        .build()
        );
    }

    private InternalBossAbility getFireballAbility() {
        // Register our pre-made Fireball ability.
        ibAPI.addAbility(new FireballAbility());
        var possibleFireballAbility = ibAPI.getAbilityById("fireball");
        if(possibleFireballAbility.isEmpty()) {
            throw new RuntimeException("failed to create and get fireball ability.");
        }

        return possibleFireballAbility.get();
    }

    private Set<InternalBossLootBag> getLootBags() {
        var loot = new HashSet<InternalBossLootBag>();

        // Default drop.
        loot.add(new InternalBossLootBag(
                List.of(new InternalBossItemLoot(new ItemStack(Material.DIAMOND))),
                List.of(),
                0.0,
                true
        ));

        // 25% chance to drop this,
        loot.add(new InternalBossLootBag(
           List.of(new InternalBossItemLoot(new ItemStack(Material.EMERALD))),
           List.of(),
           25.0,
           false
        ));

        return loot;
    }
}