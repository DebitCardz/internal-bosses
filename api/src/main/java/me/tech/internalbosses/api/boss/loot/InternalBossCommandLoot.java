package me.tech.internalbosses.api.boss.loot;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class InternalBossCommandLoot implements InternalBossLoot<String> {
    private final String command;

    @Override
    public String get() {
        return command;
    }

    @Override
    public void give(Player player) {
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                getParsedCommand(player)
        );
    }

    public String getParsedCommand(Player player) {
        return command.replaceAll("<player>", player.getName());
    }
}
