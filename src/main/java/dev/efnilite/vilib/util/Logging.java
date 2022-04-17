package dev.efnilite.vilib.util;

import dev.efnilite.vilib.ViPlugin;
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

    public static void stack(String error, String fix, Throwable... optionals) {
        error("##");
        error("## " + ViPlugin.getViPlugin().getName() + " has encountered a critical error!");
        error("## " + error);
        error("##");

        if (optionals != null) {
            Throwable throwable = optionals[0];
            error("## Stack trace:");
            error("## " + throwable);
            StackTraceElement[] stack = throwable.getStackTrace();
            for (StackTraceElement stackTraceElement : stack) {
                error("## \t" + stackTraceElement.toString());
            }
        } else {
            error("## No stack trace provided");
        }

        error("##");
        error("## This is an internal error which you may be able to fix.");
        error("## How to fix: " + fix);
        error("## Unable to solve this problem using the fix? Visit the Discord or GitHub.");
        error("## Be sure to send this entire error.");
        error("##");
        error("## Version information:");
        error("##\tBuild Version: " + ViPlugin.getViPlugin().getDescription().getVersion());
        error("##\tMinecraft: " + Version.getVersion().name().replaceAll("_", "."));
        error("##");
    }

    public static void verbose(String message) {
        if (ViPlugin.vebosing()) {
            logger.info("(Verbose) " + message);
        }
    }
}
