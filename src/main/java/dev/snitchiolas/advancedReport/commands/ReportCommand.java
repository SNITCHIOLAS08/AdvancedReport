package dev.snitchiolas.advancedReport.commands;

import dev.snitchiolas.advancedReport.Initializer;
import dev.snitchiolas.advancedReport.files.MessagesYML;
import dev.snitchiolas.advancedReport.files.StaffToggleYML;
import dev.snitchiolas.advancedReport.utils.DiscordWebhook;
import dev.snitchiolas.utils.ColorUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import net.md_5.bungee.api.chat.HoverEvent;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReportCommand implements CommandExecutor {
    private final StaffToggleYML stafftoggle;
    private final MessagesYML messages;
    private final Initializer config;

    public ReportCommand(StaffToggleYML stafftoggle, MessagesYML messages, Initializer config) {
        this.stafftoggle = stafftoggle;
        this.messages = messages;
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly player can run this command");
            return true;
        }

        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();
        final String path = "players." + uuid + ".staffchat.report";
        final boolean currentState = this.stafftoggle.getConfig().getBoolean(path, false);
        final String webhookUrl = this.config.getConfig().getString("discord.webhook-url");


        if (args.length < 1 || player.hasPermission("report.command.alerts")) {

            if (currentState == false) {
                if (!player.hasPermission("report.staff.report")) return true;
                this.stafftoggle.getConfig().set(path, true);
                player.sendMessage(ColorUtils.color(this.messages.getConfig().getString("messages.report.alerts-enable")));
                return true;
            }

            if (currentState == true) {
                if (!player.hasPermission("report.staff.report")) return true;
                this.stafftoggle.getConfig().set(path, false);
                player.sendMessage(ColorUtils.color(this.messages.getConfig().getString("messages.report.alerts-disable")));
                return true;
            }
        }

        if (args.length > 1) {
            final Player target = Bukkit.getPlayer(args[0]);
            final String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            List<String> messageReport = this.messages.getConfig().getStringList("messages.report.recive-report");

            final String ban = ColorUtils.color(this.messages.getConfig().getString("messages.report.buttons.ban.name"));
            final String banDescription = ColorUtils.color(this.messages.getConfig().getString("messages.report.buttons.ban.description"));

            final String spectate = ColorUtils.color(this.messages.getConfig().getString("messages.report.buttons.spectate.name"));
            final String spectateDescription = ColorUtils.color(this.messages.getConfig().getString("messages.report.buttons.spectate.description"));

            TextComponent banMessage = new TextComponent(TextComponent.fromLegacyText(ban));
            banMessage.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban " + target.getName() + " Cheating"));
            banMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(banDescription)));

            TextComponent spectateMessage = new TextComponent(TextComponent.fromLegacyText(spectate));
            spectateMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spectate " + target.getName()));
            spectateMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(spectateDescription)));


            List<String> reportRecived = ColorUtils.colorList(
                    messageReport.stream()
                            .map(line -> line
                                    .replace("%target%", target.getName())
                                    .replace("%reporter%", player.getName())
                                    .replace("%reason%", reason))
                            .collect(Collectors.toList())
            );

            for (Player players : Bukkit.getOnlinePlayers()) {
                if (staffReportEnable(players)) {
                    if (players.hasPermission("report.staff.report")) {
                        for (String line : reportRecived) {
                            players.sendMessage(line);
                        }
                        players.sendMessage(banMessage);
                        players.sendMessage(spectateMessage);
                    }
                }
            }

            if (webhookUrl != null && !webhookUrl.isEmpty()) {
                String webhookMessage = "**New Report Received!**\n" +
                        "👤 Reporter: " + player.getName() + "\n" +
                        "🎯 Target: " + target.getName() + "\n" +
                        "📄 Reason: " + reason;

                DiscordWebhook.sendWebhook(webhookUrl, webhookMessage);
            }

            player.sendMessage(ColorUtils.color(this.messages.getConfig().getString("messages.report.report-sent").replace("%target%", target.getName())));

            return true;
        }

        return true;
    }

    public boolean staffReportEnable(Player players) {
        return this.stafftoggle.getConfig().getBoolean("players." + players.getUniqueId() + ".staffchat.report", false);
    }
}
