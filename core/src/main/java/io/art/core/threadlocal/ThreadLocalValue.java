package io.art.core.threadlocal;

import lombok.*;
import static java.util.Objects.*;
import java.util.function.*;

@RequiredArgsConstructor
public class ThreadLocalValue<T> {
    private final ThreadLocal<T> valueHolder = new ThreadLocal<>();
    private final Supplier<T> factory;

    public T get() {
        T value = valueHolder.get();
        if (isNull(value)) valueHolder.set(value = factory.get());
        return value;
    }

    public <R> ThreadLocalValue<R> map(Function<T, R> mapper) {
        return new ThreadLocalValue<>(() -> mapper.apply(get()));
    }

    public ThreadLocalValue<T> initialize() {
        get();
        return this;
    }

    public boolean initialized() {
        return valueHolder.get() != null;
    }

    public static <T> ThreadLocalValue<T> threadLocal(Supplier<T> factory) {
        return new ThreadLocalValue<>(factory);
    }
}
