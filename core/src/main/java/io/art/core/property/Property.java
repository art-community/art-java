package io.art.core.property;

import io.art.core.changes.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.QueueFactory.*;
import static io.art.core.property.LazyProperty.*;
import static java.util.Arrays.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class Property<T> implements Supplier<T> {
    private volatile DisposableProperty<T> disposable;
    private final Consumer<T> disposer;
    private volatile List<Predicate<T>> predicates;
    private volatile List<Consumer<T>> changeConsumers;
    private volatile List<Consumer<T>> disposeConsumers;
    private volatile Queue<Consumer<T>> creationConsumers;
    private volatile List<Consumer<T>> initializationConsumers;

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


    public Property<T> created(Consumer<T> consumer) {
        if (isNull(creationConsumers)) creationConsumers = queue();
        creationConsumers.add(consumer);
        disposable.created(consumer);
        return this;
    }

    public Property<T> initialized(Consumer<T> consumer) {
        if (isNull(initializationConsumers)) initializationConsumers = linkedList();
        initializationConsumers.add(consumer);
        disposable.initialized(consumer);
        return this;
    }

    public Property<T> disposed(Consumer<T> consumer) {
        if (isNull(disposeConsumers)) disposeConsumers = linkedList();
        disposeConsumers.add(consumer);
        disposable.disposed(consumer);
        return this;
    }

    public Property<T> changed(Consumer<T> consumer) {
        if (isNull(changeConsumers)) changeConsumers = linkedList();
        changeConsumers.add(consumer);
        return this;
    }

    public Property<T> prevent(Predicate<T> predicate) {
        if (isNull(predicates)) predicates = linkedList();
        predicates.add(predicate);
        return this;
    }

    public Property<T> listenProperties(Property<?>... others) {
        stream(others).forEach(other -> other.disposed(value -> dispose()).changed(value -> refresh()));
        return this;
    }

    public Property<T> listenConsumer(Supplier<ChangesConsumer> consumer) {
        return created(ignore -> consumer.get().consume(this));
    }

    public Property<T> listen(Supplier<ChangesListener> listener) {
        return created(ignore -> listener.get().consume(this));
    }


    public Property<T> set(T newValue) {
        if (let(predicates, predicates -> predicates.stream().anyMatch(predicate -> predicate.test(newValue)), false)) {
            return this;
        }
        if (Objects.equals(disposable.get(), newValue)) {
            return this;
        }
        disposable = DisposableProperty.disposable(() -> newValue, disposer);
        apply(creationConsumers, consumers -> consumers.forEach(disposable::created));
        apply(initializationConsumers, consumers -> consumers.forEach(disposable::initialized));
        apply(disposeConsumers, consumers -> consumers.forEach(disposable::disposed));
        apply(changeConsumers, consumers -> consumers.forEach(consumer -> consumer.accept(newValue)));
        return this;
    }

    public void refresh() {
        dispose();
        T value = get();
        apply(changeConsumers, consumers -> consumers.forEach(consumer -> consumer.accept(value)));
    }

    public T value() {
        return disposable.value();
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
        return deepEquals(get(), ((Property<?>) other).get());
    }


    public static <T> Property<T> property(Supplier<T> loader) {
        return new Property<>(loader, emptyConsumer());
    }

    public static <T> Property<T> property(Supplier<T> loader, Consumer<T> disposer) {
        return new Property<>(loader, disposer);
    }
}
