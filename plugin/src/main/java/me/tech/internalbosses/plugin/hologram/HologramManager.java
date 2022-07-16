package me.tech.internalbosses.plugin.hologram;

import me.tech.internalbosses.plugin.InternalBossesPlugin;
import me.tech.internalbosses.plugin.hologram.decentholograms.DHHologramManager;
import me.tech.internalbosses.plugin.hologram.holographicdisplays.HDHologramManager;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Deprecated(forRemoval = false)
public interface HologramManager {
    UUID createHologram(Location location, List<String> lines);

    void deleteHologram(UUID id);

    void followEntity(UUID id, LivingEntity entity);

    default double getHologramLocationAboveHead(double eyeHeight) {
        return eyeHeight + 0.65;
    }

    static @Nullable HologramManager create(InternalBossesPlugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();

        if(pm.isPluginEnabled("HolographicDisplays")) {
            return new HDHologramManager(plugin);
        }

        if(pm.isPluginEnabled("DecentHolograms")) {
            return new DHHologramManager(plugin);
        }

        return null;
    }
}
