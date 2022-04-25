package dev.efnilite.vilib.serialization;

import dev.efnilite.vilib.ViPlugin;
import dev.efnilite.vilib.util.Logging;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Class for basic ItemStack serialization.
 * This uses a custom base 64 method for saving, so it is not compatible with ItemStack#serialize/ItemStack#deserialize.
 * These methods are very lacking and outdated in comparison.
 */
public class ItemSerializer {

    public static String serialize64(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream output = new BukkitObjectOutputStream(outputStream);

            output.writeObject(item);

            output.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Throwable throwable) {
            ViPlugin.logging().stack("There was an error while trying to convert an item to base 64!",
                    "Please retry. If this error still persists, contact the developer!", throwable);
            return "";
        }
    }

    public static @Nullable ItemStack deserialize64(String string) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(string));
            BukkitObjectInputStream input = new BukkitObjectInputStream(inputStream);

            input.close();
            return (ItemStack) input.readObject();
        } catch (Throwable throwable) {
            ViPlugin.logging().stack("There was an error while trying to convert an item from base 64!",
                    "You are probably using an outdated inventory saving format.", throwable);
            return null;
        }
    }

//    /**
//     * Serializes an ItemStack into a Map
//     *
//     * @param   item
//     *          The item
//     *
//     * @return a Map with things like the name, lore, etc.
//     */
//    public static Map<String, Object> serializeGson(ItemStack item) {
//        ItemMeta meta = item.getItemMeta();
//        Map<String, Object> result = new HashMap<>();
//
//        Map<String, Integer> serEnchants = new HashMap<>();  // SOMEHOW ItemStack#serialize doesn't support enchantments
//        Map<Enchantment, Integer> enchants = item.getEnchantments();
//        for (Enchantment enchantment : enchants.keySet()) {
//            serEnchants.put(enchantment.getKey().toString(), enchants.get(enchantment)); // ItemStack#deserialize uses name value instead of key
//        }
//
//        result.put("material", item.getType().name());
//        result.put("amount", item.getAmount());
//
//        if (meta != null && !Bukkit.getItemFactory().equals(meta, null)) {
//            if (meta.hasDisplayName()) {
//                result.put("name", meta.getDisplayName());
//            }
//            if (meta.hasLore()) {
//                result.put("lore", meta.getLore());
//            }
//        }
//
//        if (!enchants.isEmpty()) {
//            result.put("enchantments", serEnchants);
//        }
//
//        if (item.getType().getMaxDurability() != 0 && meta instanceof Damageable) {
//            result.put("damage", ((Damageable) meta).getDamage());
//        }
//        return result;
//    }
//
//    /**
//     * Deserializes a Map into an ItemStack
//     *
//     * @param   map
//     *          The Map with values like name
//     *
//     * @return the ItemStack created from the Map
//     */
//    public static ItemStack deserializeGson(Map<String, Object> map) {
//        if (map.get("material") == null) {
//            throw new IllegalArgumentException("You are using an outdated/incompatible inventory mapping!");
//        }
//
//        Material material = Material.matchMaterial(String.valueOf(map.get("material"))); // saved as String
//        if (material == null) {
//            ViPlugin.logging().error("Inventory recovery material not found: " + map.get("material"));
//            material = Material.STONE;
//        }
//
//        Number amount = 0D;
//        if (map.containsKey("amount")) {
//            amount = (Number) map.get("amount"); // saved as Integer but the parser hates me
//        }
//
//        ItemStack item = new ItemStack(material, amount.intValue());
//        ItemMeta meta = item.getItemMeta();
//        if (meta == null) {
//            return item;
//        }
//
//        if (map.containsKey("name")) {
//            meta.setDisplayName(String.valueOf(map.get("name")));
//        }
//        Object raw;
//        if (map.containsKey("lore")) {
//            raw = map.get("lore");
//            if (raw instanceof List) {
//                List<?> list = (List<?>) raw;
//                List<String> lore = new ArrayList<>();
//                for (Object o : list) {
//                    lore.add(String.valueOf(o));
//                }
//                meta.setLore(lore);
//            }
//        }
//
//        if (map.containsKey("enchantments")) {
//            raw = map.get("enchantments");
//            if (raw instanceof Map) {
//                Map<?, ?> rawMap = (Map<?, ?>) raw;
//                for (Object o : rawMap.keySet()) {
//                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.fromString(String.valueOf(o)));
//                    if (enchantment == null) {
//                        continue;
//                    }
//                    Double level = (Double) rawMap.get(o);
//                    item.addEnchantment(enchantment, level.intValue());
//                }
//            }
//        }
//
//        if (map.containsKey("damage") && meta instanceof Damageable) {
//            Double value = (Double) map.get("damage");
//            ((Damageable) meta).setDamage(value.intValue()); // saved as Integer
//        }
//
//        item.setItemMeta(meta);
//        return item;
//    }
}