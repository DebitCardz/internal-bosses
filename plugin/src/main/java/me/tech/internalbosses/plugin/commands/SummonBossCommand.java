package me.tech.internalbosses.plugin.commands;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.tech.internalbosses.api.exceptions.BossSpawnFailedException;
import me.tech.internalbosses.plugin.InternalBossesPlugin;
import me.tech.internalbosses.plugin.boss.BossManagerImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class SummonBossCommand implements CommandExecutor {
    private final BossManagerImpl bossManager;

    private final MiniMessage mm;

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if(!sender.hasPermission("internalbosses.commands.summon")) {
            sender.sendMessage(mm.deserialize(
                    "<red>You are lacking the required permissions to execute this command."
            ));
            return true;
        }

        if(!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(this::getUsage);
            return true;
        }
        String bossId = args[0].toLowerCase();

        try {
            bossManager.spawnBoss(bossId, player.getLocation());

            sender.sendMessage(mm.deserialize(
                    "<green>You have spawned the boss <yellow><boss_id><green>.",
                    Placeholder.unparsed("boss_id", bossId)
            ));
        } catch(BossSpawnFailedException ex) {
            sender.sendMessage(mm.deserialize(
                    "<red>Sorry! <yellow><boss_id> <red>couldn't be spawned.",
                    Placeholder.unparsed("boss_id", bossId)
            ));
        }

        return true;
    }

    private @NotNull Component getUsage() {
        return mm.deserialize("<red>Usage: /summonboss <id>.");
    }
}
