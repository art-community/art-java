package io.art.core.managed;

import io.art.core.annotation.*;
import static io.art.core.managed.DisposableValue.*;
import static java.util.Objects.*;
import java.util.function.*;

@UsedByGenerator
public class LazyValue<T> implements Supplier<T> {
    private final DisposableValue<T> disposable;

    public LazyValue(Supplier<T> loader) {
        disposable = disposable(loader);
    }

    public LazyValue<T> initialize() {
        disposable.get();
        return this;
    }

    public boolean initialized() {
        return disposable.initialized();
    }

    @Override
    public T get() {
        return disposable.get();
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

    public static <T> LazyValue<T> lazy(Supplier<T> factory) {
        return new LazyValue<>(factory);
    }
}
