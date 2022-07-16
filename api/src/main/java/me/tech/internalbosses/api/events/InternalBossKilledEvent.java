package me.tech.internalbosses.api.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.tech.internalbosses.api.boss.InternalBoss;
import me.tech.internalbosses.api.boss.SpawnedInternalBoss;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Issued when a {@link SpawnedInternalBoss} is slain by a {@link Player}.
 */
@RequiredArgsConstructor
@Getter
public class InternalBossKilledEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final InternalBoss boss;
    private final SpawnedInternalBoss spawnedBoss;
    private final Player killer;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
