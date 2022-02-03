package dev.efnilite.fycore.chat;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colours {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final char COLOUR_CHAR = '§';

    private static String translate(String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        StringBuffer buffer = new StringBuffer();

        while(matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, "§x§" + group.charAt(0) + '§' + group.charAt(1) + '§' + group.charAt(2) + '§' + group.charAt(3) + '§' + group.charAt(4) + '§' + group.charAt(5));
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static String colour(String string) {
        return !string.equals("") ? ChatColor.translateAlternateColorCodes('&', translate(string)) : string;
    }

    public static List<String> colour(List<String> strings) {
        List<String> result = new ArrayList<>();

        for (String string : strings) {
            result.add(colour(string));
        }

        return result;
    }

    public static List<String> colour(String... strings) {
        return colour(Arrays.asList(strings));
    }
}
