package me.tech.internalbosses.plugin;

import lombok.RequiredArgsConstructor;
import me.tech.internalbosses.api.InternalBossesAPI;
import me.tech.internalbosses.api.exceptions.BossSpawnFailedException;
import me.tech.internalbosses.plugin.boss.BossManagerImpl;
import org.bukkit.Location;

@RequiredArgsConstructor
public class InternalBossesAPIImpl {
    private final BossManagerImpl bossManager;

    public void summonBoss(String bossId, Location location) throws BossSpawnFailedException {
        bossManager.spawnBoss(bossId, location);
    }
}
