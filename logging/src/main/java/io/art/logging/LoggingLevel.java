package io.art.logging;

import lombok.*;
import static java.util.Arrays.*;

@Getter
@AllArgsConstructor
public enum LoggingLevel {
    ERROR((byte) 0),
    WARN((byte) 1),
    INFO((byte) 2),
    TRACE((byte) 3),
    DEBUG((byte) 4);

    private final byte level;

    public static LoggingLevel parse(String level, LoggingLevel defaultLevel) {
        return stream(LoggingLevel.values())
                .filter(known -> known.name().toLowerCase().equals(level))
                .findFirst()
                .orElse(defaultLevel);
    }
}
