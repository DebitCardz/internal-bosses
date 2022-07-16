package me.tech.internalbosses.plugin.hologram.decentholograms;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.RequiredArgsConstructor;
import me.tech.internalbosses.plugin.InternalBossesPlugin;
import me.tech.internalbosses.plugin.hologram.HologramManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@RequiredArgsConstructor
public class DHHologramManager implements HologramManager {
    private final InternalBossesPlugin plugin;

    private final Map<UUID, Hologram> holograms = new HashMap<>();

    @Override
    public UUID createHologram(Location location, List<String> lines) {
        UUID hologramId = UUID.randomUUID();
        Hologram dhHologram = DHAPI.createHologram(
                hologramId.toString(),
                location
        );
        lines.forEach(l -> DHAPI.addHologramLine(dhHologram, ChatColor.translateAlternateColorCodes('&', l)));

        holograms.put(
                hologramId,
                dhHologram
        );

        return hologramId;
    }

    @Override
    public void deleteHologram(UUID id) {
        var hologram = holograms.get(id);
        if(hologram == null) {
            return;
        }

        hologram.delete();
        holograms.remove(id);
    }

    @Override
    public void followEntity(UUID id, LivingEntity entity) {
        var hologram = holograms.get(id);
        if(hologram == null) {
            throw new NoSuchElementException("hologram of id " + id.toString() + " doesn't exist.");
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!entity.isDead()) {
                    DHAPI.moveHologram(
                            hologram.getId().toString(),
                            entity.getLocation().add(
                                    0.0, getHologramLocationAboveHead(entity.getEyeHeight()), 0.0
                            ));
                } else {
                    // Remove hologram after entity dies, maybe move this to an event instead?
                    deleteHologram(id);

                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
