package dev.efnilite.vilib.chat.tag;

import dev.efnilite.vilib.ViMain;
import dev.efnilite.vilib.chat.ChatFormat;
import dev.efnilite.vilib.chat.tag.paired.GradientTag;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Pattern;

/**
 * Super class for text format tags
 */
public abstract class TextTag {

    /**
     * The paragraph symbol, aka the colour symbol in Minecraft
     */
    protected static final char COLOUR_CHAR = '\u00A7';

    /**
     * The default tag pattern
     */
    protected Pattern pattern = Pattern.compile("<(\\S+?)>");

    /**
     * Applies an instance of a tag to a message
     *
     * @param   message
     *          The message
     *
     * @return the message but tags applied
     */
    public abstract String apply(String message);

    /**
     * Parses a message with the current available tags.
     *
     * @param   message
     *          The message
     *
     * @return the message but tags parsed
     */
    public static String parse(String message) {
        if (message == null || message.length() == 0) {
            return message;
        }

        String result = ChatColor.translateAlternateColorCodes('&', message); // support default colour codes

        result = new ColourTag().apply(result);
        result = new FormatTag().apply(result);
        result = new GradientTag().apply(result);

        return result;
    }

    /**
     * Gets the chat colour from a hex code.
     * If this code features a # at the beginning, it will be replaced.
     *
     * @param   hex
     *          The hex code.
     *
     * @return a String that looks like §x§a§b§c§d§e§f
     */
    protected static String of(String hex) {
        if (hex.startsWith("#") && hex.length() == 7) {
            hex = hex.substring(1);
        }
        if (hex.length() != 6) {
            ViMain.logging().stack("Invalid colour length " + hex.length() + " for code " + hex, new IllegalArgumentException());
        }

        StringBuilder builder = new StringBuilder("§x");
        char[] chars = hex.toCharArray();

        for (char c : chars) {
            builder.append('§').append(c);
        }

        return builder.toString();
    }

    /**
     * Gets the chat colour from a {@link ChatFormat}
     *
     * @param   format
     *          The chat format
     *
     * @return a String with the ChatFormat applied
     */
    protected static String of(ChatFormat format) {
        return "§" + format.getCode();
    }
}
