package io.art.logging;

import lombok.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.logging.LoggerWriterFactory.*;
import static io.art.logging.LoggingLevel.*;
import static java.lang.Thread.*;
import static java.time.LocalDateTime.*;
import static java.util.stream.Collectors.*;
import java.util.*;

@Getter
public class ConfiguredLogger implements Logger {
    private final String name;
    private final EnumSet<LoggingLevel> levels;
    private final LoggingModuleConfiguration configuration;
    private final List<LoggerWriter> infoWriters;
    private final List<LoggerWriter> errorWriters;
    private final List<LoggerWriter> debugWriters;
    private final List<LoggerWriter> traceWriters;
    private final List<LoggerWriter> warnWriters;

    public ConfiguredLogger(String name, LoggingModuleConfiguration configuration) {
        this.name = name;
        this.configuration = configuration;
        infoWriters = configuration.getWriters()
                .stream()
                .filter(writer -> writer.getCategories().contains(name) && writer.getLevel().getLevel() >= INFO.getLevel())
                .map(writerConfiguration -> loggerWriter(name, writerConfiguration))
                .collect(listCollector());
        errorWriters = configuration.getWriters()
                .stream()
                .filter(writer -> writer.getCategories().contains(name) && writer.getLevel().getLevel() >= ERROR.getLevel())
                .map(writerConfiguration -> loggerWriter(name, writerConfiguration))
                .collect(listCollector());
        debugWriters = configuration.getWriters()
                .stream()
                .filter(writer -> writer.getCategories().contains(name) && writer.getLevel().getLevel() >= DEBUG.getLevel())
                .map(writerConfiguration -> loggerWriter(name, writerConfiguration))
                .collect(listCollector());
        traceWriters = configuration.getWriters()
                .stream()
                .filter(writer -> writer.getCategories().contains(name) && writer.getLevel().getLevel() >= TRACE.getLevel())
                .map(writerConfiguration -> loggerWriter(name, writerConfiguration))
                .collect(listCollector());
        warnWriters = configuration.getWriters()
                .stream()
                .filter(writer -> writer.getCategories().contains(name) && writer.getLevel().getLevel() >= WARN.getLevel())
                .map(writerConfiguration -> loggerWriter(name, writerConfiguration))
                .collect(listCollector());
        errorWriters.add(loggerWriter(name, configuration.getDefaultWriter().toBuilder().level(ERROR).build()));
        warnWriters.add(loggerWriter(name, configuration.getDefaultWriter().toBuilder().level(WARN).build()));
        infoWriters.add(loggerWriter(name, configuration.getDefaultWriter().toBuilder().level(INFO).build()));
        traceWriters.add(loggerWriter(name, configuration.getDefaultWriter().toBuilder().level(TRACE).build()));
        debugWriters.add(loggerWriter(name, configuration.getDefaultWriter().toBuilder().level(DEBUG).build()));
        if (configuration.getWriters().isEmpty()) {
            levels = EnumSet.of(configuration.getDefaultWriter().getLevel());
            return;
        }
        levels = EnumSet.copyOf(configuration.getWriters().stream().map(LoggerWriterConfiguration::getLevel).collect(toList()));
    }

    @Override
    public void log(LoggingLevel level, String message) {
        String thread = currentThread().getName();
        switch (level) {
            case ERROR:
                errorWriters.forEach(writer -> configuration.getExecutor().execute(() -> writer.write(thread, message), now()));
                return;
            case INFO:
                infoWriters.forEach(writer -> configuration.getExecutor().execute(() -> writer.write(thread, message), now()));
                return;
            case TRACE:
                traceWriters.forEach(writer -> configuration.getExecutor().execute(() -> writer.write(thread, message), now()));
                return;
            case DEBUG:
                debugWriters.forEach(writer -> configuration.getExecutor().execute(() -> writer.write(thread, message), now()));
                return;
            case WARN:
                warnWriters.forEach(writer -> configuration.getExecutor().execute(() -> writer.write(thread, message), now()));
        }
    }
}
