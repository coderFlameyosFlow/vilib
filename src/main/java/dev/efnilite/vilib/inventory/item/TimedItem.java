package dev.efnilite.vilib.inventory.item;

import dev.efnilite.vilib.ViMain;
import dev.efnilite.vilib.inventory.Menu;
import dev.efnilite.vilib.inventory.MenuClickEvent;
import dev.efnilite.vilib.util.Task;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A MenuItem which only stays for a certain amount of ticks. This item is meant as a confirm option or to display items for a small amount of time.
 * On click of this item it will automatically go back to the previous item.
 */
public class TimedItem extends MenuItem {

    private int timeStay;
    private Task task;
    private MenuItem revertTo;
    private final Player player;
    private final MenuItem item;
    private final MenuClickEvent event;

    public TimedItem(MenuItem item, MenuClickEvent event) {
        this.item = item;
        this.revertTo = event.getMenu().getItem(event.getSlot());
        if (revertTo == null) {
            revertTo = new Item(Material.AIR, "<red> ");
        }
        this.player = event.getPlayer();
        this.event = event;
    }

    /**
     * Sets the amount of ticks this item will stay in view for.
     *
     * @param   ticks
     *          The amount of ticks
     *
     * @return the instance of the class
     */
    public TimedItem stay(int ticks) {
        this.timeStay = ticks;
        return this;
    }

    @Override
    public ItemStack build() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Menu menu = event.getMenu();
                InventoryView view = player.getOpenInventory();
                if (view.getTitle().equals(menu.getTitle())) {
                    menu.item(event.getSlot(), revertTo);
                    menu.updateItem(event.getSlot());
                } else {
                    cancel(); // prevent going on forever
                }
            }
        };

        task = Task.create(ViMain.getPlugin())
                .delay(timeStay)
                .execute(runnable);
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