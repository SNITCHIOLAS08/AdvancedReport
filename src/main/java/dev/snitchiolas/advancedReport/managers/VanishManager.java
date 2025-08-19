package dev.snitchiolas.advancedReport.managers;

import dev.snitchiolas.advancedReport.files.MessagesYML;
import dev.snitchiolas.advancedReport.utils.ColorUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager {

    private static final Set<UUID> vanishedPlayers = new HashSet<>();
    private static int taskId = -1;
    private static MessagesYML messages;

    public static void init(MessagesYML msgFile) {
        messages = msgFile;

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Bukkit.getPluginManager().getPlugin("AdvancedReport"),
                () -> {
                    String actionbarMsg = messages.getConfig().getString("messages.vanish.actionbar");
                    if (actionbarMsg == null) return;

                    for (UUID uuid : vanishedPlayers) {
                        Player p = Bukkit.getPlayer(uuid);
                        if (p != null && p.isOnline()) {
                            p.spigot().sendMessage(
                                    ChatMessageType.ACTION_BAR,
                                    new TextComponent(ColorUtils.color(actionbarMsg))
                            );
                        }
                    }
                },
                0L, 40L
        );
    }

    public static void vanish(Player player) {
        vanishedPlayers.add(player.getUniqueId());

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.hasPermission("report.staff.see-vanish")) {
                online.hidePlayer(player);
            }
        }

        player.sendMessage(ColorUtils.color(
                messages.getConfig().getString("messages.vanish.enable")
        ));
    }

    public static void unvanish(Player player) {
        vanishedPlayers.remove(player.getUniqueId());

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.showPlayer(player);
        }

        player.sendMessage(ColorUtils.color(
                messages.getConfig().getString("messages.vanish.disable")
        ));
    }

    public static boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }

    public static void reset() {
        for (UUID uuid : vanishedPlayers) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.showPlayer(p);
                }
            }
        }
        vanishedPlayers.clear();

        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }
}
