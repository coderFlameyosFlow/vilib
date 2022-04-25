package dev.efnilite.vilib.util;

import dev.efnilite.vilib.ViPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * Adds useful data for later (e.g. game testing)
 */
public class Logging {

    private final Logger logger;
    private final ViPlugin plugin;

    public Logging(ViPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public void info(String info) {
        logger.info(info);
    }

    public void warn(String warn) {
        logger.warning(warn);
    }

    public void error(String error) {
        logger.severe(error);
    }

    public void stack(String error, String fix) {
        stack(error, fix, null);
    }

    public void stack(String error, Throwable throwable) {
        stack(error, null, throwable);
    }

    public void stack(String error, @Nullable String fix, @Nullable Throwable throwable) {
        error("##");
        error("## " + plugin.getName() + " has encountered a critical error!");
        error("## " + error);
        error("##");

        if (throwable == null) {
            error("## No stack trace provided");
        } else {
            error("## Stack trace:");
            error("## " + throwable);
            StackTraceElement[] stack = throwable.getStackTrace();
            for (StackTraceElement stackTraceElement : stack) {
                error("## \t" + stackTraceElement.toString());
            }
        }

        error("##");
        if (fix == null) {
            error("## This is probably not your fault.");
            error("## Contact the developer to fix it.");
            error("## Be sure to send the entire error while reporting.");
        } else {
            error("## This is probably not your fault, but you may be able to fix it.");
            error("## You should try: " + fix);
            error("## Contact the developer if this doesn't work.");
            error("## Be sure to send the entire error while reporting.");
        }
        error("##");
        error("## Version information:");
        error("##\tBuild Version: " + plugin.getDescription().getVersion());
        error("##\tMinecraft: " + Version.getVersion().name().replaceAll("_", "."));
        error("##");
    }
}
