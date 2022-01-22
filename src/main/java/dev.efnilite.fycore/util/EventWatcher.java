package dev.efnilite.fycore.util;

import dev.efnilite.fycore.FyPlugin;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Wrapper for Listener, adding useful methods.
 *
 * @author Efnilite
 */
public interface EventWatcher extends Listener {

    default void unregister() {
        HandlerList.unregisterAll(this);
    }

    default void register() {
        FyPlugin.getFyPlugin().registerListener(this);
    }
}