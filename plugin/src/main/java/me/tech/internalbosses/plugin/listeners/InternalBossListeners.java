package me.tech.internalbosses.plugin.listeners;

import io.papermc.paper.event.entity.EntityMoveEvent;
import lombok.RequiredArgsConstructor;
import me.tech.internalbosses.api.boss.InternalBoss;
import me.tech.internalbosses.api.boss.SpawnedInternalBoss;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbility;
import me.tech.internalbosses.api.boss.abilities.InternalBossAttackedAbility;
import me.tech.internalbosses.api.boss.abilities.InternalBossMovementAbility;
import me.tech.internalbosses.api.events.InternalBossDamagedByPlayerEvent;
import me.tech.internalbosses.api.events.InternalBossKilledEvent;
import me.tech.internalbosses.api.events.InternalBossSummonedEvent;
import me.tech.internalbosses.plugin.boss.BossManagerImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
public class InternalBossListeners implements Listener {
    private final BossManagerImpl bossManager;

    @NotNull
    private final Server server;

    @NotNull
    private final PluginManager pm;

    private final MiniMessage mm;

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
        var killer = ev.getEntity().getKiller();

        // Wipe natural drops.
        ev.getDrops().clear();
        ev.setDroppedExp(0);

        pm.callEvent(new InternalBossKilledEvent(
                spawnedBoss.getBoss(),
                spawnedBoss,
                killer,
                // Give bag & return in event.
                spawnedBoss.getBoss().rewardLootBag(killer).orElse(null)
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
        InternalBoss boss = ev.getBoss();
        if(!boss.getAlertOnSummon()) {
            return;
        }

        server.broadcast(Component.empty());
        server.broadcast(mm.deserialize(
                "<white><boss_name> <yellow>has been summoned in <white><world> <yellow>at <white><x><yellow>, <white><y><yellow>, <white><z><yellow>.",
                Placeholder.unparsed("boss_name", boss.getUncoloredName()),
                Placeholder.unparsed("world", ev.getLocation().getWorld().getName()),
                Placeholder.unparsed("x", String.valueOf(cleanCoordinate(ev.getLocation().getX()))),
                Placeholder.unparsed("y", cleanCoordinate(ev.getLocation().getY())),
                Placeholder.unparsed("z", cleanCoordinate(ev.getLocation().getZ()))
        ));
        server.broadcast(Component.empty());
    }

    private String cleanCoordinate(double coordinate) {
        return String.valueOf(Math.round(coordinate));
    }
}
