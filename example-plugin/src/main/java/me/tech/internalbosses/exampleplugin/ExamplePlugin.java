package me.tech.internalbosses.exampleplugin;

import me.tech.internalbosses.api.InternalBossesAPI;
import me.tech.internalbosses.api.boss.InternalBoss;
import me.tech.internalbosses.api.boss.InternalBossBuilder;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbility;
import me.tech.internalbosses.api.boss.loot.InternalBossItemLoot;
import me.tech.internalbosses.api.boss.loot.InternalBossLootBag;
import me.tech.internalbosses.api.boss.loot.InternalBossLootBagBuilder;
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
                InternalBoss.builder()
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
        var bag1 = InternalBossLootBag.builder()
                .addLoot(new ItemStack(Material.DIAMOND))
                .addLoot("say <player> got a Diamond for killing the boss!")
                .chance(35.0)
                .build();

        var defaultBag = InternalBossLootBag.builder()
                .addLoot(new ItemStack(Material.EMERALD))
                .defaultDrop(true)
                .build();

        return Set.of(bag1, defaultBag);
    }
}