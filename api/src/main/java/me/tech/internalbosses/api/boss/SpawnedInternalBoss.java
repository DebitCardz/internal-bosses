package me.tech.internalbosses.api.boss;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

/**
 * Represents a {@link InternalBoss} which is currently spawned in the world.
 */
public interface SpawnedInternalBoss {
    InternalBoss getBoss();

    LivingEntity getEntity();

    Location getSpawnLocation();
}
