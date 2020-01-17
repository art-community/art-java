package ru.art.logging;

import lombok.experimental.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.logging.LogMessageColor.*;

@UtilityClass
public class ColoredLogger {
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
}