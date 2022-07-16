package me.tech.internalbosses.api.boss.abilities;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.tech.internalbosses.api.boss.InternalBoss;
import me.tech.internalbosses.api.boss.SpawnedInternalBoss;

/**
 * Activated when a {@link InternalBoss} moves position.
 * Head rotations do not count.
 */
public interface InternalBossMovementAbility extends InternalBossAbility {
    void onBossMove(
            SpawnedInternalBoss boss,
            EntityMoveEvent bukkitEvent
    );
}
