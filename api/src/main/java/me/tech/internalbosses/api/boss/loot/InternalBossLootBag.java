package me.tech.internalbosses.api.boss.loot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.tech.internalbosses.api.boss.InternalBoss;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class InternalBossLootBag {
    private final List<InternalBossItemLoot> items;

    private final List<InternalBossCommandLoot> commands;

    private final double chance;

    private final boolean defaultDrop;

    /**
     * Give the killer of an {@link InternalBoss} any {@link InternalBossLoot} if possible.
     * @param player Killer of the {@link InternalBoss}.
     */
    public void giveLoot(Player player) {
        items.forEach(loot -> loot.give(player));
        commands.forEach(loot -> loot.give(player));
    }
}
