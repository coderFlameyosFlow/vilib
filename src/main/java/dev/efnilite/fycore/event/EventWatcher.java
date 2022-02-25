package dev.efnilite.fycore.event;

import dev.efnilite.fycore.FyPlugin;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Wrapper for Listener, adding useful methods.
 *
 * @author Efnilite
 */
public interface EventWatcher extends Listener {

    /**
     * Unregisters every listener in this class
     */
    default void unregisterAll() {
        HandlerList.unregisterAll(this);
    }

    /**
     * Registers this listener
     */
    default void register() {
        FyPlugin.getFyPlugin().registerListener(this);
    }
}