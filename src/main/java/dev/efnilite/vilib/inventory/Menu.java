package dev.efnilite.vilib.inventory;

import dev.efnilite.vilib.ViMain;
import dev.efnilite.vilib.event.EventWatcher;
import dev.efnilite.vilib.inventory.animation.MenuAnimation;
import dev.efnilite.vilib.inventory.item.Item;
import dev.efnilite.vilib.inventory.item.MenuItem;
import dev.efnilite.vilib.util.Numbers;
import dev.efnilite.vilib.util.Strings;
import dev.efnilite.vilib.util.Task;
import dev.efnilite.vilib.util.collections.ViList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Class for Menu handling
 *
 * @author Efnilite
 */
@SuppressWarnings("unused")
public class Menu implements EventWatcher {

    protected boolean deactivated = false;
    
    protected UUID inventoryId;
    protected Player player;
    protected Material filler = null;
    protected MenuAnimation animation = null;
    
    protected final int rows;
    protected final String title;
    protected final Map<Integer, MenuItem> items = new HashMap<>();
    protected final List<Integer> evenlyDistributedRows = new ArrayList<>();

    private final static Map<UUID, UUID> openMenus = new HashMap<>();
    private final static List<Menu> disabledMenus = new ArrayList<>();

    /*
     * Prevents big boy memory usage by unregistering unused menus
     */
    static {
        Task.create(ViMain.getPlugin())
            .repeat(5 * 20)
            .execute(() -> {
                if (openMenus.keySet().size() == 0) {
                    for (Menu menu : disabledMenus) {
                        menu.unregisterAll();
                    }
                    disabledMenus.clear();
                }
            }).run();
    }

    public Menu(int rows, String name) {
        if (rows < 1 || rows > 6) {
            throw new IllegalArgumentException("Rows is below 1 or above 6");
        }
        this.rows = rows;
        this.title = Strings.colour(name);
        this.inventoryId = UUID.randomUUID();
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
        if (slot > rows * 9 || slot < 0) {
            throw new IllegalArgumentException("Slot " + slot + " is not in inventory");
        }

        items.put(slot, item);
        return this;
    }

    /**
     * Sets a specific set of rows to be distributed evenly. The items will be distributed.
     * Starts from 0 and goes up to 5.
     *
     * @param   rows
     *          The rows
     *
     * @return the instance of this class
     */
    public Menu distributeRowEvenly(int... rows) {
        for (int row : rows) {
            if (row < 0 || row > 5) {
                throw new IllegalArgumentException("Row must be above 0 and below 6!");
            }
            evenlyDistributedRows.add(row);
        }
        return this;
    }

    /**
     * Will distribute all rows evenly.
     * @see #distributeRowEvenly(int...)
     *
     * @return the instance of this class
     */
    public Menu distributeRowsEvenly() {
        evenlyDistributedRows.addAll(Numbers.getFromZero(rows));
        return this;
    }

    /**
     * Fills the background with a specific item
     *
     * @param   filler
     *          The background filler
     *
     * @return the instance of this class
     */
    public Menu fillBackground(@NotNull Material filler) {
        this.filler = filler;
        return this;
    }

    /**
     * Creates an animation on opening
     *
     * @param   animation
     *          The animation
     *
     * @return the instance of this class
     */
    public Menu animation(@NotNull MenuAnimation animation) {
        this.animation = animation;
        this.animation.init(rows);
        return this;
    }

    /**
     * Updates a specific item
     *
     * @param   slots
     *          The slots which are to be updated
     */
    public void updateItem(int... slots) {
        Inventory inventory = player.getOpenInventory().getTopInventory();

        if (inventory.getSize() % 9 != 0) return;

        for (int slot : slots) {
            inventory.setItem(slot, items.get(slot).build());
        }
    }

    /**
     * Updates all items in the inventory
     */
    public void update() {
        Inventory inventory = player.getOpenInventory().getTopInventory();

        if (inventory.getSize() % 9 != 0) return;

        for (int slot : items.keySet()) {
            inventory.setItem(slot, items.get(slot).build());
        }
    }

    /**
     * Opens the menu for the player. This distributes items on the same row automatically if these rows are assigned to automatically distribute.
     *
     * @param   player
     *          The player to open it to
     */
    public void open(Player player) {
        this.player = player;
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title);
        
        
        // Evenly distributed rows
        for (int row : evenlyDistributedRows) {
            int min = row * 9; // 0 * 9 = 0
            int max = min + 8; // 0 + 8 = slot 8
            Map<Integer, MenuItem> itemsInRow = new HashMap<>();

            for (int slot : items.keySet()) { // get all items in the specified row
                if (slot >= min && slot <= max) {
                    itemsInRow.put(slot, items.get(slot));
                }
            }

            
            
            if (itemsInRow.keySet().isEmpty()) return;
            
            List<Integer> sortedSlots = new ViList<>(itemsInRow.keySet()).sort().toList(); // sort all slots
            List<Integer> slots = getEvenlyDistributedSlots(sortedSlots.size()); // evenly distribute items
            List<Integer> olds = new ArrayList<>();
            List<Integer> news = new ArrayList<>();

            for (int i = 0; i < slots.size(); i++) {
                int newSlot = slots.get(i) + (9 * row); // gets the new slot
                int oldSlot = sortedSlots.get(i); // the previous slot
                MenuItem item = itemsInRow.get(oldSlot); // the item in the previous slot

                news.add(newSlot);
                olds.add(oldSlot);
                items.put(newSlot, item); // put item in new slot
            }

            for (int oldSlot : olds) {
                if (news.contains(oldSlot)) continue; 
                items.remove(oldSlot); // remove items from previous slot without deleting ones that are to-be moved
            }
        }

        // Filler
        if (filler == null) return;
        
        Item fillerItem = new Item(filler, "<red> "); // fill the background with the same material
        for (int slot = 0; slot < rows * 9; slot++) {
            if (items.get(slot) != null) { // ignore already-set items
                continue;
            }

            items.put(slot, fillerItem);
        }

        player.openInventory(inventory);

        // Set items
        if (animation == null) {
            for (int slot : items.keySet()) { // no animation means just setting it normally
                inventory.setItem(slot, items.get(slot).build());
            }
        } else {
            animation.run(this);
        }

        openMenus.put(player.getUniqueId(), inventoryId);
        register();
    }

    @EventHandler
    public void click(@NotNull InventoryClickEvent event) {
        if (deactivated || event.getClickedInventory() != event.getView().getTopInventory()) return;

        UUID id = openMenus.get(event.getWhoClicked().getUniqueId());
        if (id != inventoryId) return;

        MenuItem clickedItem = items.get(event.getSlot());
        if (clickedItem == null) return;
        
        event.setCancelled(!clickedItem.isMovable());

        clickedItem.handleClick(this, event, event.getClick());
    }

    @EventHandler
    public void close(InventoryCloseEvent event) {
        if (deactivated) return;

        UUID viewerId = event.getPlayer().getUniqueId();
        UUID id = openMenus.get(viewerId);
        if (id != inventoryId) return;

        if (animation != null) {
            animation.stop();
        }

        deactivated = true;
        disabledMenus.add(this);
        openMenus.remove(viewerId);
    }

    /**
     * Returns the item in the respective slot.
     *
     * @param   slot
     *          The slot
     *
     * @return the item in this slot. This may be null.
     */
    public @Nullable MenuItem getItem(int slot) {
        return items.get(slot);
    }

    /**
     * Gets the slots and their respective items
     *
     * @return a Map with the slots and items.
     */
    public Map<Integer, MenuItem> getItems() {
        return items;
    }

    /**
     * Gets the player
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    public String getTitle() {
        return title;
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
}
