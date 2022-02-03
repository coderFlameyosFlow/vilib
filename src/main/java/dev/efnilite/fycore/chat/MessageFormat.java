package dev.efnilite.fycore.chat;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageFormat {

    private static final Pattern TAGS_PATTERN = Pattern.compile("<(\\S+)>");
    private static final Pattern FORMAT_TAG_PATTERN = Pattern.compile("<([a-z]+)>");
    private static final Pattern COLOUR_TAG_PATTERN = Pattern.compile("<([a-z_]+)>");
    private static final Pattern HEX_TAG_PATTERN = Pattern.compile("<(#[a-fA-F0-9]{6})>");

    public static void main(String[] args) {
        System.out.println(formatFormats("<bold>Help me!<reset><underline>"));
        System.out.println(formatColours("<red>Help me!<dark_red><aqua>"));
        System.out.println(split("<#123456>Help me!<#123456><#123456>"));
    }

    private static String split(@NotNull String message) {
        String[] parts = TAGS_PATTERN.split(message);
        Matcher matcher = TAGS_PATTERN.matcher(message);

        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            builder.append(part);
            if (matcher.find()) {
                builder.append("<").append(matcher.group(1)).append(">");
            }
        }

        return builder.toString();
    }

    private static ComponentBuilder

    private static String formatFade(@NotNull String message) {
        Matcher matcher = HEX_TAG_PATTERN.matcher(message);

        while (matcher.find()) {
            String group = matcher.group(1); // inner group
            message = message.replace(matcher.group(), group); // outer group, todo add chatcolor.of
        }

        return message;
    }

    private static String formatHex(@NotNull String message) {
        Matcher matcher = HEX_TAG_PATTERN.matcher(message);

        while (matcher.find()) {
            String group = matcher.group(1); // inner group
            message = message.replace(matcher.group(), group); // outer group, todo add chatcolor.of
        }

        return message;
    }

    private static String formatColours(@NotNull String message) {
        Matcher matcher = COLOUR_TAG_PATTERN.matcher(message);

        while (matcher.find()) {
            String group = matcher.group(); // inner group
            ChatColour colour = ChatColour.getByName(group.replaceAll("[<>]", ""));
            if (colour != null) {
                message = message.replace(matcher.group(), "#" + colour.getHex()); // outer group, todo add chatcolor.of
            }
        }

        return message;
    }

    /**
     * Formats the formats
     *
     * @param   message
     *          The message
     *
     * @return the string but formatted with formats
     */
    private static String formatFormats(@NotNull String message) {
        Matcher matcher = FORMAT_TAG_PATTERN.matcher(message);

        while (matcher.find()) {
            String group = matcher.group(1); // inner group
            ChatFormat format = ChatFormat.getByName(group.replaceAll("[<>]", ""));
            if (format != null) {
                message = message.replace(matcher.group(), "&" + format.getCode()); // outer group, todo add chatcolor
            }
        }

        return message;
    }

}