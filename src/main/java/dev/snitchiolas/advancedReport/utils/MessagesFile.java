package dev.snitchiolas.advancedReport.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.io.InputStreamReader;

public class MessagesFile {
    protected final File file;
    protected FileConfiguration config;
    private final JavaPlugin plugin;
    private final String fileName;

    public MessagesFile(String fileName, JavaPlugin plugin) {
        this.plugin = plugin;
        this.fileName = fileName + ".yml";
        File storageFolder = new File(plugin.getDataFolder(), "messages");

        if (!storageFolder.exists()) {
            storageFolder.mkdirs();
        }

        this.file = new File(storageFolder, this.fileName);

        if (!this.file.exists()) {
            try {
                InputStream inputStream = plugin.getResource("messages/" + this.fileName);
                if (inputStream != null) {
                    Files.copy(inputStream, this.file.toPath());
                } else {
                    this.file.createNewFile();
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create file " + this.fileName);
                e.printStackTrace();
            }
        }

        this.reload();
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save file " + fileName);
            e.printStackTrace();
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);

        InputStream defConfigStream = plugin.getResource("data/" + fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            this.config.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}