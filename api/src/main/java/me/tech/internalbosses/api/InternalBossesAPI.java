package me.tech.internalbosses.api;

import lombok.RequiredArgsConstructor;
import me.tech.internalbosses.api.boss.InternalBoss;
import me.tech.internalbosses.api.boss.InternalBossManager;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbility;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbilityManager;
import me.tech.internalbosses.api.exceptions.BossSpawnFailedException;
import org.bukkit.Location;

import java.util.Optional;

@RequiredArgsConstructor
public final class InternalBossesAPI {
    private final InternalBossManager bossManager;

    private final InternalBossAbilityManager abilityManager;

    public void summonBoss(String bossId, Location location) throws BossSpawnFailedException {
        bossManager.spawnBoss(bossId, location);
    }

    public void addBoss(String id, InternalBoss boss) {
        bossManager.addBoss(id, boss);
    }

    public Optional<InternalBossAbility> getAbilityById(String id) {
        return abilityManager.getById(id);
    }
}
