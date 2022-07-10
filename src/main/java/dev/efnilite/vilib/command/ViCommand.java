package dev.efnilite.vilib.command;

import dev.efnilite.vilib.util.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to wrap commands, which makes it a lot easier to produce them.
 *
 * @author Efnilite
 */
public abstract class ViCommand implements CommandExecutor, TabCompleter {

    /**
     * UUID-based cooldown system
     */
    private final Map<UUID, List<CommandCooldown>> cooldowns = new HashMap<>();

    /**
     * Execute a command
     */
    public abstract boolean execute(CommandSender sender, String[] args);

    /**
     * Get what should be suggested
     */
    public abstract List<String> tabComplete(CommandSender sender, String[] args);

    /**
     * Checks the cooldown
     *
     * @param   sender
     *          The CommandSender which may have a cooldown
     *
     * @param   arg
     *          The argument to which this cooldown applies
     *
     * @param   cooldownMs
     *          The cooldown in ms
     *
     * @return false if the cooldown is not over yet, true if it has been.
     */
    protected boolean cooldown(CommandSender sender, String arg, long cooldownMs) {
        if (sender instanceof ConsoleCommandSender) { // ignore console (has no UUID)
            return true;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        CommandCooldown cooldown = null; // the current cooldown
        List<CommandCooldown> playerCooldowns = cooldowns.get(uuid);

        if (playerCooldowns == null) {
            playerCooldowns = new ArrayList<>();
            playerCooldowns.add(new CommandCooldown(arg));
            cooldowns.put(uuid, playerCooldowns);
            return true;
        }

        for (CommandCooldown plCooldown : playerCooldowns) { // get the appropriate commandcooldown class
            if (plCooldown.getArg().equals(arg)) {
                cooldown = plCooldown;
                break;
            }
        }

        if (cooldown == null) { // cooldown doesnt exist yet
            playerCooldowns.add(new CommandCooldown(arg));
            cooldowns.put(uuid, playerCooldowns);
            return true;
        }

        if (System.currentTimeMillis() - cooldown.getLastExecuted() > cooldownMs) {

            playerCooldowns.remove(cooldown); // update cooldown
            playerCooldowns.add(new CommandCooldown(arg));
            cooldowns.put(uuid, playerCooldowns);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets completions in relation to what the user has already typed
     *
     * @param   typed
     *          What the player has typed so far
     *
     * @param   possible
     *          The possible completions
     *
     * @return the updated possible completions
     */
    protected List<String> completions(String typed, List<String> possible) {
        return possible.stream()
                .filter(option -> option.toLowerCase().contains(typed))
                .collect(Collectors.toList());
    }

    /**
     * Gets completions in relation to what the user has already typed
     *
     * @param   typed
     *          What the player has typed so far
     *
     * @param   possible
     *          The possible completions
     *
     * @return the updated possible completions
     */
    protected List<String> completions(String typed, String... possible) {
        return Arrays.stream(possible)
                .filter(option -> option.toLowerCase().contains(typed))
                .collect(Collectors.toList());
    }

    /**
     * Registers a command under plugin.yml
     *
     * @param   name
     *          The name of this command in plugin.yml
     *
     * @param   wrapper
     *          The command that's going to be registered
     */
    public static void register(String name, ViCommand wrapper) {
        PluginCommand command = Bukkit.getPluginCommand(name);

        if (command == null) {
            return;
        }

        command.setExecutor(wrapper);
        command.setTabCompleter(wrapper);

        // add command to internal register
        Commands.add(name, command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return execute(sender, args);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return tabComplete(sender, args);
    }
}