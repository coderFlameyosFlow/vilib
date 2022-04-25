package dev.efnilite.vilib.event;

import dev.efnilite.vilib.ViMain;
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
        ViMain.getPlugin().registerListener(this);
    }
}