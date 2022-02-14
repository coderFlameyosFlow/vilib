package dev.efnilite.fycore.event;

import dev.efnilite.fycore.FyPlugin;
import dev.efnilite.fycore.reflection.Reflection;
import dev.efnilite.fycore.util.Logging;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.Method;

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