package io.art.core.threadlocal;

import io.art.core.lazy.LazyValue;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class ThreadLocalValue<T> {
    private final ThreadLocal<T> valueHolder = new ThreadLocal<>();
    private final Supplier<T> factory;

    public T get(){
        if (isNull(valueHolder.get())) valueHolder.set(factory.get());
        return valueHolder.get();
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
