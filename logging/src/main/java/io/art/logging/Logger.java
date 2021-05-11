package io.art.logging;

import com.google.common.base.*;
import static com.google.common.base.Throwables.getStackTraceAsString;
import static io.art.core.checker.EmptinessChecker.ifEmpty;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.constants.StringConstants.NEW_LINE;
import static io.art.logging.LoggingLevel.*;
import static java.text.MessageFormat.*;
import java.util.*;

public interface Logger {
    String getName();

    EnumSet<LoggingLevel> getLevels();

    void log(LoggingLevel level, String message);

    default void log(LoggingLevel level, String message, Throwable error) {
        String fullError = getStackTraceAsString(error);
        if (isEmpty(message)) {
            log(level, fullError);
            return;
        }
        log(level, message + NEW_LINE + fullError);
    }

    default void trace(String message) {
        log(TRACE, message);
    }

    default void trace(String format, Object... arguments) {
        log(TRACE, format(format, arguments));
    }

    default void trace(String message, Throwable error) {
        log(TRACE, message, error);
    }


    default void debug(String message) {
        log(DEBUG, message);
    }

    default void debug(String format, Object... arguments) {
        log(DEBUG, format(format, arguments));
    }

    default void debug(String message, Throwable error) {
        log(DEBUG, message, error);
    }


    default void info(String message) {
        log(INFO, message);
    }

    default void info(String format, Object... arguments) {
        log(INFO, format(format, arguments));
    }

    default void info(String message, Throwable error) {
        log(INFO, message, error);
    }


    default void warn(String message) {
        log(WARN, message);
    }

    default void warn(String format, Object... arguments) {
        log(WARN, format(format, arguments));
    }

    default void warn(String message, Throwable error) {
        log(WARN, message, error);
    }


    default void error(String message) {
        log(ERROR, message);
    }

    default void error(String format, Object... arguments) {
        log(ERROR, format(format, arguments));
    }

    default void error(String message, Throwable error) {
        log(ERROR, message, error);
    }


    default boolean isErrorEnabled() {
        return getLevels().stream().anyMatch(level -> level.getLevel() >= ERROR.getLevel());
    }

    default boolean isWarnEnabled() {
        return getLevels().stream().anyMatch(level -> level.getLevel() >= WARN.getLevel());
    }

    default boolean isInfoEnabled() {
        return getLevels().stream().anyMatch(level -> level.getLevel() >= INFO.getLevel());
    }

    default boolean isDebugEnabled() {
        return getLevels().stream().anyMatch(level -> level.getLevel() >= DEBUG.getLevel());
    }

    default boolean isTraceEnabled() {
        return getLevels().stream().anyMatch(level -> level.getLevel() >= TRACE.getLevel());
    }

    default void error(Throwable throwable) {
        error(ifEmpty(throwable.getLocalizedMessage(), throwable.getMessage()), throwable);
    }
}
