package me.tech.internalbosses.plugin.boss;

import lombok.RequiredArgsConstructor;
import me.tech.internalbosses.api.boss.InternalBoss;
import me.tech.internalbosses.api.boss.InternalBossManager;
import me.tech.internalbosses.api.boss.SpawnedInternalBoss;
import me.tech.internalbosses.api.events.InternalBossSummonedEvent;
import me.tech.internalbosses.api.exceptions.BossSpawnFailedException;
import me.tech.internalbosses.plugin.hologram.HologramManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nullable;
import java.util.*;

@RequiredArgsConstructor
public class BossManagerImpl implements InternalBossManager {
    private final PluginManager pm;

    private final InternalBossAbilityManagerImpl abilityManager;

    @Nullable
    private final HologramManager hologramManager;

    private final MiniMessage mm;

    private final Map<String, InternalBoss> bosses = new HashMap<>();

    private final Map<UUID, SpawnedInternalBoss> spawnedBosses = new HashMap<>();

    public void load(@Nullable ConfigurationSection bosses) throws NullPointerException {
        if(bosses == null) {
            throw new NullPointerException("bosses section doesn't exist.");
        }

        this.bosses.clear();
        bosses.getKeys(false).forEach(key -> addBoss(key, InternalBossDeserializer.fromConfig(bosses.getConfigurationSection(key), abilityManager)));
    }

    @Override
    public void addBoss(String id, InternalBoss boss) {
        bosses.put(
                id,
                boss
        );
    }

    @Override
    public void removeBoss(String id) {
        bosses.remove(id);
    }

    @Override
    public UUID spawnBoss(String id, Location location) throws BossSpawnFailedException {
        Optional<InternalBoss> possibleBoss = getBossById(id);
        if(possibleBoss.isEmpty()) {
            throw new BossSpawnFailedException();
        }
        InternalBoss boss = possibleBoss.get();

        Entity entity = location.getWorld().spawnEntity(
                location,
                boss.getEntityType()
        );

        LivingEntity livingEntity;
        // how.
        if(entity.isDead()) {
            throw new BossSpawnFailedException();
        }
        livingEntity = (LivingEntity) entity;

        // Name always visible.
        livingEntity.setCustomNameVisible(true);
        livingEntity.customName(mm.deserialize(boss.getName()));

        livingEntity.setRemoveWhenFarAway(false);
        livingEntity.setCanPickupItems(false);

        // bro be glowin
        if(boss.isGlowing()) {
            livingEntity.setGlowing(true);
        }

        SpawnedInternalBossImpl spawnedBoss = SpawnedInternalBossImpl.create(
                boss,
                livingEntity,
                location
        );

        spawnedBosses.put(
                livingEntity.getUniqueId(),
                spawnedBoss
        );

        if(hologramManager != null) {
            UUID hologramId = hologramManager.createHologram(
                    livingEntity.getLocation(),
                    List.of(boss.getName())
            );

            hologramManager.followEntity(hologramId, livingEntity);
        }

        pm.callEvent(new InternalBossSummonedEvent(
                boss,
                spawnedBoss,
                location
        ));

        return livingEntity.getUniqueId();
    }

    @Override
    public Optional<InternalBoss> getBossById(String id) {
        return Optional.ofNullable(bosses.getOrDefault(id, null));
    }

    @Override
    public Optional<SpawnedInternalBoss> getSpawnedBossById(UUID uuid) {
        return Optional.ofNullable(spawnedBosses.getOrDefault(uuid, null));
    }
}
