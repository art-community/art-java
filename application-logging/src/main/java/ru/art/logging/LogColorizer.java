package ru.art.logging;

import lombok.experimental.*;
import org.apache.logging.log4j.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.logging.LogMessageColor.*;

@UtilityClass
public class LogColorizer {
    public static String message(String message, LogMessageColor color) {
        return color.getCode() + message + ANSI_RESET;
    }

    public static String success(String message) {
        return message(message, GREEN_BOLD);
    }

    public static String error(String message) {
        return message(message, RED);
    }

    public static String warning(String message) {
        return message(message, YELLOW);
    }

    public static String additional(String message) {
        return message(message, PURPLE_BOLD);
    }

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