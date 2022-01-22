package dev.efnilite.fycore.serialization;

import dev.efnilite.fycore.item.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class InventorySerialization {

    public static PlayerInventory deserializeGson(Map<Integer, Map<String, Object>> map) {
        PlayerInventory inventory = new PlayerInventory();
        for (Integer slot : map.keySet()) {
            inventory.setItem(slot, ItemSerializer.deserializeGson(map.get(slot)));
        }
        return inventory;
    }

    public static Map<Integer, Map<String, Object>> serializeGson(PlayerInventory inventory) {
        Map<Integer, Map<String, Object>> result = new HashMap<>();
        for (Integer slot : inventory.getItems().keySet()) {
            result.put(slot, ItemSerializer.serializeGson(inventory.getSlot(slot)));
        }
        return result;
    }
}
