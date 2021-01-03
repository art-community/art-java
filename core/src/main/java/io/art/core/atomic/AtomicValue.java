package io.art.core.atomic;

import static java.util.Objects.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class AtomicValue<T> {
    private final AtomicReference<T> reference = new AtomicReference<>();

    public AtomicValue<T> initialize(Supplier<T> factory) {
        if (reference.compareAndSet(null, null)) {
            reference.set(factory.get());
        }
        return this;
    }

    public AtomicValue<T> dispose(Consumer<T> consumer) {
        T value;
        if (nonNull(value = reference.getAndSet(null))) {
            consumer.accept(value);
        }
        return this;
    }

    public boolean initialized() {
        return nonNull(reference.get());
    }

    public void apply(Consumer<T> action) {
        reference.getAndUpdate(current -> {
            action.accept(get());
            return current;
        });
    }

    public void update(UnaryOperator<T> action) {
        reference.getAndUpdate(action);
    }

    public T get() {
        return reference.get();
    }

    public <R> AtomicValue<R> map(Function<T, R> mapper) {
        return AtomicValue.<R>atomic().initialize(() -> mapper.apply(reference.get()));
    }

    public static <T> AtomicValue<T> atomic() {
        return new AtomicValue<>();
    }
}
