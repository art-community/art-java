package io.art.logging;

import lombok.experimental.*;
import static io.art.core.colorizer.AnsiColorizer.*;

@UtilityClass
public class LogColorizer {
    public static String byLevel(LoggingLevel level, String message) {
        switch (level) {
            case ERROR:
                return error(message);
            case WARN:
                return warning(message);
            case INFO:
                return success(message);
            case DEBUG:
                return additional(message);
            default:
                return message;
        }
    }

}
