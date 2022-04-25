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

    public static Logging logging() {
        return getPlugin().logging;
    }

    public static ViMain getPlugin() {
        return instance;
    }
}
