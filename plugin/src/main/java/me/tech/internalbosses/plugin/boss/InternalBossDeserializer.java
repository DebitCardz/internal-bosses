package me.tech.internalbosses.plugin.boss;

import me.tech.internalbosses.api.boss.InternalBoss;
import me.tech.internalbosses.api.boss.InternalBossBuilder;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbility;
import me.tech.internalbosses.api.exceptions.BossFailedToLoadException;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

public class InternalBossDeserializer {
    public static InternalBoss fromConfig(
            ConfigurationSection boss,
            InternalBossAbilityManagerImpl abilityManager
    ) throws BossFailedToLoadException, IllegalArgumentException, NullPointerException {
        if(boss == null) {
            throw new NullPointerException("configuration doesn't exist.");
        }

        String entityTypeStr = boss.getString("entity");
        if(entityTypeStr == null) {
            throw new BossFailedToLoadException();
        }
        EntityType entityType = EntityType.valueOf(entityTypeStr);

        var abilities = new HashSet<InternalBossAbility>();
        for(String abilityName : boss.getStringList("abilities")) {
            Optional<InternalBossAbility> possibleAbility = abilityManager.getById(abilityName);
            if(possibleAbility.isEmpty()) {
//                throw new BossFailedToLoadException();
                continue;
            }

            abilities.add(possibleAbility.get());
        }

        return InternalBossBuilder.builder()
                .name(boss.getString("name"))
                .nameColor(boss.getString("name_color"))
                .health(boss.getDouble("health"))
                .entityType(entityType)
                .glowing(boss.getBoolean("glowing"))
                .equipment(deserializeEquipmentSlots(
                        boss.getConfigurationSection("equipment.armor"),
                        boss.getConfigurationSection("equipment.hands")
                ))
                .loot(Set.of())
                .abilities(abilities)
                .alertOnSummon(boss.getBoolean("alert_on_summon"))
                .build();
    }

    private static Map<EquipmentSlot, ItemStack> deserializeEquipmentSlots(ConfigurationSection ...sections) {
        Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();

        for(ConfigurationSection section : sections) {
            equipment.putAll(deserializeEquipmentSlots(section));
        }

        return equipment;
    }

    private static Map<EquipmentSlot, ItemStack> deserializeEquipmentSlots(@Nullable ConfigurationSection section) {
        Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();

        if(section != null) {
            for(String slotStr : section.getKeys(false)) {
                deserializeItemStackFromConfig(section.getConfigurationSection(slotStr))
                        .ifPresent(
                                stack -> equipment.put(equipmentSlotStrToEquipmentSlot(slotStr), stack)
                        );
            }
        }

        return equipment;
    }

    private static EquipmentSlot equipmentSlotStrToEquipmentSlot(String str) {
        return switch(str.toLowerCase()) {
            case "helmet" -> EquipmentSlot.HEAD;
            case "chestplate" -> EquipmentSlot.CHEST;
            case "leggings" -> EquipmentSlot.LEGS;
            case "boots" -> EquipmentSlot.FEET;
            case "primary" -> EquipmentSlot.HAND;
            case "secondary" -> EquipmentSlot.OFF_HAND;
            default -> null;
        };
    }

    private static Optional<ItemStack> deserializeItemStackFromConfig(ConfigurationSection item) {
        if(item == null || item.getString("item") == null) {
            return Optional.empty();
        }

        ItemStack stack = new ItemStack(Material.valueOf(item.getString("item").toUpperCase()));

        if(item.isSet("enchantments")) {
            for(String unparsedEnchant : item.getStringList("enchantments")) {
                parseEnchantment(stack, unparsedEnchant);
            }
        }

        if(item.getBoolean("glowing")) {
            if(stack.getType() != Material.BOW) {
                stack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
            } else {
                stack.addUnsafeEnchantment(Enchantment.LUCK, 1);
            }

            stack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        return Optional.of(stack);
    }

    private static void parseEnchantment(ItemStack stack, String unparsedEnchant) {
        String[] strings = unparsedEnchant.split(":");

        Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(strings[0].toLowerCase()));
        if(enchant == null) {
            throw new IllegalArgumentException(strings[0] + " is not a valid enchantment id.");
        }

        int level;
        try {
            level = Integer.parseInt(strings[1]);
        } catch(NumberFormatException ignored) {
            level = 1;
        }

        stack.addUnsafeEnchantment(enchant, level);
    }

    private static Set<InternalBossAbility> deserializeAbilities(
            @Nullable List<String> abilitiesList,
            InternalBossAbilityManagerImpl abilityManager
    ) {
        Set<InternalBossAbility> abilities = new HashSet<>();

        if(abilitiesList != null && abilitiesList.size() != 0) {
           for(String unparsedAbility : abilitiesList) {
                abilityManager.getById(unparsedAbility)
                        .ifPresent(abilities::add);
           }
        }

        return abilities;
    }
}