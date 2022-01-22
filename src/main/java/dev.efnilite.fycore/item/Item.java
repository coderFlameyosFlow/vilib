package dev.efnilite.fycore.item;

import dev.efnilite.fycore.util.Version;
import dev.efnilite.fycore.util.colour.Colours;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * A class for creating items.
 *
 * @author Efnilite
 */
public class Item {

    private int amount;
    private int durability;
    private boolean glowing;
    private boolean unbreakable;
    private String name;
    private Material material;
    private BiConsumer<ItemStack, InventoryClickEvent> clickFunction;
    private final List<String> lore;

    /**
     * Creates a new instance
     *
     * @param   material
     *          The material
     *
     * @param   name
     *          The name of the item
     */
    public Item(Material material, String name) {
        this(material, 1, name);
    }

    /**
     * Creates a new instance
     *
     * @param   material
     *          The material
     *
     * @param   amount
     *          The amount of the item
     *
     * @param   name
     *          The name of the item
     */
    public Item(Material material, int amount, String name) {
        this.amount = amount;
        if (material != null) {
            this.durability = material.getMaxDurability();
        } else {
            material = Material.GRASS_BLOCK;
        }
        this.name = name;
        this.lore = new ArrayList<>();
        this.material = material;
        this.unbreakable = false;
    }

    /**
     * Finishes everything and gives the ItemStack result.
     *
     * @return the result
     */
    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        assert meta != null;

        if (glowing) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        meta.setDisplayName(Colours.colour(name));
        meta.setLore(lore);

        if (Version.isHigherOrEqual(Version.V1_13)) {
            ((Damageable) meta).setDamage(Math.abs(durability - material.getMaxDurability()));
            meta.setUnbreakable(unbreakable);
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Set the function on click
     *
     * @param   consumer
     *          Useful values which are gathered in the click e vent
     *
     * @return the instance of this class
     */
    public Item click(BiConsumer<ItemStack, InventoryClickEvent> consumer) {
        this.clickFunction = consumer;
        return this;
    }

    public void handleClick(ItemStack item, InventoryClickEvent watcher) {
        clickFunction.accept(item, watcher);
    }

    /**
     * Set unbreakable
     *
     * @return the instance this class
     */
    public Item unbreakable() {
        this.unbreakable = true;
        return this;
    }

    /**
     * Set glowing
     *
     * @return the instance this class
     */
    public Item glowing() {
        this.glowing = true;
        return this;
    }

    /**
     * Set glowing
     *
     * @return the instance of this class
     */
    public Item glowing(boolean predicate) {
        if (predicate) {
            this.glowing = true;
        }
        return this;
    }

    /**
     * Sets the name
     *
     * @param   name
     *          The name
     *
     * @return  the instance this class
     */
    public Item name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the durability of the item
     *
     * @return the instance of this class
     */
    public Item durability(int durability) {
        this.durability = durability;
        return this;
    }


    /**
     * Sets the item amount
     *
     * @param   amount
     *          The item amount
     *
     * @return the instance of this class
     */
    public Item amount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Sets the type
     *
     * @param   material
     *          The type
     *
     * @return  the instance this class
     */
    public Item material(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Sets the lore
     *
     * @param   lore
     *          The lore
     *
     * @return  the instance this class
     */
    public Item lore(@Nullable List<String> lore) {
        if (lore == null || lore.isEmpty()) {
            return this;
        }
        for (String l : lore) {
            this.lore.add(Colours.colour(l));
        }
        return this;
    }

    /**
     * Sets the lore
     *
     * @param   lore
     *          The lore
     *
     * @return the instance this class
     */
    public Item lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    /**
     * Gets the amount
     *
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the lore
     *
     * @return the lore
     */
    public List<String> getLore() {
        return lore;
    }

    /**
     * Gets the item type
     *
     * @return the type
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Gets the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
