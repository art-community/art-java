package ru.adk.core.extension;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import java.util.concurrent.Callable;
import java.util.function.Function;

public interface ExceptionExtensions {
    static String emptyIfException(Callable<String> operation) {
        if (isNull(operation)) return EMPTY_STRING;
        try {
            return operation.call();
        } catch (Exception e) {
            return EMPTY_STRING;
        }
    }

    static <T> T nullIfException(Callable<T> operation) {
        if (isNull(operation)) return null;
        try {
            return operation.call();
        } catch (Exception e) {
            return null;
        }
    }

    static <T> T ifException(Callable<T> operation, T value) {
        requireNonNull(operation);
        try {
            return operation.call();
        } catch (Exception e) {
            return value;
        }
    }

    static <T> T ifExceptionOrEmpty(Callable<T> operation, T value) {
        requireNonNull(operation);
        try {
            T result = operation.call();
            if (isEmpty(result)) return value;
            return result;
        } catch (Exception e) {
            return value;
        }
    }

    static <T> T exceptionIfNull(T value, RuntimeException e) {
        if (isNull(value)) throw e;
        return value;
    }

    static <T> T exceptionIfEmpty(T value, RuntimeException e) {
        if (isEmpty(value)) throw e;
        return value;
    }

    static <T> T wrapException(Callable<T> action, Function<Exception, RuntimeException> exceptionFactory) {
        try {
            return action.call();
        } catch (Exception e) {
            throw exceptionFactory.apply(e);
        }
    }
}
