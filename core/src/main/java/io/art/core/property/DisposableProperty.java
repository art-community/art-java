package io.art.core.property;

import io.art.core.exception.*;
import lombok.*;
import org.jctools.util.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.constants.Errors.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.QueueFactory.*;
import static java.util.Objects.*;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
@SuppressWarnings(SUN_API)
public class DisposableProperty<T> implements Supplier<T> {
    private static final Object UNINITIALIZED = new Object();
    private static final VarHandle VALUE_HANDLE;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            VALUE_HANDLE = lookup.findVarHandle(DisposableProperty.class, VALUE_FIELD_NAME, Object.class);
        } catch (Throwable throwable) {
            throw new InternalRuntimeException(throwable);
        }
    }

    @SuppressWarnings(FINAL_FIELD)
    private T value = cast(UNINITIALIZED);

    private final Supplier<T> loader;
    private volatile Queue<Consumer<T>> creationConsumers;
    private volatile List<Consumer<T>> initializationConsumers;
    private volatile List<Consumer<T>> disposeConsumers;

    public DisposableProperty<T> initialize() {
        get();
        return this;
    }


    public DisposableProperty<T> created(Consumer<T> consumer) {
        if (isNull(creationConsumers)) creationConsumers = queue();
        creationConsumers.add(consumer);
        return this;
    }

    public DisposableProperty<T> initialized(Consumer<T> consumer) {
        if (isNull(initializationConsumers)) initializationConsumers = linkedList();
        initializationConsumers.add(consumer);
        return this;
    }

    public DisposableProperty<T> disposed(Consumer<T> consumer) {
        if (isNull(disposeConsumers)) disposeConsumers = linkedList();
        disposeConsumers.add(consumer);
        return this;
    }


    public boolean initialized() {
        return value != UNINITIALIZED;
    }

    public boolean disposed() {
        return !initialized();
    }


    public void dispose() {
        T current = value;
        if (current == UNINITIALIZED) {
            return;
        }
        current = cast(VALUE_HANDLE.getVolatile(this));
        if (current == UNINITIALIZED) {
            return;
        }

        final T currentValue = current;
        if (VALUE_HANDLE.compareAndSet(this, current, UNINITIALIZED)) {
            apply(disposeConsumers, consumers -> consumers.forEach(consumer -> consumer.accept(currentValue)));
        }
    }

    @Override
    public T get() {
        T localValue = value;
        if (localValue == UNINITIALIZED) {
            localValue = cast(VALUE_HANDLE.getVolatile(this));
            if (localValue == UNINITIALIZED) {
                T loaded = orThrow(loader.get(), new InternalRuntimeException(PROPERTY_VALUE_IS_NULL));
                localValue = VALUE_HANDLE.compareAndSet(this, UNINITIALIZED, loaded)
                        ? loaded
                        : cast(VALUE_HANDLE.getVolatile(this));
                apply(creationConsumers, consumers -> erase(consumers, consumer -> consumer.accept(loaded)));
                apply(initializationConsumers, consumers -> consumers.forEach(consumer -> consumer.accept(loaded)));
            }
        }
        return localValue;
    }

    public T value() {
        return value;
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
        return deepEquals(get(), ((DisposableProperty<?>) other).get());
    }

    public static <T> DisposableProperty<T> disposable(Supplier<T> factory) {
        return new DisposableProperty<>(factory);
    }

    public static <T> DisposableProperty<T> disposable(Supplier<T> factory, Consumer<T> disposer) {
        return new DisposableProperty<>(factory).disposed(disposer);
    }
}
