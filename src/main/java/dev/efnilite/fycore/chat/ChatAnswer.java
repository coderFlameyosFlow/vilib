package dev.efnilite.fycore.chat;

import dev.efnilite.fycore.event.EventWatcher;
import dev.efnilite.fycore.util.Strings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Class for making listening to player answers in chat really easy
 */
public class ChatAnswer implements EventWatcher {

    /**
     * The player
     */
    private final Player player;

    /**
     * The text, when entered, which will disable the instance of this class
     */
    private String cancelText;

    /**
     * What to do after the message has been sent. This BiConsumer provides the answer and the player instance.
     */
    private BiConsumer<Player, String> postMessage;

    /**
     * Constructor.
     *
     * @param   player
     *          The player of which the chat will be monitored for answers
     */
    public ChatAnswer(Player player) {
        this.player = player;

        register();
    }

    /**
     * The text which will cancel waiting for an answer
     *
     * @param   cancelText
     *          If this text is entered, this instance gets disabled.
     *
     * @return the instance of this class
     */
    public ChatAnswer cancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    /**
     * What will happen before the answer is given.
     * The consumer will be executed the moment it's assigned.
     *
     * @param   consumer
     *          What to do. The player is given.
     *
     * @return the instance of this class
     */
    public ChatAnswer pre(Consumer<Player> consumer) {
        consumer.accept(player);
        return this;
    }

    /**
     * What will happen after the answer is given
     *
     * @param   consumer
     *          What to do after the answer. The player and the answer are given.
     *
     * @return the instance of this class
     */
    public ChatAnswer post(BiConsumer<Player, String> consumer) {
        this.postMessage = consumer;
        return this;
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        if (event.getPlayer() != player) {
            return;
        }

        String message = event.getMessage();
        if (Strings.getLevenshteinDistance(cancelText, message) < 2) {
            postMessage.accept(player, message);
        }
        unregister();
    }
}