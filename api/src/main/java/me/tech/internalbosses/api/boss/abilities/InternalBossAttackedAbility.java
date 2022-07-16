package me.tech.internalbosses.api.boss.abilities;

import me.tech.internalbosses.api.boss.InternalBoss;
import me.tech.internalbosses.api.boss.SpawnedInternalBoss;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Ability is activated when a {@link InternalBoss} is attacked by a player.
 */
public interface InternalBossAttackedAbility extends InternalBossAbility {
    void onBossDamaged(
            Player attacker,
            SpawnedInternalBoss boss,
            EntityDamageByEntityEvent bukkitEvent
    );
}
