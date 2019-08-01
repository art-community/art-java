package ru.adk.core.extension;

import static java.time.Instant.ofEpochMilli;
import static java.time.ZoneId.systemDefault;
import static java.util.Objects.isNull;
import java.time.LocalDateTime;

public interface DateTimeExtensions {
    static long toMillis(LocalDateTime dateTime) {
        if (isNull(dateTime)) {
            return 0L;
        }
        return dateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }

    static LocalDateTime fromMillis(long millis) {
        return ofEpochMilli(millis).atZone(systemDefault()).toLocalDateTime();
    }
}
