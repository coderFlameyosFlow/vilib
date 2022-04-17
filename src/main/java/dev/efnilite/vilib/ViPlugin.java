package dev.efnilite.vilib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.efnilite.vilib.command.ViCommand;
import dev.efnilite.vilib.util.Logging;
import dev.efnilite.vilib.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ViPlugin extends JavaPlugin {

    protected static boolean verbosing;
    protected static ViPlugin viPlugin;
    protected static Gson gson;
    protected static Version version;

    @Override
    public void onEnable() {
        viPlugin = this;
        version = Version.getVersion();

        if (Version.isHigherOrEqual(Version.V1_13)) {
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().setLenient().create();
        }

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
    public void registerCommand(String name, ViCommand command) {
        ViCommand.register(name, command);
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

    public static ViPlugin getFyPlugin() {
        return viPlugin;
    }
}