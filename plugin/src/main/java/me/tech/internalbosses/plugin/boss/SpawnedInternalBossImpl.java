package me.tech.internalbosses.plugin.boss;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.tech.internalbosses.api.boss.InternalBoss;
import me.tech.internalbosses.api.boss.SpawnedInternalBoss;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

@Getter
@RequiredArgsConstructor
public class SpawnedInternalBossImpl implements SpawnedInternalBoss {
    private final InternalBoss boss;

    private final LivingEntity entity;

    private final Location spawnLocation;

    public void applyPotionEffects() {
        boss.getEffects().forEach(entity::addPotionEffect);
    }

    public void applyHealth() {
        entity.registerAttribute(Attribute.GENERIC_MAX_HEALTH);
        var attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if(attribute != null) {
            attribute.setBaseValue(boss.getHealth());
        }

        // Guess they just don't get any additional health.
        try {
            entity.setHealth(boss.getHealth());
        } catch(IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    public void applyEquipment() {
        EntityEquipment equipment = entity.getEquipment();
        if(equipment == null) {
            return;
        }

        boss.getEquipment().forEach((slot, item) -> {
            equipment.setItem(slot, item);
            equipment.setDropChance(slot, 0);
        });
    }

    public static SpawnedInternalBossImpl create(
            InternalBoss boss,
            LivingEntity entity,
            Location location
    ) {
        var spawnedBoss = new SpawnedInternalBossImpl(boss, entity, location);

        spawnedBoss.applyEquipment();
        spawnedBoss.applyHealth();
        spawnedBoss.applyPotionEffects();

        return spawnedBoss;
    }
}
