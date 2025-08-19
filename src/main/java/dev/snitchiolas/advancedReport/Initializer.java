package dev.snitchiolas.advancedReport;

import dev.snitchiolas.advancedReport.commands.ReloadConfigCommand;
import dev.snitchiolas.advancedReport.commands.ReportCommand;
import dev.snitchiolas.advancedReport.commands.SpectatePlayerCommand;
import dev.snitchiolas.advancedReport.files.MessagesYML;
import dev.snitchiolas.advancedReport.files.StaffToggleYML;
import dev.snitchiolas.advancedReport.managers.VanishManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Initializer extends JavaPlugin {
    private static Initializer instance;
    public static MessagesYML MESSAGES;
    public static StaffToggleYML DATA;
    public static VanishManager VANISHMANAGER;

    private void logConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        MESSAGES = new MessagesYML(this);
        DATA = new StaffToggleYML(this);
        VANISHMANAGER = new VanishManager();
        VanishManager.init(MESSAGES);

        getCommand("report").setExecutor(new ReportCommand(DATA, MESSAGES, this));
        getCommand("reportreload").setExecutor(new ReloadConfigCommand(MESSAGES, DATA, this));
        getCommand("spectate").setExecutor(new SpectatePlayerCommand(MESSAGES, DATA, VANISHMANAGER));

        logConsole("&8&m==================================================");
        logConsole("██████╗ ███████╗██████╗  ██████╗ ██████╗ ████████╗");
        logConsole("██╔══██╗██╔════╝██╔══██╗██╔═══██╗██╔══██╗╚══██╔══╝");
        logConsole("██████╔╝█████╗  ██████╔╝██║   ██║██████╔╝   ██║   ");
        logConsole("██╔══██╗██╔══╝  ██╔═══╝ ██║   ██║██╔══██╗   ██║   ");
        logConsole("██║  ██║███████╗██║     ╚██████╔╝██║  ██║   ██║   ");
        logConsole("╚═╝  ╚═╝╚══════╝╚═╝      ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ");
        logConsole(" ");
        logConsole("&a[AdvancedReport] &fPlugin has been &aenabled &fsuccessfully!");
        logConsole("&7Official Website: &bhttps://snitchiolas.netlify.app/");
        logConsole("&8&m==================================================");
    }

    @Override
    public void onDisable() {
        MESSAGES.save();
        DATA.save();
        VanishManager.reset();

        logConsole("&8&m==================================================");
        logConsole("██████╗ ███████╗██████╗  ██████╗ ██████╗ ████████╗");
        logConsole("██╔══██╗██╔════╝██╔══██╗██╔═══██╗██╔══██╗╚══██╔══╝");
        logConsole("██████╔╝█████╗  ██████╔╝██║   ██║██████╔╝   ██║   ");
        logConsole("██╔══██╗██╔══╝  ██╔═══╝ ██║   ██║██╔══██╗   ██║   ");
        logConsole("██║  ██║███████╗██║     ╚██████╔╝██║  ██║   ██║   ");
        logConsole("╚═╝  ╚═╝╚══════╝╚═╝      ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ");
        logConsole(" ");
        logConsole("&8&m==================================================");
        logConsole("&c[AdvancedReport] &fPlugin has been &cdisabled &fcorrectly!");
        logConsole("&7Official Website: &bhttps://snitchiolas.netlify.app/");
        logConsole("&8&m==================================================");
    }

}
