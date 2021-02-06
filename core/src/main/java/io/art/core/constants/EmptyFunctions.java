package io.art.core.constants;

import java.util.function.*;

public interface EmptyFunctions {
    static Runnable emptyRunnable() {
        return () -> {
        };
    }

    static <T> Consumer<T> emptyConsumer() {
        return (T ignore) -> {
        };
    }

    static <T, U> BiConsumer<T, U> emptyBiConsumer() {
        return (T first, U second) -> {
        };
    }

    static <T, U, R> BiFunction<T, U, R> emptyBiFunction() {
        return (T first, U second) -> null;
    }

    static <T> Supplier<T> emptySupplier() {
        return () -> null;
    }

    static <K, V> Function<K, V> emptyFunction() {
        return (K ignore) -> null;
    }
}
