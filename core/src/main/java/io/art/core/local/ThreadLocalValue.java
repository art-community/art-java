package io.art.core.local;

import lombok.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@RequiredArgsConstructor(access = PRIVATE)
public class ThreadLocalValue<T> {
    private final ThreadLocal<T> valueHolder = new ThreadLocal<>();
    private final Supplier<T> factory;

    public T get() {
        T value = valueHolder.get();
        if (isNull(value)) valueHolder.set(value = factory.get());
        return value;
    }

    public ThreadLocalValue<T> set(T newValue) {
        valueHolder.set(newValue);
        return this;
    }

    public <R> ThreadLocalValue<R> map(Function<T, R> mapper) {
        return new ThreadLocalValue<>(() -> mapper.apply(get()));
    }

    public ThreadLocalValue<T> initialize() {
        get();
        return this;
    }

    public boolean initialized() {
        return nonNull(valueHolder.get());
    }

    public boolean disposed() {
        return !initialized();
    }

    public void dispose() {
        valueHolder.set(null);
    }

    public static <T> ThreadLocalValue<T> threadLocal(Supplier<T> factory) {
        return new ThreadLocalValue<>(factory);
    }
}
