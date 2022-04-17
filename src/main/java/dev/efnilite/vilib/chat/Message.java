package dev.efnilite.vilib.chat;

import dev.efnilite.vilib.chat.tag.TextTag;
import org.bukkit.command.CommandSender;

public class Message {

    private final String text;

    public Message(String text) {
        this.text = text;
    }

    public void send(CommandSender sender) {
        sender.sendMessage(TextTag.parse(text));
    }

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(TextTag.parse(message));
    }

    public static String parseFormatting(String message) {
        return TextTag.parse(message);
    }

}
