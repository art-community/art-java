package ru.art.core.lazy;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import static java.util.Objects.nonNull;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@RequiredArgsConstructor
@AllArgsConstructor
public class LazyLoadingValue<T> {
    private final ReentrantLock lock = new ReentrantLock();
    private final Supplier<T> loader;
    private boolean safe = false;
    private final AtomicReference<T> value = new AtomicReference<>();

    public T value() {
        T value;
        if (nonNull(value = this.value.get())) {
            return value;
        }
        if (this.value.compareAndSet(null, value = loader.get())) {
            return value;
        }
        return this.value.get();
    }

    public T safeValue() {
        T value;
        if (nonNull(value = this.value.get())) {
            return value;
        }
        try {
            lock.lock();
            if (this.value.compareAndSet(null, value = loader.get())) {
                return value;
            }
            return this.value.get();
        } finally {
            lock.unlock();
        }
    }

    public static <T> LazyLoadingValue<T> lazyValue(Supplier<T> factory) {
        return new LazyLoadingValue<T>(factory);
    }
}
