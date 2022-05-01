package dev.efnilite.vilib;

import com.tchristofferson.configupdater.ConfigUpdater;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An utilities class for the Configuration
 */
public class Configuration {

    private final Plugin plugin;
    private final Map<String, FileConfiguration> files = new HashMap<>();

    /**
     * Create a new instance
     */
    public Configuration(Plugin plugin) {
        this.plugin = plugin;

        List<String> defaultFiles = Collections.singletonList("config.yml");
        for (String name : defaultFiles) {
            File file = new File(plugin.getDataFolder(), name);

            if (!file.exists()) {
                plugin.getDataFolder().mkdirs();

                plugin.saveResource(name, false);
            }
        }

        // Config files without languages
        try {
            ConfigUpdater.update(plugin, "config.yml", new File(plugin.getDataFolder(), "config.yml"), Collections.emptyList());
        } catch (IOException ex) {
            ViMain.logging().stack("Error while trying to update a config file", "delete all config files and restart the server", ex);
        }
        reload();
    }

    /**
     * Maps all files to aliases.
     */
    public void reload() {
        files.put("config", YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "/config.yml")));
    }
    /**
     * Gets a file
     *
     * @param file The name of the file
     * @return the FileConfiguration
     */
    public FileConfiguration getFile(String file) {
        FileConfiguration config;
        if (files.get(file) == null) {
            config = YamlConfiguration.loadConfiguration(new File(file));
            files.put(file, config);
        } else {
            config = files.get(file);
        }
        return config;
    }
}