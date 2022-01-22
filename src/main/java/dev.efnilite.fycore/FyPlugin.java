package dev.efnilite.fycore;

import dev.efnilite.fycore.command.FyCommand;
import dev.efnilite.fycore.reflection.Reflection;
import dev.efnilite.fycore.util.Logging;
import dev.efnilite.fycore.util.Version;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class FyPlugin extends JavaPlugin {

    protected static boolean verbosing;
    protected static FyPlugin fyPlugin;
    protected Version version;

    @Override
    public void onEnable() {
        fyPlugin = this;
        version = Version.getVersion();
        Logging.init(this);

        enable();
    }

    @Override
    public void onDisable() {
        disable();
    }

    public abstract void enable();

    public abstract void disable();

    /**
     * Register a command
     *
     * @param   name
     *          The name of the command in plugin.yml
     *
     * @param   command
     *          The command class
     */
    public void registerCommand(String name, Class<? extends FyCommand> command) {
        FyCommand.register(name, Reflection.newInstance(command));
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public static boolean vebosing() {
        return verbosing;
    }

    public static FyPlugin getFyPlugin() {
        return fyPlugin;
    }
}