package me.tech.internalbosses.api.boss;

import me.tech.internalbosses.api.boss.abilities.InternalBossAbility;
import me.tech.internalbosses.api.boss.loot.InternalBossLootBag;
import me.tech.internalbosses.api.exceptions.BossFailedToLoadException;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InternalBossBuilder {
    private String name;

    private double health;

    private EntityType entityType;

    private boolean isGlowing;

    private Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();

    private Set<InternalBossLootBag> loot = new HashSet<>();

    private Set<PotionEffect> effects = new HashSet<>();

    private Set<InternalBossAbility> abilities = new HashSet<>();

    private boolean alertOnSummon;

    public InternalBossBuilder() {
        // Make sure bosses don't spawn with natural equipment.
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            this.equipment.put(slot, new ItemStack(Material.AIR));
        }
    }

    public InternalBossBuilder name(String name) {
        this.name = name;
        return this;
    }

    public InternalBossBuilder health(double health) {
        this.health = health;
        return this;
    }

    public InternalBossBuilder entityType(EntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    public InternalBossBuilder glowing(boolean isGlowing) {
        this.isGlowing = isGlowing;
        return this;
    }

    public InternalBossBuilder equipment(Map<EquipmentSlot, ItemStack> equipment) {
        this.equipment = equipment;
        return this;
    }

    public InternalBossBuilder equipmentSlot(EquipmentSlot slot, ItemStack stack) {
        this.equipment.put(slot, stack);
        return this;
    }

    public InternalBossBuilder loot(Set<InternalBossLootBag> loot) {
        this.loot = loot;
        return this;
    }

    public InternalBossBuilder effects(Set<PotionEffect> effects) {
        this.effects.addAll(effects);
        return this;
    }

    public InternalBossBuilder effect(PotionEffect effect) {
        this.effects.add(effect);
        return this;
    }

    public InternalBossBuilder abilities(Set<InternalBossAbility> abilities) {
        this.abilities.addAll(abilities);
        return this;
    }

    public InternalBossBuilder ability(InternalBossAbility ability) {
        this.abilities.add(ability);
        return this;
    }

    public InternalBossBuilder alertOnSummon(boolean alertOnSummon) {
        this.alertOnSummon = alertOnSummon;
        return this;
    }

    private void validate() {
        if(
                name == null
                || entityType == null
        ) {
            throw new BossFailedToLoadException();
        }
    }

    public InternalBoss build() {
        validate();

        return new InternalBoss(
                name,
                health,
                entityType,
                isGlowing,
                equipment,
                loot,
                effects,
                abilities,
                alertOnSummon
        );
    }

    public static InternalBossBuilder builder() {
        return new InternalBossBuilder();
    }
}