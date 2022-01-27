package dev.efnilite.fycore.inventory.item;

import dev.efnilite.fycore.inventory.Menu;
import dev.efnilite.fycore.util.Task;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class TimedItem extends MenuItem {

    private int timeStay;
    private int previousSlot;
    private MenuItem revertTo;
    private Player player;
    private Menu menu;
    private MenuItem item;

    public TimedItem(MenuItem item, MenuItem revertTo, Player player, Menu menu, int timeStay, int previousSlot) {
        this.item = item;
        this.timeStay = timeStay;
        this.previousSlot = previousSlot;
        this.revertTo = revertTo;
        this.player = player;
        this.menu = menu;
    }

    @Override
    public ItemStack build() {
        new Task()
                .delay(timeStay)
                .execute(() -> {
                    InventoryView view = player.getOpenInventory();
                    if (view.getTitle().equals(menu.getTitle())) {
                        menu.item(previousSlot, revertTo);
                        menu.update();
                    }
                })
                .run();
        return item.build();
    }

    @Override
    public boolean isMovable() {
        return false;
    }
}
