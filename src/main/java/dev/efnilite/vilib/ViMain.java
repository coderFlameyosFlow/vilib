package dev.efnilite.vilib;

import dev.efnilite.vilib.util.Logging;
import dev.efnilite.vilib.util.Version;
import dev.efnilite.vilib.util.elevator.GitElevator;
import dev.efnilite.vilib.util.elevator.VersionComparator;
import org.bstats.bukkit.Metrics;

public class ViMain extends ViPlugin {

    private static ViMain instance;

    @Override
    public void enable() {
        instance = this;
        logging = new Logging(this);

        Configuration configuration = new Configuration(this);

        if (Version.isHigherOrEqual(Version.V1_13)) { // no gson under 1.13
            new GitElevator("ViStudios/vilib", this, VersionComparator.FROM_SEMANTIC,
                    configuration.getFile("config").getBoolean("auto-updater"));
        }

        new Metrics(this, 15090);
    }

    @Override
    public void disable() {

    }

    /**
     * Returns the {@link Logging} belonging to this plugin.
     *
     * @return this plugin's {@link Logging} instance.
     */
    public static Logging logging() {
        return getPlugin().logging;
    }

    /**
     * Returns this plugin instance.
     *
     * @return the plugin instance.
     */
    public static ViMain getPlugin() {
        return instance;
    }
}
