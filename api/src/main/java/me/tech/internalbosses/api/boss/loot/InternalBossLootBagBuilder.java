package me.tech.internalbosses.api.boss.loot;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InternalBossLootBagBuilder {
    private List<InternalBossItemLoot> items = new ArrayList<>();

    private List<InternalBossCommandLoot> commands = new ArrayList<>();

    private double chance;

    private boolean defaultDrop = false;

    public InternalBossLootBagBuilder addLoot(ItemStack item) {
        return addLoot(new InternalBossItemLoot(item));
    }

    public InternalBossLootBagBuilder addLoot(String command) {
        return addLoot(new InternalBossCommandLoot(command));
    }

    public InternalBossLootBagBuilder addLoot(InternalBossItemLoot loot) {
        items.add(loot);
        return this;
    }

    public InternalBossLootBagBuilder addLoot(InternalBossCommandLoot loot) {
        commands.add(loot);
        return this;
    }

    public InternalBossLootBagBuilder chance(double chance) {
        this.chance = chance;
        return this;
    }

    public InternalBossLootBagBuilder defaultDrop(boolean defaultDrop) {
        this.defaultDrop = defaultDrop;
        return this;
    }

    public InternalBossLootBag build() {
        return new InternalBossLootBag(
                items,
                commands,
                chance,
                defaultDrop
        );
    }

    public static InternalBossLootBagBuilder builder() {
        return new InternalBossLootBagBuilder();
    }
}
