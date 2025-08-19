package dev.snitchiolas.advancedReport.files;

import dev.snitchiolas.advancedReport.utils.StorageFile;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffToggleYML extends StorageFile {
    public StaffToggleYML(JavaPlugin plugin) {
        super("staff-toggle", plugin);
    }
}
