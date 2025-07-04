package io.scriptor.log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    private static final Log instance = new Log();

    public static void info(final @NotNull String format, final Object @Nullable ... arguments) {
        instance.getLogger().info(() -> format.formatted(arguments));
    }

    public static void warning(final @NotNull String format, final Object @Nullable ... arguments) {
        instance.getLogger().warning(() -> format.formatted(arguments));
    }

    public static void severe(final @NotNull String format, final Object @Nullable ... arguments) {
        instance.getLogger().severe(() -> format.formatted(arguments));
    }

    public static void throwing(
            final @NotNull String sourceClass,
            final @NotNull String sourceMethod,
            final @NotNull Throwable thrown
    ) {
        instance.getLogger().throwing(sourceClass, sourceMethod, thrown);
    }

    public static void trace(final @NotNull Throwable e) {
        instance.getLogger().log(Level.SEVERE, e.getMessage(), e);
    }

    private final Logger logger;

    private Log() {
        logger = Logger.getLogger("io.scriptor");
    }

    private @NotNull Logger getLogger() {
        return logger;
    }
}
