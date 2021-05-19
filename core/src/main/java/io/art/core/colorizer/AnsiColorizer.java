package io.art.core.colorizer;

import io.art.core.constants.*;
import lombok.experimental.*;
import static io.art.core.constants.AnsiColor.*;
import static io.art.core.constants.StringConstants.*;

@UtilityClass
public class AnsiColorizer {
    public static String message(String message, AnsiColor color) {
        return color.getCode() + message + ANSI_RESET;
    }

    public static String success(String message) {
        return message(message, GREEN);
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

    public static String special(String message) {
        return message(message, CYAN_BOLD);
    }
}
