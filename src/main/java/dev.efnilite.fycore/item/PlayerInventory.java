package dev.efnilite.fycore.item;

import com.google.gson.annotations.Expose;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerInventory {

    private final Map<Integer, ItemStack> items;
    @Expose
    private Map<Integer, Map<String, Object>> serializedItems = new HashMap<>();

    public PlayerInventory() {
        items = new HashMap<>();
    }

    public PlayerInventory(HashMap<Integer, ItemStack> items) {
        this.items = items;
    }

    public PlayerInventory setItem(int slot, ItemStack item) {
        items.put(slot, item);
        return this;
    }

    public ItemStack getSlot(int slot) {
        return items.get(slot);
    }

    public PlayerInventory setSerializedItem(int slot, Map<String, Object> item) {
        serializedItems.put(slot, item);
        return this;
    }

    public Map<Integer, ItemStack> getItems() {
        return items;
    }
}
