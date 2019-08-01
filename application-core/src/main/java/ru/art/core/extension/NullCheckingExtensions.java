package ru.art.core.extension;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import java.util.function.Function;

public interface NullCheckingExtensions {
    static <T> T getOrElse(T object, T orElse) {
        return isNull(object) ? orElse : object;
    }

    static <T> T nullOrElse(Object val, T orElse) {
        return isNull(val) ? null : orElse;
    }

    static <T, R> R doIfNotNull(T val, Function<T, R> action) {
        return nonNull(val) ? action.apply(val) : null;
    }
}
