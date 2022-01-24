package dev.efnilite.fycore.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MenuExample {

    public void test(Player player) {

        new Menu(3, "Testing")
                .item(12, new SliderItem()
                        .add(0, new Item(Material.BLUE_STAINED_GLASS_PANE, "&b&l1"), (item) -> player.sendMessage("Switched to one"))
                        .add(1, new Item(Material.BLUE_STAINED_GLASS_PANE, "&b&l2"), (item) -> player.sendMessage("Switched to two"))
                        .initial(0))
                .item(14, new Item(Material.GOLDEN_AXE, "&6&lGet Axe"))
                .open(player);
    }
}