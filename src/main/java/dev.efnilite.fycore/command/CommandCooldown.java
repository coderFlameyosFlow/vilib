package dev.efnilite.fycore.command;

/**
 * Class for managing command cooldowns
 */
public class CommandCooldown {

    /**
     * The argument
     */
    private final String arg;

    /**
     * When the command with arg was last executed
     */
    private final long lastExecuted;

    public CommandCooldown(String arg) {
        this.lastExecuted = System.currentTimeMillis();
        this.arg = arg;
    }

    public long getLastExecuted() {
        return lastExecuted;
    }

    public String getArg() {
        return arg;
    }
}
