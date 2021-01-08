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

    static <T> Supplier<T> emptySupplier() {
        return () -> null;
    }

    static <K, V> Function<K, V> emptyFunction() {
        return (K ignore) -> null;
    }
}
