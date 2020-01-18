package ru.art.core.colorizer;

import lombok.experimental.*;
import ru.art.core.constants.*;
import static ru.art.core.constants.AnsiColor.*;
import static ru.art.core.constants.AnsiColor.PURPLE_BOLD;
import static ru.art.core.constants.StringConstants.ANSI_RESET;

@UtilityClass
public class AnsiColorizer {
    public static String message(String message, AnsiColor color) {
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
