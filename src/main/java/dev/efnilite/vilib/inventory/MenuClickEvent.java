package dev.efnilite.vilib.inventory;

import dev.efnilite.vilib.inventory.item.MenuItem;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Container class for clicking on menus
 */
public class MenuClickEvent {

    private final int slot;
    private final Menu menu;
    private final MenuItem item;
    private final InventoryClickEvent event;

    public MenuClickEvent(int slot, Menu menu, MenuItem item, InventoryClickEvent event) {
        this.slot = slot;
        this.menu = menu;
        this.item = item;
        this.event = event;
    }

    public int getSlot() {
        return slot;
    }

    public Menu getMenu() {
        return menu;
    }

    public MenuItem getItem() {
        return item;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }
}