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
     * Unregisters every event in this listener.
     *
     * @author krisrok, source https://bukkit.org/threads/unregistering-events.21568/
     */
    default void unregister() {
        Logging.info(this.getClass().toString());
        try {
            for (Method method : getClass().getMethods()) {
                if (method.getAnnotation(EventHandler.class) != null) {
                    unregisterEvent((Class<? extends Event>) method.getParameterTypes()[0]);
                }

            }
        } catch (Throwable ignored) {
        }
    }

    /**
     * Unregisters a specific event
     *
     * @param   event
     *          The event
     *
     * @author krisrok, source https://bukkit.org/threads/unregistering-events.21568/
     */
    default void unregisterEvent(Class<? extends Event> event) {
        try {
            for (RegisteredListener registered : HandlerList.getRegisteredListeners(FyPlugin.getFyPlugin())) {
                if (registered.getListener() == this) {
                    ((HandlerList) event.getMethod("getHandlerList").invoke(null)).unregister(registered);
                }
            }
        } catch (Throwable ignored) {
        }
    }

    /**
     * Registers this listener
     */
    default void register() {
        FyPlugin.getFyPlugin().registerListener(this);
    }
}