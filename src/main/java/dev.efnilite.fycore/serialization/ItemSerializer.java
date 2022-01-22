package dev.efnilite.fycore.serialization;

import dev.efnilite.fycore.util.Logging;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for basic ItemStack serialization.
 * This uses a custom method for saving, so it is not compatible with ItemStack#serialize/ItemStack#deserialize.
 * These methods are very lacking and outdated in comparison.
 */
public class ItemSerializer {

    /**
     * Serializes an ItemStack into a Map
     *
     * @param   item
     *          The item
     *
     * @return a Map with things like the name, lore, etc.
     */
    public static Map<String, Object> serializeGson(ItemStack item) {
        Map<String, Object> result = new HashMap<>();

        ItemMeta meta = item.getItemMeta();

        Map<String, Integer> serEnchants = new HashMap<>();  // SOMEHOW ItemStack#serialize doesn't support enchantments
        Map<Enchantment, Integer> enchants = item.getEnchantments();
        for (Enchantment enchantment : enchants.keySet()) {
            serEnchants.put(enchantment.getKey().toString(), enchants.get(enchantment)); // ItemStack#deserialize uses name value instead of key
        }

        result.put("material", item.getType().name());
        result.put("amount", item.getAmount());

        if (meta != null && !Bukkit.getItemFactory().equals(meta, null)) {
            if (meta.hasDisplayName()) {
                result.put("name", meta.getDisplayName());
            }
            if (meta.hasLore()) {
                result.put("lore", meta.getLore());
            }
        }
        if (!enchants.isEmpty()) {
            result.put("enchantments", serEnchants);
        }
        if (item.getType().getMaxDurability() != 0 && meta instanceof Damageable) {
            result.put("damage", ((Damageable) meta).getDamage());
        }
        return result;
    }

    /**
     * Deserializes a Map into an ItemStack
     *
     * @param   map
     *          The Map with values like name
     *
     * @return the ItemStack created from the Map
     */
    public static ItemStack deserializeGson(Map<String, Object> map) {
        if (map.get("type") != null || map.get("material") == null) {
            throw new IllegalArgumentException("You are using an outdated/incompatible inventory mapping!");
        }

        Number amount = 0D;
        Material material = Material.getMaterial(String.valueOf(map.get("material"))); // saved as String

        if (material == null) {
            Logging.error("Inventory recovery material not found: " + map.get("material"));
            material = Material.STONE;
        }

        if (map.containsKey("amount")) {
            amount = (Number) map.get("amount"); // saved as Integer but the parser hates me
        }

        ItemStack item = new ItemStack(material, amount.intValue());
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        if (map.containsKey("name")) {
            meta.setDisplayName(String.valueOf(map.get("name")));
        }
        Object raw;
        if (map.containsKey("lore")) {
            raw = map.get("lore");
            if (raw instanceof List) {
                List<?> list = (List<?>) raw;
                List<String> lore = new ArrayList<>();
                for (Object o : list) {
                    lore.add(String.valueOf(o));
                }
                meta.setLore(lore);
            }
        }

        if (map.containsKey("enchantments")) {
            raw = map.get("enchantments");
            if (raw instanceof Map) {
                Map<?, ?> rawMap = (Map<?, ?>) raw;
                for (Object o : rawMap.keySet()) {
                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.fromString(String.valueOf(o)));
                    if (enchantment == null) {
                        continue;
                    }
                    Double level = (Double) rawMap.get(o);
                    item.addEnchantment(enchantment, level.intValue());
                }
            }
        }

        if (map.containsKey("damage") && meta instanceof Damageable) {
            Double value = (Double) map.get("damage");
            ((Damageable) meta).setDamage(value.intValue()); // saved as Integer
        }

        item.setItemMeta(meta);
        return item;
    }

}
