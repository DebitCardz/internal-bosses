package me.tech.internalbosses.api.boss;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbility;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

@Getter
@AllArgsConstructor
public class InternalBoss {
    private String name;

    private double health;

    private EntityType entityType;

    private boolean glowing;

    private Map<EquipmentSlot, ItemStack> equipment;

    private Set<InternalBossLoot> loot;

    private Set<InternalBossAbility> abilities;

    @Getter(AccessLevel.NONE)
    private boolean alertOnSummon;

    public String getUncoloredName() {
        return MiniMessage.miniMessage().stripTags(name);
    }

    public void addAbility(InternalBossAbility ability) {
        abilities.add(ability);
    }

    public void removeAbility(InternalBossAbility ability) {
        abilities.remove(ability);
    }

    public boolean getAlertOnSummon() {
        return alertOnSummon;
    }

    public static InternalBossBuilder builder() {
        return new InternalBossBuilder();
    }
}