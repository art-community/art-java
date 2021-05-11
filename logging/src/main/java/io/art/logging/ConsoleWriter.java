package io.art.logging;

import static io.art.core.colorizer.AnsiColorizer.*;
import static io.art.core.constants.AnsiColor.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.logging.LogColorizer.*;
import static io.art.logging.LoggingLevel.*;
import static java.text.MessageFormat.*;
import static java.time.LocalDateTime.*;
import java.util.function.*;

public class ConsoleWriter implements LoggerWriter {
    private final BiConsumer<String, String> writer;
    private final String name;
    private final LoggerWriterConfiguration configuration;

    public ConsoleWriter(String name, LoggerWriterConfiguration configuration) {
        this.name = name;
        this.configuration = configuration;
        if (configuration.getLevel() == ERROR) {
            writer = configuration.getConsole().getColored()
                    ? (thread, message) -> System.err.println(buildMessage(thread, byLevel(ERROR, message)))
                    : (thread, message) -> System.err.println(buildMessage(thread, message));
            return;
        }
        writer = configuration.getConsole().getColored()
                ? (thread, message) -> System.out.println(buildMessage(thread, byLevel(configuration.getLevel(), message)))
                : (thread, message) -> System.out.println(buildMessage(thread, message));
    }

    @Override
    public void write(String thread, String message) {
        writer.accept(thread, message);
    }

    private String buildMessage(String thread, String message) {
        String dateTime = configuration.getDateTimeFormatter().format(now());
        LoggingLevel level = configuration.getLevel();
        return format("{0} {1} {2}: {3} - {4}",
                message(dateTime, BLUE),
                byLevel(level, level.name()),
                message(OPENING_SQUARE_BRACES + thread + CLOSING_SQUARE_BRACES, PURPLE_BOLD),
                name,
                message
        );
    }
}
