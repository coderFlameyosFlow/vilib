package dev.efnilite.vilib.util.elevator;

import dev.efnilite.vilib.ViMain;
import dev.efnilite.vilib.ViPlugin;
import dev.efnilite.vilib.util.Task;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Elevator class to automatically check for and update plugins.
 * Lowers the barrier of downloading a new version, which is useful for improving "updatedness" retention.
 */
public class Elevator {

    private boolean outdated;
    private String newerVersion;
    private String versionUrl;
    private String downloadUrl;
    private final ViPlugin plugin;
    private VersionComparator comparator;
    private VersionRetriever retriever;
    private boolean downloadIfOutdated;

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
    public Elevator(ViPlugin plugin, String versionUrl, String downloadUrl, boolean downloadIfOutdated) {
        this.plugin = plugin;
        this.versionUrl = versionUrl;
        this.downloadUrl = downloadUrl;
        this.downloadIfOutdated = downloadIfOutdated;
    }

    /**
     * Sets the comparator of this Elevator.
     *
     * @param   comparator
     *          The comparator type to be used.
     *
     * @return the instance of this class
     */
    public Elevator comparator(VersionComparator comparator) {
        this.comparator = comparator;
        return this;
    }

    /**
     * Sets the retriever type of this Elevator.
     *
     * @param   retriever
     *          The retriever type.
     *
     * @return the instance of this class
     */
    public Elevator retriever(VersionRetriever retriever) {
        this.retriever = retriever;
        return this;
    }

    // todo get latest version via github rest and then the tag name -v

    /**
     * Checks if this plugin version is outdated. Automatically called on instantiation.
     */
    public void check() {
        Task.create(ViMain.getPlugin())
                .async()
                .execute(() -> {
                    try {
                        newerVersion = retriever.getLatestVersion(versionUrl);
                        outdated = !comparator.isLatest(plugin.getDescription().getVersion(), newerVersion);

                        if (outdated) {
                            ViMain.logging().info("A new version of " + plugin.getName() + " has been found.");
                            if (downloadIfOutdated) {
                                ViMain.logging().info(plugin.getName() + " will now be updated...");
                                elevate();
                            } else {
                                ViMain.logging().info("Please update!");
                            }
                        }
                    } catch (Throwable throwable) {
                        ViMain.logging().error("There was an error while checking the latest version");
                    }
                })
                .run();
    }

    public void elevate() {
        Task.create(ViMain.getPlugin())
                .async()
                .execute(() -> {
                    try {
                        if (!outdated) {
                            return;
                        }
                        File jar = getJar();
                        if (jar == null) {
                            return;
                        }

                        InputStream stream = new URL(downloadUrl).openStream();
                        ReadableByteChannel channel = Channels.newChannel(stream);
                        FileOutputStream output = new FileOutputStream(jar);

                        output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);

                        channel.close();
                        output.flush();
                        output.close();
                        stream.close();

                        ViMain.logging().info("A new version of " + plugin.getName() + " has been downloaded.");
                        ViMain.logging().info("A server restart is required for this download to take effect.");
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        ViMain.logging().error("There was an error while updating to the latest version");
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
            ViMain.logging().stack("Failed to get plugin jar name", ex);
            return null;
        }
    }

    public boolean isOutdated() {
        return outdated;
    }
}
