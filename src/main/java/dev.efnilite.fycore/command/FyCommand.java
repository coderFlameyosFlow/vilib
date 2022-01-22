package dev.efnilite.fycore.command;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to wrap commands, which makes it a lot easier to produce them.
 *
 * @author Efnilite
 */
public abstract class FyCommand implements CommandExecutor, TabCompleter {

    /**
     * Execute a command
     */
    public abstract boolean execute(CommandSender sender, String[] args);

    /**
     * Get what should be suggested
     */
    public abstract List<String> tabComplete(CommandSender sender, String[] args);

    protected List<String> completions(String typed, List<String> options) {
        return options.stream()
                .filter(option -> option.toLowerCase().contains(typed))
                .collect(Collectors.toList());
    }

    protected List<String> completions(String typed, String... options) {
        return Arrays.stream(options)
                .filter(option -> option.toLowerCase().contains(typed))
                .collect(Collectors.toList());
    }

    public static void register(String name, FyCommand wrapper) {
        PluginCommand command = Bukkit.getPluginCommand(name);

        if (command == null) {
            return;
        }

        command.setExecutor(wrapper);
        command.setTabCompleter(wrapper);
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