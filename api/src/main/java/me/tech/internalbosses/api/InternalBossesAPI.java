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

    /**
     * Summon a {@link InternalBoss} based off its Id.
     * @param bossId Boss Id.
     * @param location {@link Location} as to where to spawn it.
     * @throws BossSpawnFailedException If the boss failed to spawn.
     */
    public void summonBoss(String bossId, Location location) throws BossSpawnFailedException {
        bossManager.spawnBoss(bossId, location);
    }

    /**
     * Add a {@link InternalBoss} to storage.
     * @param id Boss ID.
     * @param boss {@link InternalBoss}
     */
    public void addBoss(String id, InternalBoss boss) {
        bossManager.addBoss(id, boss);
    }

    /**
     * @param id {@link InternalBossAbility} ID.
     * @return {@link Optional<InternalBossAbility>}
     */
    public Optional<InternalBossAbility> getAbilityById(String id) {
        return abilityManager.getById(id);
    }
}
