package io.art.core.property;

import io.art.core.changes.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.property.LazyProperty.*;
import static java.util.Arrays.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class Property<T> implements Supplier<T> {
    private volatile DisposableProperty<T> disposable;
    private final Consumer<T> disposer;
    private final List<Consumer<T>> changeConsumers = linkedList();
    private final List<Consumer<T>> clearConsumers = linkedList();
    private final List<Predicate<T>> predicates = linkedList();

    public Property(Supplier<T> loader, Consumer<T> disposer) {
        disposable = DisposableProperty.disposable(loader, disposer);
        this.disposer = disposer;
    }


    public LazyProperty<T> immutable() {
        return lazy(disposable);
    }

    public DisposableProperty<T> disposable() {
        return DisposableProperty.disposable(disposable);
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

    public void dispose() {
        disposable.dispose();
    }


    public Property<T> initialized(Consumer<T> consumer) {
        disposable.initialized(consumer);
        return this;
    }

    public Property<T> disposed(Consumer<T> consumer) {
        disposable.disposed(consumer);
        return this;
    }

    public Property<T> changed(Consumer<T> consumer) {
        changeConsumers.add(consumer);
        return this;
    }

    public Property<T> cleared(Consumer<T> consumer) {
        clearConsumers.add(consumer);
        return this;
    }

    public Property<T> prevent(Predicate<T> predicate) {
        predicates.add(predicate);
        return this;
    }

    public Property<T> listenProperties(Property<?>... others) {
        stream(others).forEach(other -> other
                .initialized(value -> initialize())
                .cleared(value -> clear())
                .changed(value -> refresh()));
        return this;
    }

    public Property<T> listenConsumer(Supplier<ChangesConsumer> listener) {
        return initialized(ignore -> listener.get().consume(this));
    }

    public Property<T> listen(Supplier<ChangesListener> listener) {
        return initialized(ignore -> listener.get().consume(this));
    }


    public Property<T> set(T newValue) {
        if (predicates.stream().anyMatch(predicate -> predicate.test(newValue))) {
            return this;
        }
        if (Objects.equals(disposable.get(), newValue)) {
            return this;
        }
        disposable = DisposableProperty.disposable(() -> newValue, disposer);
        changeConsumers.forEach(consumer -> consumer.accept(newValue));
        return this;
    }

    public Property<T> clear() {
        clearConsumers.forEach(consumer -> consumer.accept(get()));
        dispose();
        return this;
    }

    public void refresh() {
        dispose();
        T value = get();
        changeConsumers.forEach(consumer -> consumer.accept(value));
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
        if (!(other instanceof Property)) {
            return false;
        }
        return get().equals(((Property<?>) other).get());
    }


    public static <T> Property<T> property(Supplier<T> loader) {
        return new Property<>(loader, emptyConsumer());
    }

    public static <T> Property<T> property(Supplier<T> loader, Consumer<T> disposer) {
        return new Property<>(loader, disposer);
    }
}
