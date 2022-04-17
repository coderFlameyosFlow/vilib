package dev.efnilite.vilib.util;

import org.bukkit.Bukkit;

/**
 * Version class, useful for checking whether certain features on legacy can be executed.
 *
 * @author Efnilite
 */
public enum Version {

    V1_8(8),
    V1_9(9),
    V1_10(10),
    V1_11(11),
    V1_12(12),
    V1_13(13),
    V1_14(14),
    V1_15(15),
    V1_16(16),
    V1_17(17),
    V1_18(18);

    public final int major;

    Version(int major) {
        this.major = major;
    }

    public static Version VERSION;

    /**
     * Returns whether the version is higher or equal to a given version.
     *
     * @param   compareTo
     *          The version to compare to
     *
     * @return true if the current version is higher or equal to the given version, false if not
     */
    public static boolean isHigherOrEqual(Version compareTo) {
        return VERSION.major >= compareTo.major; // 17 >= 16 -> true
    }

    public static String getPrettyVersion() {
        String string = getFullVersion().replace("_", ".");
        string = string.endsWith(".") ? string.substring(0, string.length() - 1) : string;
        return string;
    }

    public static Version getVersion() {
        String string = getFullVersion().toUpperCase();
        string = string.endsWith("_") ? string.substring(0, string.length() - 1) : string;
        VERSION = valueOf(string);
        return VERSION;
    }

    private static String getFullVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3].substring(0, 5);
    }
}