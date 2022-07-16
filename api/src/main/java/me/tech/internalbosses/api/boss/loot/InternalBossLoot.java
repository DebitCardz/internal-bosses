package me.tech.internalbosses.api.boss.loot;

import org.bukkit.entity.Player;

public interface InternalBossLoot <T> {
    T get();

    void give(Player player);
}
