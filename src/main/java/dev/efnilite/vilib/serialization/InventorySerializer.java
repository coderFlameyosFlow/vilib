package dev.efnilite.vilib.serialization;

import java.util.HashMap;
import java.util.Map;

public class InventorySerializer {

    public static PlayerInventory deserialize64(Map<Integer, String> map) {
        PlayerInventory inventory = new PlayerInventory();
        for (int slot : map.keySet()) {
            inventory.add(slot, ItemSerializer.deserialize64(map.get(slot)));
        }
        return inventory;
    }

    public static Map<Integer, String> serialize64(PlayerInventory inventory) {
        Map<Integer, String> result = new HashMap<>();
        for (int slot : inventory.getItems().keySet()) {
            result.put(slot, ItemSerializer.serialize64(inventory.get(slot)));
        }
        return result;
    }
}
