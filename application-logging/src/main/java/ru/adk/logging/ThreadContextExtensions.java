package ru.adk.logging;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.ThreadContext.put;

public interface ThreadContextExtensions {
    static void putIfNotNull(String key, Object value) {
        if (isNull(value)) return;
        put(key, value.toString());
    }
}
