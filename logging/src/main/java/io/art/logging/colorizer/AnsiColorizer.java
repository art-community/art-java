package io.art.logging.colorizer;

import lombok.*;
import lombok.experimental.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.logging.colorizer.AnsiColorizer.AnsiColor.*;

@UtilityClass
public class AnsiColorizer {
    @Getter
    @AllArgsConstructor
    public enum AnsiColor {
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m"),
        BLACK_BOLD("\u001b[1;30m"),
        RED_BOLD("\u001b[1;31m"),
        GREEN_BOLD("\u001b[1;32m"),
        YELLOW_BOLD("\u001b[1;33m"),
        BLUE_BOLD("\u001b[1;34m"),
        PURPLE_BOLD("\u001b[1;35m"),
        CYAN_BOLD("\u001b[1;36m"),
        WHITE_BOLD("\u001b[1;37m");

        private final String code;
    }

    public static String message(String message, AnsiColor color) {
        return color.getCode() + message + ANSI_RESET;
    }

    public static String success(String message) {
        return message(message, GREEN_BOLD);
    }

    public static String error(String message) {
        return message(message, RED_BOLD);
    }

    public static String warning(String message) {
        return message(message, YELLOW_BOLD);
    }

    public static String additional(String message) {
        return message(message, PURPLE_BOLD);
    }

    public static String special(String message) {
        return message(message, CYAN_BOLD);
    }
}
