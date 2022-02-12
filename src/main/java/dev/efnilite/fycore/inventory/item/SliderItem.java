package dev.efnilite.fycore.inventory.item;

import dev.efnilite.fycore.inventory.Menu;
import dev.efnilite.fycore.util.FyMap;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/**
 * Class for an item which uses the difference between left/right click to assign values
 *
 * @author Efnilite
 */
public class SliderItem extends MenuItem {

    /**
     * The initial value which will be displayed
     */
    private int current;
    private final FyMap<Integer, Item> items = new FyMap<>();
    private final FyMap<Integer, BiConsumer<Menu, InventoryClickEvent>> switchFunctions = new FyMap<>();

    /**
     * Sets the initial viewing index.
     * If you set up 2 items, with index 0 and 1, you can specify which will be viewed first.
     *
     * @param   initial
     *          The initial viewing index
     *
     * @return the instance of this class
     */
    public SliderItem initial(int initial) {
        this.current = initial;
        return this;
    }

    /**
     * Adds an item to the possible options
     *
     * @param   value
     *          The int value which the item will be assigned to. This must start from 0 and specifies the index of the panel.
     *
     * @param   item
     *          The item which will be displayed
     *
     * @return the instance of this class
     */
    public SliderItem add(int value, Item item, BiConsumer<Menu, InventoryClickEvent> onSwitchTo) {
        items.put(value, item);
        switchFunctions.put(value, onSwitchTo);
        return this;
    }

    @Override
    public void handleClick(Menu menu, InventoryClickEvent event, ClickType clickType) {
        switch (clickType) {
            case LEFT:
                current++;
                if (current >= items.size()) {
                    current = 0;
                }
                break;
            case SHIFT_LEFT:
                current = items.size() - 1;
                break;
            case RIGHT:
                current--;
                if (current == -1) {
                    current = items.size() - 1;
                }
                break;
            case SHIFT_RIGHT:
                current = 0;
                break;
            default:
                return;
        }

        BiConsumer<Menu, InventoryClickEvent> consumer = switchFunctions.get(current);
        if (consumer == null) {
            return;
        }
        consumer.accept(menu, event);

        event.getInventory().setItem(event.getSlot(), items.get(current).build());
        menu.updateItem(event.getSlot());
    }

    @Override
    public ItemStack build() {
        if (items.keySet().size() <= 0) {
            throw new IllegalArgumentException("Items size is <0 or 0!");
        }

        Item init = items.get(current);
        if (init == null) {
            init = items.get(items.randomKey());
        }

        return init.build();
    }

    @Override
    public boolean isMovable() {
        return false;
    }
}
