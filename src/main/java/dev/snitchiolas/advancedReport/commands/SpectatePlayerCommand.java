package dev.snitchiolas.advancedReport.commands;

import dev.snitchiolas.advancedReport.files.MessagesYML;
import dev.snitchiolas.advancedReport.files.StaffToggleYML;
import dev.snitchiolas.advancedReport.managers.VanishManager;
import dev.snitchiolas.advancedReport.utils.InventorySave;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.snitchiolas.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SpectatePlayerCommand implements CommandExecutor {
    private final MessagesYML messages;
    private final StaffToggleYML stafftoggle;
    private final VanishManager vanish;

    public SpectatePlayerCommand(MessagesYML messages, StaffToggleYML stafftoggle, VanishManager vanish) {
        this.messages = messages;
        this.stafftoggle = stafftoggle;
        this.vanish = vanish;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtils.color(this.messages.getConfig().getString("messages.general.not-a-player")));
            return true;
        }

        final Player player = (Player) sender;

        if (!player.hasPermission("report.command.spectate")) {
            player.sendMessage(ColorUtils.color(this.messages.getConfig().getString("messages.general.insufficient-permissions")));
            return true;
        }

        final UUID uuid = player.getUniqueId();

        final String path = "players." + uuid + ".staffreport.spectmode";
        final String spectPath = "players." + uuid + ".staffreport.old-location";

        final boolean currentState = this.stafftoggle.getConfig().getBoolean(path);
        final Location spectCurrentLocation = this.stafftoggle.getConfig().getLocation(spectPath);

        final Player target = Bukkit.getPlayer(args[0]);
        final Location playerLoc = player.getLocation();

        if (target == sender) {
            player.sendMessage(ColorUtils.color(this.messages.getConfig().getString("messages.report.cant-spect-yourself")));
            return true;
        }

        if (target == null) {
            player.sendMessage(ColorUtils.color(this.messages.getConfig().getString("messages.spectate.player-offline")));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ColorUtils.color(this.messages.getConfig().getString("messages.spectate.usage")));
            return true;
        }

        if (currentState == false) {
            if (!player.hasPermission("report.staff.report")) return true;

            this.stafftoggle.getConfig().set(spectPath, playerLoc);
            this.stafftoggle.getConfig().set(path, true);

            player.sendMessage(dev.snitchiolas.utils.ColorUtils.color(this.messages.getConfig().getString("messages.report.spect-enable")));

            InventorySave.saveAndClearInventory(player);
            player.setAllowFlight(true);
            player.setFlying(true);

            vanish.vanish(player);

            player.teleport(target);
            return true;
        }

        if (currentState == true) {
            if (!player.hasPermission("report.staff.report")) return true;

            this.stafftoggle.getConfig().set(path, false);

            player.sendMessage(dev.snitchiolas.utils.ColorUtils.color(this.messages.getConfig().getString("messages.report.spect-disable")));

            InventorySave.loadInventory(player);
            player.setAllowFlight(false);
            player.setFlying(false);

            vanish.unvanish(player);

            player.teleport(spectCurrentLocation);

            return true;
        }

        return true;
    }
}
