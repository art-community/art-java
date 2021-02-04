package io.art.core.lazy;

import io.art.core.exception.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.constants.ExceptionMessages.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@RequiredArgsConstructor
public class ManagedValue<T> implements Supplier<T> {
    private volatile T value;
    private final AtomicBoolean initialized = new AtomicBoolean();
    private final Supplier<T> loader;

    public ManagedValue<T> initialize() {
        get();
        return this;
    }

    public boolean initialized() {
        return initialized.get();
    }

    public boolean disposed() {
        return !initialized();
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

    public void dispose() {
        dispose(emptyConsumer());
    }

    public static <T> ManagedValue<T> managed(Supplier<T> factory) {
        return new ManagedValue<>(factory);
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
        if (!(other instanceof ManagedValue)) {
            return false;
        }
        return get().equals(((ManagedValue<?>) other).get());
    }
}
