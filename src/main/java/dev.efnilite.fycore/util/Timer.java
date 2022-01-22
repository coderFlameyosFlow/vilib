package dev.efnilite.fycore.util;

import java.util.HashMap;

/**
 * Class for timing things.
 *
 * @author Efnilite
 */
public class Timer {

    private static final HashMap<String, Long> timings = new HashMap<>();

    /**
     * Start the timer.
     *
     * @param   key
     *          The name of the operation which will be timed. These need to be unique!
     */
    public static void start(String key) {
        timings.put(key, System.currentTimeMillis());
    }

    /**
     * End the timer and get the time between start and finish in ms.
     *
     * @param   key
     *          The name of the operation.
     *
     * @return the time it took between starting and finishing.
     */
    public static long end(String key) {
        long startTime = timings.get(key);
        timings.remove(key);
        return System.currentTimeMillis() - startTime;
    }
}