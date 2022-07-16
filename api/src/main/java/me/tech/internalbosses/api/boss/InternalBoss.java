package me.tech.internalbosses.api.boss;

import lombok.*;
import me.tech.internalbosses.api.Chance;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbility;
import me.tech.internalbosses.api.boss.loot.InternalBossLootBag;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class InternalBoss {
    @NonNull
    private String name;

    @NonNull
    private double health;

    @NonNull
    private EntityType entityType;

    @NonNull
    private boolean glowing;

    @NonNull
    private Map<EquipmentSlot, ItemStack> equipment;

    @NonNull
    private Set<InternalBossLootBag> loot;

    @Nullable
    private InternalBossLootBag defaultLootBag = null;

    @NonNull
    private Set<InternalBossAbility> abilities;

    @Getter(AccessLevel.NONE)
    @NonNull
    private boolean alertOnSummon;

    public String getUncoloredName() {
        return MiniMessage.miniMessage().stripTags(name);
    }

    /**
     * Reward the player with a random {@link InternalBossLootBag}
     * @param player Killer of the boss.
     * @return The possibly provided {@link InternalBossLootBag}.
     */
    public Optional<InternalBossLootBag> rewardLootBag(Player player) {
        InternalBossLootBag givenBag = null;

        for(InternalBossLootBag bag : loot) {
            if(
                    !bag.isDefaultDrop()
                    && Chance.successfulRandom(bag.getChance())
            ) {
                bag.giveLoot(player);
                givenBag = bag;

                break;
            }
        }

        // No bag has been given yet, try giving default.
        if(givenBag == null) {
            var defaultBag = getDefaultLootBag();
            if(defaultBag.isPresent()) {
                defaultBag.get().giveLoot(player);
                givenBag = defaultBag.get();
            }
        }

        return Optional.ofNullable(givenBag);
    }

    public Optional<InternalBossLootBag> getDefaultLootBag() {
        // caching magic.
        if(defaultLootBag == null && loot.size() != 0) {
            loot.stream()
                    .filter(InternalBossLootBag::isDefaultDrop)
                    .findFirst()
                    .ifPresent(bag -> defaultLootBag = bag);
        }

        return Optional.ofNullable(defaultLootBag);
    }

    public void addAbility(InternalBossAbility ability) {
        abilities.add(ability);
    }

    public void removeAbility(InternalBossAbility ability) {
        abilities.remove(ability);
    }

    public boolean getAlertOnSummon() {
        return alertOnSummon;
    }

    public static InternalBossBuilder builder() {
        return new InternalBossBuilder();
    }
}