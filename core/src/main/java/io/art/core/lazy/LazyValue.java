package io.art.core.lazy;

import io.art.core.annotation.*;
import static io.art.core.lazy.ManagedValue.*;
import static java.util.Objects.*;
import java.util.function.*;

@UsedByGenerator
public class LazyValue<T> implements Supplier<T> {
    private final ManagedValue<T> managed;

    public LazyValue(Supplier<T> loader) {
        managed = managed(loader);
    }

    public LazyValue<T> initialize() {
        managed.get();
        return this;
    }

    public boolean initialized() {
        return managed.initialized();
    }

    @Override
    public T get() {
        return managed.get();
    }

    public static <T> LazyValue<T> lazy(Supplier<T> factory) {
        return new LazyValue<>(factory);
    }

    @Override
    public int hashCode() {
        return get().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (isNull(other)) {
            return false;
        }
        if (!(other instanceof LazyValue)) {
            return false;
        }
        return get().equals(((LazyValue<?>) other).get());
    }
}
