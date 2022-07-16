package me.tech.internalbosses.api.boss.abilities;

import me.tech.internalbosses.api.boss.InternalBoss;
import me.tech.internalbosses.api.boss.SpawnedInternalBoss;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Activated when a {@link InternalBoss} attacks a player.
 */
public interface InternalBossDamagePlayerAbility extends InternalBossAbility {
    void onBossAttack(
            Player damaged,
            SpawnedInternalBoss boss,
            EntityDamageByEntityEvent bukkitEvent
    );
}
