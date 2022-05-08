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

/**
 * Main class which plugins may inherit to reduce the amount of setup required.
 * Classes inheriting this may want to create static methods to inherit the logging and viPlugin vars.
 *
 * Made by Efnilite (c) 2021-2022
 *
 * @since 1.0.0
 */
public abstract class ViPlugin extends JavaPlugin {

    protected Logging logging;
    protected ViPlugin viPlugin;
    protected static Gson gson;
    protected static Version version;

    @Override
    public void onEnable() {
        viPlugin = this;
        logging = new Logging(this);
        version = Version.getVersion();

        if (Version.isHigherOrEqual(Version.V1_13)) { // <1.13 makes me sad
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().setLenient().create();
        } else {
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
        }

        enable();
    }

    @Override
    public void onDisable() {
        disable();

        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
    }

    /**
     * What happens on enable of the plugin inheriting this library.
     */
    public abstract void enable();

    /**
     * What happens on disable of the plugin inheriting this library.
     * Disabling will automatically cancel all active tasks and unregister all EventWatchers.
     */
    public abstract void disable();

    /**
     * Register a command to this plugin.
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

    /**
     * Registers a Listener, with this plugin as its owner.
     *
     * @see dev.efnilite.vilib.event.EventWatcher
     *
     * @param   listener
     *          The listener to register.
     */
    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Gets a default Gson instance.
     *
     * @return a Gson instance which has already been set up.
     */
    public static Gson getGson() {
        return gson;
    }
}