package ru.art.logging;

import lombok.experimental.*;
import org.apache.logging.log4j.*;
import static ru.art.core.colorizer.AnsiColorizer.*;
import static ru.art.core.constants.AnsiColor.*;

@UtilityClass
public class LogColorizer {
    public static String byLevel(Level level, String message) {
        switch (level.getStandardLevel()) {
            case FATAL:
                return message(message, RED_BOLD);
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