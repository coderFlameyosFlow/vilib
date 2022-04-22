package dev.efnilite.vilib.util;

import dev.efnilite.vilib.ViPlugin;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

/**
 * Elevator class to automatically check for and update plugins.
 * Lowers the barrier of downloading a new version, which is useful for improving "updatedness" retention.
 */
public class Elevator {

    private boolean outdated;

    private final String versionUrl;
    private final String downloadUrl;
    private final ViPlugin plugin;

    /**
     * Constructor of this Elevator. Automatically checks for updates on creation.
     *
     * @param   plugin
     *          The plugin. Used to get the current version.
     *
     * @param   versionUrl
     *          The url of the plugin.yml (or other files) to check versions with.
     *          File must contain the following syntax: 'version: '
     *
     * @param   downloadUrl
     *          The url where to download the latest file from.
     */
    public Elevator(ViPlugin plugin, String versionUrl, String downloadUrl) {
        this.plugin = plugin;
        this.versionUrl = versionUrl;
        this.downloadUrl = downloadUrl;

        check();
    }

    /**
     * Checks if this plugin version is outdated. Automatically called on instantiation.
     */
    public void check() {
        try {
            InputStream stream = new URL(versionUrl).openStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream)); // buffered to improve performance
            String version = reader.lines()
                    .filter(s -> s.startsWith("version:")) // only get the version
                    .collect(Collectors.toList())
                    .get(0)
                    .replace("version:", "")
                    .replaceAll("['\"]", "")
                    .trim(); // remove trailing spaces

            outdated = plugin.getDescription().getVersion().equals(version);

            stream.close();
            reader.close();
        } catch (Throwable throwable) {
            Logging.error("There was an error while checking the latest version");
        }
    }

    public void elevate() {
        try {
            InputStream stream = new URL(downloadUrl).openStream();
            Files.copy(stream, Paths.get(plugin.getDataFolder().getParent()), StandardCopyOption.REPLACE_EXISTING);
            // todo

            stream.close();

            Logging.info("A new version of " + plugin.getDescription().getName() + " has been downloaded.");
            Logging.info("A server restart is required for this download to take effect.");
        } catch (Throwable throwable) {
            Logging.error("There was an error while updating to the latest version");
        }
    }
}
