package io.art.core.property;

import io.art.core.exception.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.ExceptionMessages.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.QueueFactory.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@RequiredArgsConstructor
public class DisposableProperty<T> implements Supplier<T> {
    private volatile T value;
    private final Supplier<T> loader;
    private final AtomicBoolean initialized = new AtomicBoolean();
    private final Queue<Consumer<T>> initializeConsumers = queue();
    private final List<Consumer<T>> disposeConsumers = linkedList();


    public DisposableProperty<T> initialize() {
        get();
        return this;
    }


    public DisposableProperty<T> initialized(Consumer<T> consumer) {
        initializeConsumers.add(consumer);
        return this;
    }

    public DisposableProperty<T> disposed(Consumer<T> consumer) {
        disposeConsumers.add(consumer);
        return this;
    }


    public boolean initialized() {
        return initialized.get();
    }

    public boolean disposed() {
        return !initialized();
    }


    public void dispose() {
        if (disposed()) return;
        while (nonNull(this.value)) {
            if (this.initialized.compareAndSet(true, false)) {
                T current = this.value;
                disposeConsumers.forEach(consumer -> consumer.accept(current));
                this.value = null;
            }
        }
    }


    @Override
    public T get() {
        while (isNull(this.value)) {
            if (this.initialized.compareAndSet(false, true)) {
                this.value = orThrow(loader.get(), new InternalRuntimeException(MANAGED_VALUE_IS_NULL));
                erase(initializeConsumers, consumer -> consumer.accept(value));
            }
        }
        return this.value;
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
        if (!(other instanceof DisposableProperty)) {
            return false;
        }
        return get().equals(((DisposableProperty<?>) other).get());
    }

    public static <T> DisposableProperty<T> disposable(Supplier<T> factory) {
        return new DisposableProperty<>(factory);
    }

    public static <T> DisposableProperty<T> disposable(Supplier<T> factory, Consumer<T> disposer) {
        return new DisposableProperty<>(factory).disposed(disposer);
    }
}
