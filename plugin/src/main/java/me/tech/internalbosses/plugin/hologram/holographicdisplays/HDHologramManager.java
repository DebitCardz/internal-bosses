package me.tech.internalbosses.plugin.hologram.holographicdisplays;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.RequiredArgsConstructor;
import me.tech.internalbosses.plugin.InternalBossesPlugin;
import me.tech.internalbosses.plugin.hologram.HologramManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@RequiredArgsConstructor
public class HDHologramManager implements HologramManager {
    private final InternalBossesPlugin plugin;

    private final Map<UUID, Hologram> holograms = new HashMap<>();

    @Override
    public UUID createHologram(Location location, List<String> lines) {
        UUID hologramId = UUID.randomUUID();
        Hologram hdHologram = HologramsAPI.createHologram(plugin, location);
        // thanks hd.
        lines.forEach(l -> hdHologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', l)));

        holograms.put(
                hologramId,
                hdHologram
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
                    hologram.teleport(entity.getLocation().add(
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
