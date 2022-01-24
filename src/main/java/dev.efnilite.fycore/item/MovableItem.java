package dev.efnilite.fycore.item;

import org.bukkit.Material;

public class MovableItem extends Item {

    public MovableItem(Material material, String name) {
        super(material, name);
    }

    public MovableItem(Material material, int amount, String name) {
        super(material, amount, name);
    }

    @Override
    public boolean isMovable() {
        return true;
    }
}
