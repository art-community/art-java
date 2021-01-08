package io.art.core.lazy;

import io.art.core.annotation.*;
import io.art.core.exception.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.ExceptionMessages.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@UsedByGenerator
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

    @Override
    public T get() {
        while (isNull(this.value)) {
            if (this.initialized.compareAndSet(false, true)) {
                this.value = orThrow(loader.get(), new InternalRuntimeException(MANAGED_VALUE_IS_NULL));
            }
        }
        return this.value;
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
