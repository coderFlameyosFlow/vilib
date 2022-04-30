package dev.efnilite.vilib;

import dev.efnilite.vilib.util.Logging;
import dev.efnilite.vilib.util.elevator.GitElevator;
import dev.efnilite.vilib.util.elevator.VersionComparator;

public class ViMain extends ViPlugin {

    private static ViMain instance;
    private static GitElevator elevator;

    @Override
    public void enable() {
        instance = this;

        elevator = new GitElevator(this, "ViStudios/vilib", true)
                .comparator(VersionComparator.FROM_SEMANTIC);
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
