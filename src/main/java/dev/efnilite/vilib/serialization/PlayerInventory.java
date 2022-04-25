package dev.efnilite.vilib.serialization;

import com.google.gson.annotations.Expose;
import dev.efnilite.vilib.ViMain;
import dev.efnilite.vilib.ViPlugin;
import dev.efnilite.vilib.util.Task;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Wrapper class for player inventories, making it easier to handle.
 */
public class PlayerInventory {

    private final Map<Integer, ItemStack> items = new HashMap<>();

    @Expose
    private final Map<Integer, String> serialized = new HashMap<>();

    /**
     * Empty constructor to allow deserializing
     */
    public PlayerInventory() {

    }

    /**
     * Constructor. Saves the player's items.
     *
     * @param   player
     *          The player
     */
    public PlayerInventory(Player player) {
        Inventory inventory = player.getInventory();

        int slot = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                items.put(slot, item);
            }
            slot++;
        }
    }

    /**
     * Gets a new instance of a PlayerInventory
     *
     * @param   player
     *          The player
     *
     * @return the PlayerInventory containing all the player's items
     */
    public static PlayerInventory of(Player player) {
        return new PlayerInventory(player);
    }

    /**
     * Saves this instance of a PlayerInventory to a file by serializing every item using {@link InventorySerializer#serialize64(PlayerInventory)}
     *
     * @param   file
     *          The file to save it to
     *
     * @param   inventory
     *          The inventory
     *
     * @param   onComplete
     *          What to do on complete. Can be null.
     */
    public static void save(File file, PlayerInventory inventory, @Nullable Runnable onComplete) {
        Task.create(ViMain.getPlugin())
                .async()
                .execute(() -> {
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }

                        inventory.serialized.clear();
                        inventory.serialized.putAll(InventorySerializer.serialize64(inventory));

                        FileWriter writer = new FileWriter(file);
                        ViPlugin.getGson().toJson(inventory, writer);

                        if (onComplete != null) {
                            onComplete.run();
                        }

                        writer.flush();
                        writer.close();
                    } catch (IOException ex) {
                        ViMain.logging().stack("Error while saving inventory", ex);
                    }
                })
                .run();
    }

    /**
     * Reads a file
     *
     * @param   file
     *          The file
     *
     * @param   onRead
     *          Consumer containing the gathered PlayerInventory. Can be null.
     */
    public static void read(File file, @Nullable Consumer<@Nullable PlayerInventory> onRead) {
        Task.create(ViMain.getPlugin())
                .async()
                .execute(() -> {
                    try {
                        if (!file.exists()) {
                            if (onRead != null) {
                                onRead.accept(null);
                            }
                            return;
                        }
                        FileReader reader = new FileReader(file);
                        PlayerInventory inventory = ViPlugin.getGson().fromJson(reader, PlayerInventory.class);

                        if (onRead != null) {
                            onRead.accept(InventorySerializer.deserialize64(inventory.serialized));
                        }

                        reader.close();
                    } catch (IOException ex) {
                        ViMain.logging().stack("Error while reading inventory", ex);
                        if (onRead != null) {
                            onRead.accept(null);
                        }
                    }
                })
                .run();
    }

    /**
     * Applies contents of this PlayerInventory to the player
     *
     * @param   player
     *          The player
     */
    public void apply(Player player) {
        Inventory inventory = player.getInventory();
        inventory.clear();

        for (int slot : items.keySet()) {
            inventory.setItem(slot, items.get(slot));
        }
    }

    /**
     * Adds an item to a slot
     *
     * @param   slot
     *          The slot
     *
     * @param   item
     *          The item
     */
    public void add(int slot, ItemStack item) {
        items.put(slot, item);
    }

    /**
     * Gets an item from a slot
     *
     * @param   slot
     *          The slot
     *
     * @return the item, null if there isnt one
     */
    @Nullable
    public ItemStack get(int slot) {
        return items.get(slot);
    }

    public Map<Integer, ItemStack> getItems() {
        return items;
    }
}