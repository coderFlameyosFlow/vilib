package dev.efnilite.vilib;

import dev.efnilite.vilib.util.Logging;

public class ViMain extends ViPlugin {

    private static ViMain instance;

    @Override
    public void enable() {
        instance = this;

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
