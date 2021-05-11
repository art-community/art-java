package io.art.logging;

import static java.util.Arrays.*;

public enum LoggingWriterType {
    CONSOLE;

    public static LoggingWriterType parse(String type, LoggingWriterType defaultType) {
        return stream(LoggingWriterType.values())
                .filter(known -> known.name().toLowerCase().equals(type))
                .findFirst()
                .orElse(defaultType);
    }
}
