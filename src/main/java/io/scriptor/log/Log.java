package io.scriptor.log;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {

    private static class CustomFormatter extends Formatter {

        @Override
        public String format(final LogRecord record) {
            return "[ %1$tF %1$tT ] %2$-8s | %3$s%n".formatted(new Date(record.getMillis()),
                                                      record.getLevel(),
                                                      record.getMessage());
        }
    }

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

    private final Logger logger;

    private Log() {
        final var handler = new ConsoleHandler();
        handler.setFormatter(new CustomFormatter());

        logger = Logger.getGlobal();
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    private Logger getLogger() {
        return logger;
    }
}
