package io.art.logging;

import lombok.*;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.util.*;
import static io.art.logging.LogColorizer.*;

@Getter
public class ColoredLogger extends Logger {
    public ColoredLogger(String name) {
        super(LoggerContext.getContext(), name, LogManager.getLogger().getMessageFactory());
    }

    public ColoredLogger(Logger logger) {
        super(logger.getContext(), logger.getName(), logger.getMessageFactory());
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message));
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), paramSuppliers);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object... params) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), params);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), p0);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), p0, p1);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), p0, p1, p2);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), p0, p1, p2, p3);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), p0, p1, p2, p3, p4);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Throwable t) {
        super.logIfEnabled(fqcn, level, marker, byLevel(level, message), t);
    }
}
