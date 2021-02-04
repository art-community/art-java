package io.art.core.managed;

import static io.art.core.factory.ListFactory.*;
import static io.art.core.managed.LazyValue.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class ManagedValue<T> {
    private volatile DisposableValue<T> disposable;
    private final List<Consumer<T>> changeConsumers = copyOnWriteList();
    private final List<Consumer<T>> clearConsumers = copyOnWriteList();
    private final List<Predicate<T>> predicates = copyOnWriteList();

    public ManagedValue(Supplier<T> loader) {
        disposable = DisposableValue.disposable(loader);
    }

    public T get() {
        return disposable.get();
    }

    public LazyValue<T> immutable() {
        return lazy(disposable);
    }

    public DisposableValue<T> disposable() {
        return disposable;
    }

    public void dispose(Consumer<T> action) {
        disposable.dispose(action);
    }

    public void dispose() {
        disposable.dispose();
    }

    public boolean initialized() {
        return disposable.initialized();
    }

    public boolean disposed() {
        return disposable.disposed();
    }

    public ManagedValue<T> consume(Consumer<T> consumer) {
        changeConsumers.add(consumer);
        return this;
    }

    public ManagedValue<T> cleared(Consumer<T> consumer) {
        clearConsumers.add(consumer);
        return this;
    }

    public ManagedValue<T> prevent(Predicate<T> predicate) {
        predicates.add(predicate);
        return this;
    }

    public ManagedValue<T> set(T newValue) {
        if (predicates.stream().anyMatch(predicate -> predicate.test(newValue)) || Objects.equals(disposable.get(), newValue)) {
            return this;
        }
        update(newValue);
        return this;
    }

    public T produce(T newValue) {
        set(newValue);
        return newValue;
    }

    public ManagedValue<T> clear() {
        disposable.dispose(disposed -> clearConsumers.forEach(consumer -> consumer.accept(disposed)));
        return this;
    }

    public ManagedValue<T> dependsOn(ManagedValue<?>... others) {
        for (ManagedValue<?> other : others) {
            other.cleared(value -> clear()).consume(value -> update(get()));
        }
        return this;
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

    public static <T> ManagedValue<T> managed(Supplier<T> loader) {
        return new ManagedValue<>(loader);
    }

    private void update(T newValue) {
        DisposableValue<T> current = this.disposable;
        this.disposable = DisposableValue.disposable(() -> newValue);
        current.dispose(disposed -> changeConsumers.forEach(consumer -> consumer.accept(newValue)));
    }
}
