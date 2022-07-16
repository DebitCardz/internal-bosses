package me.tech.internalbosses.plugin.listeners;

import io.papermc.paper.event.entity.EntityMoveEvent;
import lombok.RequiredArgsConstructor;
import me.tech.internalbosses.api.boss.SpawnedInternalBoss;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbility;
import me.tech.internalbosses.api.boss.abilities.InternalBossAttackedAbility;
import me.tech.internalbosses.api.boss.abilities.InternalBossMovementAbility;
import me.tech.internalbosses.api.events.InternalBossDamagedByPlayerEvent;
import me.tech.internalbosses.api.events.InternalBossKilledEvent;
import me.tech.internalbosses.api.events.InternalBossSummonedEvent;
import me.tech.internalbosses.plugin.boss.BossManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.PluginManager;

import java.util.Optional;

@RequiredArgsConstructor
public class InternalBossListeners implements Listener {
    private final BossManagerImpl bossManager;

    private final PluginManager pm;

    @EventHandler
    public void onEntityMove(EntityMoveEvent ev) {
        Optional<SpawnedInternalBoss> possibleSpawnedBoss = bossManager.getSpawnedBossById(ev.getEntity().getUniqueId());
        if(possibleSpawnedBoss.isEmpty()) {
            return;
        }
        var spawnedBoss = possibleSpawnedBoss.get();

        if(
                spawnedBoss.getBoss().getAbilities().size() == 0
                || !ev.hasChangedPosition()
        ) {
            return;
        }

        for(InternalBossAbility ability : spawnedBoss.getBoss().getAbilities()) {
            if(ability instanceof InternalBossMovementAbility movementAbility) {
                movementAbility.onBossMove(spawnedBoss, ev);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent ev) {
        Optional<SpawnedInternalBoss> possibleSpawnedBoss = bossManager.getSpawnedBossById(ev.getEntity().getUniqueId());
        if(possibleSpawnedBoss.isEmpty()) {
            return;
        }
        var spawnedBoss = possibleSpawnedBoss.get();

        pm.callEvent(new InternalBossKilledEvent(
                spawnedBoss.getBoss(),
                spawnedBoss,
                ev.getEntity().getKiller()
        ));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent ev) {
       Optional<SpawnedInternalBoss> possibleSpawnedBoss = bossManager.getSpawnedBossById(ev.getEntity().getUniqueId());
        if(
                possibleSpawnedBoss.isEmpty()
                || !(ev.getDamager() instanceof Player)
        ) {
            return;
        }
        var spawnedBoss = possibleSpawnedBoss.get();

        pm.callEvent(new InternalBossDamagedByPlayerEvent(
                spawnedBoss.getBoss(),
                spawnedBoss,
                (Player) ev.getDamager(),
                ev
        ));

        for(InternalBossAbility ability : spawnedBoss.getBoss().getAbilities()) {
            if(ability instanceof InternalBossAttackedAbility movementAbility) {
                movementAbility.onBossDamaged((Player) ev.getDamager(), spawnedBoss, ev);
            }
        }
    }

    @EventHandler
    public void onBossSummon(InternalBossSummonedEvent ev) {
    }
}
