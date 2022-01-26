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

}
