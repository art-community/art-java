package io.art.core.property;

import io.art.core.exception.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.constants.ExceptionMessages.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@RequiredArgsConstructor
public class DisposableProperty<T> implements Supplier<T> {
    private volatile T value;
    private final Supplier<T> loader;
    private final Consumer<T> disposer;
    private final AtomicBoolean initialized = new AtomicBoolean();


    public DisposableProperty<T> initialize() {
        get();
        return this;
    }


    public boolean initialized() {
        return initialized.get();
    }

    public boolean disposed() {
        return !initialized();
    }


    public void dispose() {
        dispose(disposer);
    }

    public void dispose(Consumer<T> action) {
        if (disposed()) return;
        while (nonNull(this.value)) {
            if (this.initialized.compareAndSet(true, false)) {
                T current = this.value;
                action.accept(current);
                this.value = null;
            }
        }
    }


    @Override
    public T get() {
        while (isNull(this.value)) {
            if (this.initialized.compareAndSet(false, true)) {
                this.value = orThrow(loader.get(), new InternalRuntimeException(MANAGED_VALUE_IS_NULL));
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
        return new DisposableProperty<>(factory, emptyConsumer());
    }

    public static <T> DisposableProperty<T> disposable(Supplier<T> factory, Consumer<T> disposer) {
        return new DisposableProperty<>(factory, disposer);
    }
}
