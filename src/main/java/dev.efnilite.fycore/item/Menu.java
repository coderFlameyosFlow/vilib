package dev.efnilite.fycore.item;

import dev.efnilite.fycore.event.EventWatcher;
import dev.efnilite.fycore.util.FyList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class for Menu handling
 *
 * @author Efnilite
 */
public class Menu implements EventWatcher {

    private final int rows;
    private final String title;
    private final List<UUID> watchers;
    private final Map<Integer, MenuItem> items;
    private final List<Integer> evenlyDistributedRows;

    public Menu(int rows, String name) {
        this.rows = rows;
        this.title = name;
        this.watchers = new ArrayList<>();
        this.items = new HashMap<>();
        this.evenlyDistributedRows = new ArrayList<>();
    }

    /**
     * Sets an item to a slot
     *
     * @param   slot
     *          The slot
     *
     * @param   item
     *          The item
     *
     * @return the instance of this class
     */
    public Menu item(int slot, MenuItem item) {
        items.put(slot, item);
        return this;
    }

    /**
     * Sets a specific row to be distributed evenly. The items will be distributed.
     *
     * @param   row
     *          The row
     *
     * @return the instance of this class
     */
    public Menu distributeRowEvenly(int row) {
        evenlyDistributedRows.add(row);
        return this;
    }

    /**
     * Will distribute all rows evenly
     * @see #distributeRowEvenly(int)
     *
     * @return the instance of this class
     */
    public Menu distributeRowsEvenly() {
        evenlyDistributedRows.addAll(getFromZero(rows));
        return this;
    }

    /**
     * Opens the menu for the player. This distributes items on the same row automatically if these rows are assigned to automatically distribute.
     *
     * @param   player
     *          The player to open it to
     *
     * @return the instance of this class
     */
    public Menu open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title);

        for (int row : evenlyDistributedRows) {
            int max = row * 9 - 1; // 1 * 9 - 1 = slot 8
            int min = (row - 1) * 9; // (1 - 1) * 9 = 0
            Map<Integer, MenuItem> itemsInRow = new HashMap<>();

            for (int slot : items.keySet()) { // get all items in the specified row
                if (slot >= min && slot <= max) {
                    itemsInRow.put(slot, items.get(slot));
                }
            }

            if (itemsInRow.keySet().size() > 0) {
                List<Integer> sortedSlots = FyList.sort(itemsInRow.keySet()); // sort all slots
                List<Integer> slots = getEvenlyDistributedSlots(sortedSlots.size()); // evenly distribute items

                for (int i = 0; i < slots.size(); i++) {
                    int newSlot = slots.get(i) + 9 * row; // gets the new slot
                    int oldSlot = sortedSlots.get(i); // the previous slot
                    MenuItem item = itemsInRow.get(oldSlot); // the item in the previous slot

                    items.remove(oldSlot); // remove item from previous slot
                    items.put(newSlot, item); // put item in new slot
                }
            }
        }
        for (int slot : items.keySet()) {
            inventory.setItem(slot, items.get(slot).build());
        }

        watchers.add(player.getUniqueId());

        player.openInventory(inventory);
        register();
        return this;
    }

    @EventHandler
    public void click(@NotNull InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        InventoryView view = event.getView();
        int slot = event.getSlot();

        if (!view.getTitle().equals(title) && !watchers.contains(player.getUniqueId()) && event.getCurrentItem() != null) {
            return;
        }

        MenuItem clickedItem = items.get(slot);
        if (clickedItem == null) {
            return;
        }
        event.setCancelled(!clickedItem.isMovable());

        clickedItem.handleClick(event.getCurrentItem(), event, event.getClick());
    }

    @EventHandler
    public void close(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        watchers.remove(player.getUniqueId());
    }

    private List<Integer> getEvenlyDistributedSlots(int amountInRow) {
        switch (amountInRow) {
            case 0:
                return Collections.emptyList();
            case 1:
                return Collections.singletonList(4);
            case 2:
                return Arrays.asList(3, 5);
            case 3:
                return Arrays.asList(3, 4, 5);
            case 4:
                return Arrays.asList(2, 3, 5, 6);
            case 5:
                return Arrays.asList(2, 3, 4, 5, 6);
            case 6:
                return Arrays.asList(1, 2, 3, 5, 6, 7);
            case 7:
                return Arrays.asList(1, 2, 3, 4, 5, 6, 7);
            case 8:
                return Arrays.asList(0, 1, 2, 3, 5, 6, 7, 8);
            default:
                return Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        }
    }

    // gets all ints from zero (inclusive) to n (inclusive)
    private List<Integer> getFromZero(int n) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            result.add(n);
        }
        return result;
    }
}