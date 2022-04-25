package dev.efnilite.vilib.config;

import dev.efnilite.vilib.ViMain;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for config options
 *
 * @param   <Type>
 *          The type of the config option: double, integer, etc.
 */
@SuppressWarnings("unchecked")
public class ConfigOption<Type> {

    private Type value;
    private Pattern regex;

    public ConfigOption(FileConfiguration config, String path, String regex) {
        try {
            value = (Type) config.get(path);
        } catch (ClassCastException ex) {
            ViMain.logging().stack("Incompatible types in config option '" + path + "': " + ex.getMessage(),
                    "check if you have entered the correct type of data for path "+ path, ex);
            return;
        }

        if (value == null) {
            ViMain.logging().stack("No value found for option " + path,
                    "check if you have entered anything for path " + path);
        } else if (regex != null) {
            this.regex = Pattern.compile(regex);
            Matcher matcher = this.regex.matcher(String.valueOf(value));
            if (!matcher.find()) {
                ViMain.logging().stack("Invalid type regex found for option " + path, "check if you have entered the correct syntax");
            }
        }
    }

    public ConfigOption(FileConfiguration config, String path) {
        this(config, path, null);
    }

    public ConfigOption(Type value) {
        this.value = value;

        if (value == null) {
            ViMain.logging().stack("No value found for unknown option",
                    "check if you have entered everything correctly");
        }
    }

    public void thenSet(Type value) {
        this.value = value;
    }

    public double getAsDouble() {
        if (value instanceof Double) {
            return Double.parseDouble(String.valueOf(value));
        }
        return 0D;
    }

    public Pattern getRegex() {
        return regex;
    }

    public Type get() {
        return value;
    }
}
