package me.tech.internalbosses.api.boss.loot;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@RequiredArgsConstructor
public class InternalBossItemLoot implements InternalBossLoot<ItemStack> {
    private final ItemStack item;

    @Override
    public ItemStack get() {
        return item;
    }

    @Override
    public void give(Player player) {
        PlayerInventory inv = player.getInventory();

        // TODO: 7/16/2022 Do some magic to check to see if the player has an existing item in their inventory.
        if(inv.firstEmpty() != -1) {
            inv.addItem(item);
            return;
        }
        // If they can't hold it just drop it lol
        player.getWorld().dropItemNaturally(
                player.getLocation(),
                item
        );
    }
}
