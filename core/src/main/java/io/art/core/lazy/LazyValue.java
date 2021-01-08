package io.art.core.lazy;

import lombok.*;
import static io.art.core.constants.EmptyFunctions.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@RequiredArgsConstructor
public class LazyValue<T> implements Supplier<T> {
    private volatile T value;
    private final AtomicBoolean initialized = new AtomicBoolean();
    private final Supplier<T> loader;

    public LazyValue<T> initialize() {
        get();
        return this;
    }

    public boolean initialized() {
        return initialized.get();
    }

    public T get() {
        while (isNull(this.value)) {
            if (this.initialized.compareAndSet(false, true)) {
                this.value = loader.get();
            }
        }
        return this.value;
    }

    public void dispose(Consumer<T> action) {
        if (!initialized() || isNull(value)) return;
        while (nonNull(this.value)) {
            if (this.initialized.compareAndSet(true, false)) {
                T current = this.value;
                action.accept(current);
                this.value = null;
            }
        }
    }

    public <R> LazyValue<R> map(Function<T, R> mapper) {
        return new LazyValue<>(() -> mapper.apply(get()));
    }

    public void dispose() {
        dispose(emptyConsumer());
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
