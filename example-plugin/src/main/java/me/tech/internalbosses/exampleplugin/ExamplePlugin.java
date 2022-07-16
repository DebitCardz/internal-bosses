package me.tech.internalbosses.exampleplugin;

import me.tech.internalbosses.api.InternalBossesAPI;
import me.tech.internalbosses.api.boss.InternalBoss;
import me.tech.internalbosses.api.events.InternalBossDamagedByPlayerEvent;
import me.tech.internalbosses.api.events.InternalBossKilledEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class ExamplePlugin extends JavaPlugin implements Listener {
    private InternalBossesAPI internalBossesAPI;

    @Override
    public void onEnable() {
        ServicesManager sm =  getServer().getServicesManager();

        internalBossesAPI = sm.load(InternalBossesAPI.class);
        if(internalBossesAPI == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        var boss = InternalBoss.builder()
                .name("sus")
                .nameColor("&c")
                .entityType(EntityType.ZOMBIE)
                .glowing(true)
                .alertOnSummon(true)
                .health(30)
                .equipment(Map.of(EquipmentSlot.HEAD, new ItemStack(Material.DIAMOND_HELMET)))
                .ability(internalBossesAPI.getAbilityById("fireball").get())
                .build();

        internalBossesAPI.addBoss("sus", boss);

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        getServer().getScheduler().runTaskLater(this, () -> internalBossesAPI.summonBoss(
                "test",
                ev.getPlayer().getLocation().add(0.0, 2.5, 0.0)
        ), 20L * 5);
    }

    @EventHandler
    public void onBossDamage(InternalBossDamagedByPlayerEvent ev) {
        ev.getAttacker().sendMessage("You've hit " + ev.getBoss().getUncoloredName());
    }

    @EventHandler
    public void onBossKilled(InternalBossKilledEvent ev) {
        ev.getKiller().sendMessage(Component.text("You've killed " + ev.getBoss().getUncoloredName(), NamedTextColor.RED));
    }
}