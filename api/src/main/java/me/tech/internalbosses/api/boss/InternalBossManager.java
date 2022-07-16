package me.tech.internalbosses.api.boss;

import me.tech.internalbosses.api.exceptions.BossSpawnFailedException;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.Optional;
import java.util.UUID;

public interface InternalBossManager {
    /**
     * Add a {@link InternalBoss} to storage.
     * @param id Boss ID.
     * @param boss {@link InternalBoss}
     */
    void addBoss(String id, InternalBoss boss);

    /**
     * Remove a {@link InternalBoss} based off its ID.
     * @param id Boss ID.
     */
    void removeBoss(String id);

    /**
     * Spawn a {@link InternalBoss} at a specific location.
     * @param id Boss ID.
     * @param location {@link Location}
     * @return The spawned {@link InternalBoss} {@link UUID}.
     * @throws BossSpawnFailedException If the boss failed to spawn for any reason.
     */
    UUID spawnBoss(String id, Location location) throws BossSpawnFailedException;


    Optional<InternalBoss> getBossById(String id);

    /**
     * Get a {@link SpawnedInternalBoss} based off it's {@link LivingEntity}'s {@link UUID}.
     * @param uuid Spawned Boss UUID.
     * @return {@link Optional<SpawnedInternalBoss>}
     */
    Optional<SpawnedInternalBoss> getSpawnedBossById(UUID uuid);
}
