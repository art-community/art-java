package io.art.core.managed;

import static io.art.core.factory.ListFactory.*;
import static io.art.core.managed.LazyValue.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class ManagedValue<T> implements Supplier<T> {
    private volatile DisposableValue<T> disposable;
    private final List<Consumer<T>> changeConsumers = linkedList();
    private final List<Consumer<T>> clearConsumers = linkedList();
    private final List<Predicate<T>> predicates = linkedList();

    public ManagedValue(Supplier<T> loader) {
        disposable = DisposableValue.disposable(loader);
    }

    public LazyValue<T> immutable() {
        return lazy(disposable);
    }

    public DisposableValue<T> disposable() {
        return disposable;
    }

    public void initialize() {
        get();
    }

    public boolean initialized() {
        return disposable.initialized();
    }

    public boolean disposed() {
        return disposable.disposed();
    }

    public ManagedValue<T> disposer(Consumer<T> disposer) {
        disposable.disposer(disposer);
        return this;
    }

    public void dispose(Consumer<T> action) {
        disposable.dispose(action);
    }

    public void dispose() {
        disposable.dispose();
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
        if (predicates.stream().anyMatch(predicate -> predicate.test(newValue))) {
            return this;
        }
        if (Objects.equals(disposable.get(), newValue)) {
            return this;
        }
        disposable = DisposableValue.disposable(() -> newValue);
        changeConsumers.forEach(consumer -> consumer.accept(newValue));
        return this;
    }

    public T produce(T newValue) {
        set(newValue);
        return newValue;
    }

    public ManagedValue<T> listen(ManagedValue<?>... others) {
        for (ManagedValue<?> other : others) {
            other.cleared(value -> clear()).consume(value -> change());
        }
        return this;
    }

    public ManagedValue<T> listen(ChangesListener listener) {
        return listener.consume(this);
    }

    public ManagedValue<T> clear() {
        clearConsumers.forEach(consumer -> consumer.accept(get()));
        dispose();
        return this;
    }

    public void change() {
        dispose();
        changeConsumers.forEach(consumer -> consumer.accept(get()));
    }

    @Override
    public T get() {
        return disposable.get();
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
}
