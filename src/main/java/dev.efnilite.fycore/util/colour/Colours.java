package dev.efnilite.fycore.util.colour;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for colouring utilities.
 *
 * Hex colouring source: https://github.com/sb2bg/CustomDeathMessages/blob/master/CustomDeathMessages/src/me/element/customdeathmessages/other/HexChat.java
 *
 * @author Efnilite
 */
public class Colours {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})"); // &#123456
    private static final char COLOUR_CHAR = '\u00A7'; // paragraph symbol, §

    /**
     * Translates hex colour codes into hex colours
     *
     * @param   string
     *          The String containing possible hex colour codes
     *
     * @return the String but coloured
     */
    private static String translate(String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        StringBuilder buffer = new StringBuilder();
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOUR_CHAR + "x"
                    + COLOUR_CHAR + group.charAt(0) + COLOUR_CHAR + group.charAt(1)
                    + COLOUR_CHAR + group.charAt(2) + COLOUR_CHAR + group.charAt(3)
                    + COLOUR_CHAR + group.charAt(4) + COLOUR_CHAR + group.charAt(5)
            );
        }
        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }


    /**
     * Color a String
     *
     * @param   string
     *          The string to be coloured
     *
     * @return a coloured String
     */
    public static String colour(String string) {
        if (!string.equals("")) {
            return ChatColor.translateAlternateColorCodes('&', translate(string));
        }
        return string;
    }

    /**
     * Color a List of Strings
     *
     * @param   strings
     *          The Strings to be colored
     *
     * @return the Strings, but colored
     */
    public static List<String> colour(List<String> strings) {
        List<String> result = new ArrayList<>();
        for (String string : strings) {
            result.add(colour(string));
        }
        return result;
    }

    /**
     * Color an Array of Strings
     *
     * @param   strings
     *          The Strings to be colored
     *
     * @return the Strings, but colored
     */
    public static List<String> colour(String... strings) {
        return colour(Arrays.asList(strings));
    }
}