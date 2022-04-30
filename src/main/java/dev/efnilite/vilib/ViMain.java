package dev.efnilite.vilib;

import dev.efnilite.vilib.util.Logging;
import dev.efnilite.vilib.util.elevator.Elevator;
import dev.efnilite.vilib.util.elevator.VersionComparator;
import dev.efnilite.vilib.util.elevator.VersionRetriever;

public class ViMain extends ViPlugin {

    private static ViMain instance;
    private static Elevator elevator;

    @Override
    public void enable() {
        instance = this;

        elevator = new Elevator(this,
                "https://raw.githubusercontent.com/ViStudios/vilib/master/src/main/resources/plugin.yml",
                "https://github.com/ViStudios/vilib/releases/download/v1.0.0-test/vilib-1.0.0.jar", true)
                .comparator(VersionComparator.FROM_SEMANTIC)
                .retriever(VersionRetriever.GITHUB);

        // todo update checker
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
