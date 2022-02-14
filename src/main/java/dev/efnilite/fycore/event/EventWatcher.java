package dev.efnilite.fycore.event;

import dev.efnilite.fycore.FyPlugin;
import dev.efnilite.fycore.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Wrapper for Listener, adding useful methods.
 *
 * @author Efnilite
 */
public interface EventWatcher extends Listener {

    default void register() {
        FyPlugin.getFyPlugin().registerListener(this);
    }
}