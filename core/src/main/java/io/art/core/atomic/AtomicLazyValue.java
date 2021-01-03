package io.art.core.atomic;

import io.art.core.lazy.*;
import static io.art.core.checker.NullityChecker.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class AtomicLazyValue<T> {
    private final AtomicReference<LazyValue<T>> reference = new AtomicReference<>();

    public AtomicLazyValue<T> initialize(Supplier<LazyValue<T>> factory) {
        if (reference.compareAndSet(null, null)) {
            reference.set(factory.get());
        }
        return this;
    }

    public AtomicLazyValue<T> dispose(Consumer<T> consumer) {
        LazyValue<T> value;
        if (nonNull(value = reference.getAndSet(null))) {
            if (value.initialized()) {
                consumer.accept(value.get());
            }
        }
        return this;
    }

    public boolean initialized() {
        return let(reference.get(), LazyValue::initialized, false);
    }

    public void apply(Consumer<T> action) {
        reference.getAndUpdate(current -> {
            action.accept(get());
            return current;
        });
    }

    public void update(UnaryOperator<LazyValue<T>> action) {
        reference.getAndUpdate(action);
    }

    public T get() {
        return reference.get().get();
    }

    public <R> AtomicLazyValue<R> map(Function<T, R> mapper) {
        return AtomicLazyValue.<R>atomicLazy().initialize(() -> reference.get().map(mapper));
    }

    public static <T> AtomicLazyValue<T> atomicLazy() {
        return new AtomicLazyValue<>();
    }
}
