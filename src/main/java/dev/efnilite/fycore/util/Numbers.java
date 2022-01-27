package dev.efnilite.fycore.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Number utils
 */
public class Numbers {

    /**
     * Gets all ints from 0 (inclusive) to n (inclusive)
     *
     * @param   n
     *          The max value
     *
     * @return a list with all ints from 0 to n
     */
    public static List<Integer> getFromZero(int n) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            result.add(i);
        }
        return result;
    }

    /**
     * Gets the min int from an Array of ints
     *
     * @param   ints
     *          All ints
     *
     * @return the smallest of the values of parameter ints
     */
    public static int min(int... ints) {
        int min = Integer.MAX_VALUE;

        for (int value : ints) {
            min = Math.min(min, value);
        }

        return min;
    }

    public static int max(int... ints) {
        int max = Integer.MIN_VALUE;

        for (int value : ints) {
            max = Math.max(max, value);
        }

        return max;
    }

    public static List<Integer> getFromTo(int from, int to) {
        List<Integer> result = new ArrayList<>();
        for (int i = min(from, to); i <= max(from, to); i++) {
            result.add(i);
        }
        return result;
    }
}
