package io.art.logging;

import lombok.*;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.util.Supplier;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.extensions.StringExtensions.*;
import java.util.function.*;

@Getter
public class ConfiguredLogger extends Logger {
    private final LoggingModuleConfiguration configuration;
    private final BiFunction<Level, String, String> messageDecorator;

    public ConfiguredLogger(String name, LoggingModuleConfiguration configuration) {
        super(LoggerContext.getContext(), name, LogManager.getLogger().getMessageFactory());
        this.configuration = configuration;
        messageDecorator = !configuration.getEnabled()
                ? emptyBiFunction()
                : configuration.getColored()
                ? LogColorizer::byLevel
                : (level, message) -> emptyIfNull(message);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, paramSuppliers));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object... params) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, params));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, p0));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, p0, p1));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, p0, p1, p2));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, p0, p1, p2, p3));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, p0, p1, p2, p3, p4));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, p0, p1, p2, p3, p4, p5));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, p0, p1, p2, p3, p4, p5, p6));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, p0, p1, p2, p3, p4, p5, p6, p7));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, p0, p1, p2, p3, p4, p5, p6, p7, p8));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Throwable t) {
        apply(messageDecorator.apply(level, message), result -> super.logIfEnabled(fqcn, level, marker, result, t));
    }
}
