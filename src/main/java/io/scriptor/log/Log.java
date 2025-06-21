package io.scriptor.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    private static final Log instance = new Log();

    public static void info(final String format, final Object... arguments) {
        instance.getLogger().info(() -> format.formatted(arguments));
    }

    public static void warning(final String format, final Object... arguments) {
        instance.getLogger().warning(() -> format.formatted(arguments));
    }

    public static void severe(final String format, final Object... arguments) {
        instance.getLogger().severe(() -> format.formatted(arguments));
    }

    public static void throwing(final String sourceClass, final String sourceMethod, final Throwable thrown) {
        instance.getLogger().throwing(sourceClass, sourceMethod, thrown);
    }

    public static void trace(final Throwable e) {
        instance.getLogger().log(Level.SEVERE, e.getMessage(), e);
    }

    private final Logger logger;

    private Log() {
        logger = Logger.getLogger("io.scriptor");
    }

    private Logger getLogger() {
        return logger;
    }
}
