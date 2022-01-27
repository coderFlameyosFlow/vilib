package dev.efnilite.fycore.inventory;

import dev.efnilite.fycore.inventory.item.MenuItem;
import dev.efnilite.fycore.util.Numbers;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagedMenu extends Menu {

    private int total;
    public int current;
    public int nextPageSlot;
    public MenuItem nextPageItem;
    public int prevPageSlot;
    public MenuItem prevPageItem;
    private final List<Integer> displaySlots = new ArrayList<>();
    private final List<MenuItem> totalToDisplay = new ArrayList<>();
    private final Map<Integer, List<MenuItem>> assigned = new HashMap<>();

    public PagedMenu(int rows, String name) {
        super(rows, name);
    }

    /**
     * Excludes a specific set of rows from being used to display items
     *
     * @param   rows
     *          The rows which will be excluded
     *
     * @return the instance of this
     */
    public PagedMenu displayRows(int... rows) {
        int begin = Integer.MAX_VALUE;
        int end = Integer.MIN_VALUE;

        for (int row : rows) {
            int max = row * 9 - 1;
            int min = (row - 1) * 9;

            begin = Numbers.min(begin, min);
            end = Numbers.max(end, max);
        }
        displaySlots.addAll(Numbers.getFromTo(begin, end));
        return this;
    }

    @Override
    public void open(Player player) {
        assignPages();
        page(0);

        super.open(player);
    }

    public void page(int delta) {
        int newPage = current + delta;
        if (newPage < 0 || newPage > total) {
            return;
        }
        List<MenuItem> vals = assigned.get(newPage);
        for (int slot : displaySlots) {
            items.remove(slot);
            if (vals.size() <= 0) {
                break;
            }
            items.put(slot, vals.get(0));
            vals.remove(0);
        }
        items.remove(prevPageSlot);
        items.remove(nextPageSlot);
        if (newPage > 1) {
            items.put(prevPageSlot, prevPageItem);
        }
        if (newPage < total - 1) {
            items.put(nextPageSlot, nextPageItem);
        }
        update();
    }

    private void assignPages() {
        List<MenuItem> total = new ArrayList<>(totalToDisplay);
        List<MenuItem> thisPage = new ArrayList<>();

        int totalPages = (int) Math.ceil((total.size() + 0.0) / displaySlots.size());
        for (int page = 0; page < totalPages; page++) {
            for (int slot = 0; slot < displaySlots.size(); slot++) {
                if (total.size() <= 0) {
                    break;
                }
                thisPage.add(total.get(0));
                total.remove(0);
            }

            assigned.put(page, thisPage);
        }
        current = 0;
        this.total = assigned.keySet().size();
    }

    public PagedMenu addToDisplay(List<MenuItem> items) {
        totalToDisplay.addAll(items);
        return this;
    }

    public PagedMenu nextPage(int slot, MenuItem item) {
        this.nextPageSlot = slot;
        this.nextPageItem = item;
        return this;
    }

    public PagedMenu prevPage(int slot, MenuItem item) {
        this.prevPageSlot = slot;
        this.prevPageItem = item;
        return this;
    }
}