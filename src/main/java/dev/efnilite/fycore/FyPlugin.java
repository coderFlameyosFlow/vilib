package dev.efnilite.fycore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.efnilite.fycore.command.FyCommand;
import dev.efnilite.fycore.util.Logging;
import dev.efnilite.fycore.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class FyPlugin extends JavaPlugin {

    protected static boolean verbosing;
    protected static FyPlugin fyPlugin;
    protected static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().setLenient().create();
    protected static Version version;

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

        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
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
    public void registerCommand(String name, FyCommand command) {
        FyCommand.register(name, command);
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public static Gson getGson() {
        return gson;
    }

    public static boolean vebosing() {
        return verbosing;
    }

    public static FyPlugin getFyPlugin() {
        return fyPlugin;
    }
}