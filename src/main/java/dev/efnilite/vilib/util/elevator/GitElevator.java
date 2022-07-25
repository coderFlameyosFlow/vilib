package dev.efnilite.vilib.util.elevator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.efnilite.vilib.ViPlugin;
import dev.efnilite.vilib.util.Task;
import dev.efnilite.vilib.util.Time;
import dev.efnilite.vilib.util.Version;
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
    private String downloadUrl;
    private final VersionComparator comparator;
    private final String repo;
    private final ViPlugin plugin;
    private final boolean downloadIfOutdated;

    /**
     * The interval between update checks. By default 8 hours.
     */
    public static final int CHECK_INTERVAL = Time.SECONDS_PER_HOUR * 8 * 20;

    /**
     * Constructor of this Elevator. Automatically checks for updates on creation.
     *
     * @param   plugin
     *          The plugin. Used to get the current version.
     *
     * @param   comparator
     *          The comparator type used for version checking.
     *
     * @param   repo
     *          The repo name including the author, e.g. Efnilite/vilib
     *
     * @param   downloadIfOutdated
     *          Whether the Elevator should download a new version if the current is outdated.
     */
    public GitElevator(String repo, ViPlugin plugin, VersionComparator comparator, boolean downloadIfOutdated) {
        this.plugin = plugin;
        this.repo = repo;
        this.comparator = comparator;
        this.downloadIfOutdated = downloadIfOutdated;
    }

    /**
     * Checks if this plugin version is outdated.
     */
    @SuppressWarnings("deprecation") // <1.13
    public void check() {
        try {
            String url = "https://api.github.com/repos/" + repo + "/releases/latest";

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            JsonObject object = new JsonParser().parse(reader).getAsJsonObject();
            String newerVersion = object.get("tag_name").getAsString();
            downloadUrl = object.getAsJsonArray("assets").get(0) // for some reason assets is an array with one element
                    .getAsJsonObject().get("browser_download_url").getAsString(); // get browser_download_url

            reader.close();

            outdated = !comparator.isLatest(newerVersion, plugin.getDescription().getVersion());
            outdated = true;

            if (outdated) {
                plugin.getLogger().info("A new version of is available!");
                plugin.getLogger().info("Restarting the server will apply the changes.");
            }
        } catch (Throwable throwable) {
            plugin.getLogger().severe("There was an error while checking the latest version");
        }
    }

    /**
     * Elevates the current plugin build.
     *
     * Warning: using this on start-up will result in many errors in class-loading semantics, etc.
     * Advised to be used on disable.
     *
     * @param   async
     *          Whether this task should be run async. Warning: when this is used on disable, async tasks can't be registered. Use true in this case.
     */
    public void elevate(boolean async) {
        if (async) {
            Task.create(plugin)
                    .async()
                    .execute(this::_update)
                    .run();
        } else {
            _update();
        }
    }

    private void _update() {
        System.out.println("update call");
        try {
            if (!outdated) {
                return;
            }
            File jar = getJar();
            if (jar == null) {
                System.out.println("jar null");
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

            outdated = false;

            plugin.getLogger().info("A new version of " + plugin.getName() + " has been downloaded.");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            plugin.getLogger().severe("There was an error while updating to the latest version");
        }
    }

    private File getJar() {
        try {
            Method method = JavaPlugin.class.getDeclaredMethod("getFile");
            method.setAccessible(true);
            return (File) method.invoke(this.plugin);
        } catch (ReflectiveOperationException ex) {
            plugin.getLogger().severe("Failed to get plugin jar name: " + ex.getMessage());
            return null;
        }
    }

    public boolean isOutdated() {
        return outdated;
    }

    public boolean shouldDownloadIfOutdated() {
        return downloadIfOutdated;
    }
}
