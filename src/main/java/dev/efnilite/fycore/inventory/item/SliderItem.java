package dev.efnilite.fycore.inventory.item;

import dev.efnilite.fycore.inventory.Menu;
import dev.efnilite.fycore.inventory.MenuClickEvent;
import dev.efnilite.fycore.util.FyMap;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

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
    private final FyMap<Integer, Function<MenuClickEvent, Boolean>> switchFunctions = new FyMap<>();

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
     * Adds an item to the possible options. This uses a Function.
     * The Function will determine whether the item will update in the inventory.
     * If this returns false, it will not update the item in the menu, but it will execute the code.
     *
     * @param   value
     *          The value assigned to this item
     *
     * @param   item
     *          The item
     *
     * @param   onSwitchTo
     *          What happens on switch to this item. Returns true if it should update, false if not.
     *
     * @return the instance of this class
     */
    public SliderItem add(int value, Item item, Function<MenuClickEvent, Boolean> onSwitchTo) {
        items.put(value, item);
        switchFunctions.put(value, onSwitchTo);
        return this;
    }

    @Override
    public void handleClick(Menu menu, InventoryClickEvent event, ClickType clickType) {
        int currentTo = current;
        switch (clickType) {
            case LEFT:
                currentTo++;
                if (currentTo >= items.size()) {
                    currentTo = 0;
                }
                break;
            case SHIFT_LEFT:
                currentTo = items.size() - 1;
                break;
            case RIGHT:
                currentTo--;
                if (currentTo == -1) {
                    currentTo = items.size() - 1;
                }
                break;
            case SHIFT_RIGHT:
                currentTo = 0;
                break;
            default:
                return;
        }

        Function<MenuClickEvent, Boolean> function = switchFunctions.get(currentTo);
        if (function == null) {
            return;
        }
        boolean update = function.apply(new MenuClickEvent(event.getSlot(), menu, this, event));

        if (update) {
            event.getInventory().setItem(event.getSlot(), items.get(currentTo).build());
            menu.updateItem(event.getSlot());

            current = currentTo;
        }
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
