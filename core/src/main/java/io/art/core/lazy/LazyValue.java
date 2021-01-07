package io.art.core.lazy;

import lombok.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@RequiredArgsConstructor
public class LazyValue<T> implements Supplier<T> {
    private final AtomicReference<T> value = new AtomicReference<>();
    private final Supplier<T> loader;


    public LazyValue<T> initialize() {
        get();
        return this;
    }

    public boolean initialized() {
        return nonNull(value.get());
    }

    public T get() {
        T value;
        if (nonNull(value = this.value.get())) {
            return value;
        }
        if (this.value.compareAndSet(null, value = loader.get())) {
            return value;
        }
        return this.value.get();
    }


    public <R> LazyValue<R> map(Function<T, R> mapper) {
        return new LazyValue<>(() -> mapper.apply(get()));
    }


    public void apply(Consumer<T> action) {
        update(current -> {
            action.accept(current);
            return current;
        });
    }

    public void dispose(Consumer<T> action) {
        T current;
        if (nonNull(current = value.getAndSet(null))) {
            action.accept(current);
        }
    }


    public LazyValue<T> update(UnaryOperator<T> action) {
        getAndUpdate(action);
        return this;
    }

    public T getAndUpdate(UnaryOperator<T> action) {
        return value.getAndUpdate(action);
    }

    public T updateAndGet(UnaryOperator<T> action) {
        return value.updateAndGet(action);
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
