package me.tech.internalbosses.plugin;

import lombok.Getter;
import me.tech.internalbosses.api.InternalBossesAPI;
import me.tech.internalbosses.plugin.boss.BossManagerImpl;
import me.tech.internalbosses.plugin.boss.InternalBossAbilityManagerImpl;
import me.tech.internalbosses.plugin.commands.SummonBossCommand;
import me.tech.internalbosses.plugin.hologram.HologramManager;
import me.tech.internalbosses.plugin.listeners.InternalBossListeners;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

@Getter
public final class InternalBossesPlugin extends JavaPlugin {
    private BossManagerImpl bossManager;

    private InternalBossAbilityManagerImpl abilityManager;

    @Nullable
    private HologramManager hologramManager;

    @Getter
    private MiniMessage miniMessage = null;

    @Override
    public void onLoad() {
        miniMessage = MiniMessage.builder().build();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // We can just use custom entity names, I had an idea for holograms
        // but don't think it's necessary to implement.
        hologramManager = null;
        /*hologramManager = HologramManager.create(this);
        if(hologramManager == null) {
            getLogger().warning("No hologram manager was initialized.");
        }*/

        abilityManager = new InternalBossAbilityManagerImpl();
        bossManager = new BossManagerImpl(getServer().getPluginManager(), abilityManager, hologramManager, miniMessage);

        try {
            bossManager.load(getConfig().getConfigurationSection("bosses"));
        } catch(NullPointerException ex) {
            ex.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);

            return;
        }

        Bukkit.getServicesManager().register(
                InternalBossesAPI.class,
                new InternalBossesAPI(bossManager, abilityManager),
                this,
                ServicePriority.High
        );

        Arrays.asList(
                new InternalBossListeners(bossManager, getServer(), getServer().getPluginManager(), miniMessage)
        ).forEach(l -> getServer().getPluginManager().registerEvents(l, this));

        getCommand("summonboss").setExecutor(new SummonBossCommand(bossManager, miniMessage));
    }

    @Override
    public void onDisable() {

    }
}
