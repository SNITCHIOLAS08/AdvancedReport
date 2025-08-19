package dev.snitchiolas.advancedReport.files;

import dev.snitchiolas.advancedReport.utils.MessagesFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MessagesYML extends MessagesFile {

    public MessagesYML(JavaPlugin plugin) {
        super("messages", plugin);
    }
}
