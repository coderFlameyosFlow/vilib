package dev.efnilite.fycore.util;

import dev.efnilite.fycore.FyPlugin;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

/**
 * Adds useful data for later (e.g. game testing)
 */
public class Logging {

    private static Logger logger;

    public static void init(Plugin plugin) {
        logger = plugin.getLogger();
    }

    public static void info(String info) {
        logger.info(info);
    }

    public static void warn(String warn) {
        logger.warning(warn);
    }

    public static void error(String error) {
        logger.severe(error);
    }

    public static void stack(String error, String fix) {
        error("##");
        error("## " + FyPlugin.getFyPlugin().getName() + " has encountered a critical error!");
        error("## " + error);
        error("##");
        error("## This is an internal error which may be able to fix.");
        error("## How to fix: " + fix);
        error("## Unable to solve this problem using the fix? Visit the Discord or GitHub.");
        error("##");
        error("## Version information:");
        error("##\tBuild Version: " + FyPlugin.getFyPlugin().getDescription().getVersion());
        error("##\tMinecraft: " + Version.getVersion().name().replaceAll("_", "."));
        error("##");
    }
}
