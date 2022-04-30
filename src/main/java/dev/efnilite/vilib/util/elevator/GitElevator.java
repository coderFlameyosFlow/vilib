package dev.efnilite.vilib.util.elevator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.efnilite.vilib.ViMain;
import dev.efnilite.vilib.ViPlugin;
import dev.efnilite.vilib.util.Task;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Elevator class to automatically check for and update plugins.
 * Used for open-source plugins, specifically hosted on GitHub.
 * Lowers the barrier of downloading a new version, which is useful for improving "updatedness" retention.
 */
public class GitElevator {

    private boolean outdated;
    private String newerVersion;
    private String downloadUrl;
    private VersionComparator comparator;
    private final String repo;
    private final ViPlugin plugin;
    private final boolean downloadIfOutdated;

    /**
     * Constructor of this Elevator. Automatically checks for updates on creation.
     *
     * @param   plugin
     *          The plugin. Used to get the current version.
     *
     * @param   repo
     *          The repo name including the author, e.g. Efnilite/vilib
     *
     * @param   downloadIfOutdated
     *          Whether the Elevator should download a new version if the current is outdated.
     */
    public GitElevator(ViPlugin plugin, String repo, boolean downloadIfOutdated) {
        this.plugin = plugin;
        this.repo = repo;
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
    public GitElevator comparator(VersionComparator comparator) {
        this.comparator = comparator;
        return this;
    }

    /**
     * Checks if this plugin version is outdated. Automatically called on instantiation.
     */
    public void check() {
        Task.create(ViMain.getPlugin())
                .async()
                .execute(() -> {
                    try {
                        String url = "https://api.github.com/repos/" + repo + "/releases/latest";

                        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setRequestProperty("accept", "application/json");

                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
                        newerVersion = object.get("tag_name").getAsString();
                        downloadUrl = object.get("browser_download_url").getAsString();

                        reader.close();

                        outdated = !comparator.isLatest(plugin.getDescription().getVersion(), newerVersion);

                        if (outdated) {
                            ViMain.logging().info("A new version of " + plugin.getName() + " is available!");
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
                        ViMain.logging().info("A server restart is required for this download to work!");
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
