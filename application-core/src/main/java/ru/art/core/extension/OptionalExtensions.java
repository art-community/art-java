package ru.art.core.extension;

import static java.util.Objects.isNull;
import java.util.Optional;

public interface OptionalExtensions {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <T> T unwrap(Optional<T> optional) {
        return optional.orElse(null);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static boolean isEmpty(Optional<?> optional) {
        return isNull(optional) || !optional.isPresent();
    }
}
