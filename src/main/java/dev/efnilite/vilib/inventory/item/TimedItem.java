package dev.efnilite.vilib.inventory.item;

import dev.efnilite.vilib.inventory.Menu;
import dev.efnilite.vilib.inventory.MenuClickEvent;
import dev.efnilite.vilib.util.Task;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

/**
 * A MenuItem which only stays for a certain amount of ticks. This item is meant as a confirm option or to display items for a small amount of time.
 * On click of this item it will automatically go back to the previous item.
 */
public class TimedItem extends MenuItem {

    private Task task;
    private MenuItem revertTo;
    private final int timeStay;
    private final int slot;
    private final Player player;
    private final Menu menu;
    private final MenuItem item;

    public TimedItem(MenuItem item, MenuClickEvent event, int timeStay) {
        this.item = item;
        this.timeStay = timeStay;
        this.slot = event.getSlot();
        this.revertTo = event.getMenu().getItem(slot);
        if (revertTo == null) {
            revertTo = new Item(Material.AIR, "&c");
        }
        this.player = (Player) event.getEvent().getWhoClicked();
        this.menu = event.getMenu();
    }

    @Override
    public ItemStack build() {
        task = new Task()
                .delay(timeStay)
                .execute(() -> {
                    InventoryView view = player.getOpenInventory();
                    if (view.getTitle().equals(menu.getTitle())) {
                        menu.item(slot, revertTo);
                        menu.updateItem(slot);
                    }
                });
        task.run();
        return item.build();
    }

    @Override
    public void handleClick(Menu menu, InventoryClickEvent event, ClickType clickType) {
        item.handleClick(menu, event, clickType);

        task.cancelAndRunImmediately();
    }

    @Override
    public boolean isMovable() {
        return false;
    }
}