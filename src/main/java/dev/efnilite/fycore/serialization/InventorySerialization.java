package dev.efnilite.fycore.serialization;

import dev.efnilite.fycore.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class InventorySerialization {

    public static PlayerInventory deserialize64(Map<Integer, String> map) {
        PlayerInventory inventory = new PlayerInventory();
        for (int slot : map.keySet()) {
            inventory.setItem(slot, ItemSerializer.deserialize64(map.get(slot)));
        }
        return inventory;
    }

    public static Map<Integer, String> serialize64(PlayerInventory inventory) {
        Map<Integer, String> result = new HashMap<>();
        for (int slot : inventory.getItems().keySet()) {
            result.put(slot, ItemSerializer.serialize64(inventory.getSlot(slot)));
        }
        return result;
    }
}
