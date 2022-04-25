package dev.efnilite.vilib.util.elevator;

import dev.efnilite.vilib.ViPlugin;
import dev.efnilite.vilib.util.Logging;
import dev.efnilite.vilib.util.Task;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private final VersionComparator comparator;

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
    public Elevator(ViPlugin plugin, String versionUrl, String downloadUrl, VersionComparator comparator) {
        this.plugin = plugin;
        this.versionUrl = versionUrl;
        this.downloadUrl = downloadUrl;
        this.comparator = comparator;

        check();
    }

    /**
     * Checks if this plugin version is outdated. Automatically called on instantiation.
     */
    public void check() {
        new Task()
                .async()
                .execute(() -> {
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

                        outdated = !comparator.isLatest(plugin.getDescription().getVersion(), version);

                        stream.close();
                        reader.close();
                    } catch (Throwable throwable) {
                        ViPlugin.logging().error("There was an error while checking the latest version");
                    }
                })
                .run();
    }

    public void elevate() {
        new Task()
                .async()
                .execute(() -> {
                    try {
                        // todo
                        InputStream stream = new URL(downloadUrl).openStream();

                        File folder = plugin.getDataFolder();
                        File jar = getJar();
                        if (jar == null) {
                            return;
                        }

                        Path full = Paths.get(folder.toString(), jar.toString());

                        Files.copy(stream, full, StandardCopyOption.REPLACE_EXISTING); // replace current jar

                        stream.close();

                        ViPlugin.logging().info("A new version of " + plugin.getDescription().getName() + " has been downloaded.");
                        ViPlugin.logging().info("A server restart is required for this download to take effect.");
                    } catch (Throwable throwable) {
                        ViPlugin.logging().error("There was an error while updating to the latest version");
                    }
                })
                .run();
    }

    private File getJar() {
        try {
            Method method = JavaPlugin.class.getDeclaredMethod("getFile");
            method.setAccessible(true);
            return (File) method.invoke(this.plugin);
        } catch (ReflectiveOperationException ex) {
            ViPlugin.logging().stack("Failed to get plugin jar name", "Please report this error to the developer!", ex);
            return null;
        }
    }

    public boolean isOutdated() {
        return outdated;
    }
}
