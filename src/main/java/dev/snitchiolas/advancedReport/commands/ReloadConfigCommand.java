package dev.snitchiolas.advancedReport.commands;

import dev.snitchiolas.advancedReport.Initializer;
import dev.snitchiolas.advancedReport.files.MessagesYML;
import dev.snitchiolas.advancedReport.utils.StorageFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.snitchiolas.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand implements CommandExecutor {
    private final MessagesYML messages;
    private final StorageFile file;
    private final Initializer config;

    public ReloadConfigCommand(MessagesYML messages, StorageFile file, Initializer config) {
        this.messages = messages;
        this.file = file;
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.color(this.messages.getConfig().getString("messages.general.not-a-player")));
            return true;
        }

        final Player player = (Player) sender;

        if (!player.hasPermission("report.command.reload-config")) {
            player.sendMessage(ColorUtils.color(this.messages.getConfig().getString("messages.general.insufficient-permissions")));
            return true;
        }

        this.file.reload();
        this.messages.reload();
        this.config.reloadConfig();

        player.sendMessage(ColorUtils.color(this.messages.getConfig().getString("messages.general.config-reloaded")));

        return true;
    }
}
