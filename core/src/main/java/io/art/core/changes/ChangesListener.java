package io.art.core.changes;

import io.art.core.local.*;
import io.art.core.property.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.local.ThreadLocalValue.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

@NoArgsConstructor(access = PRIVATE)
public class ChangesListener {
    private final ThreadLocalValue<Integer> index = threadLocal(() -> 0);
    private final ReentrantLock lock = new ReentrantLock();
    private final AtomicBoolean created = new AtomicBoolean();
    private final AtomicBoolean pending = new AtomicBoolean();
    private final List<Runnable> changeConsumers = copyOnWriteList();
    private final List<Runnable> disposeConsumers = copyOnWriteList();
    private final List<Object> values = copyOnWriteList();

    @Getter
    @Accessors(fluent = true)
    private final ChangesConsumer consumer = new ChangesConsumer(this);

    @Getter
    @Accessors(fluent = true)
    private final ChangesProducer producer = new ChangesProducer(this);

    public ChangesListener lock() {
        lock.lock();
        return this;
    }

    public ChangesListener unlock() {
        lock.unlock();
        return this;
    }

    public ChangesListener produce() {
        if (values.isEmpty()) return this;

        if (created.compareAndSet(false, true)) {
            return this;
        }

        if (pending.compareAndSet(true, false)) {
            changeConsumers.forEach(Runnable::run);
        }

        index.set(0);
        return this;
    }

    public ChangesListener dispose(){
        values.clear();
        pending.set(false);
        created.set(false);
        disposeConsumers.forEach(Runnable::run);
        return this;
    }

    public ChangesListener consume(Runnable action) {
        return consume(action, action);
    }

    public ChangesListener consume(Runnable onChange, Runnable onDispose) {
        changeConsumers.add(onChange);
        disposeConsumers.add(onDispose);
        return this;
    }

    public <T> Property<T> consume(Property<T> property) {
        changeConsumers.add(property::refresh);
        disposeConsumers.add(property::dispose);
        return property;
    }

    public <T> T emit(T value) {
        int index = this.index.get();
        if (!created.get()) {
            values.add(value);
            this.index.set(index + 1);
            return value;
        }
        Object current = values.get(index);
        if (!pending.get() && !Objects.equals(value, current)) {
            values.set(index, value);
            pending.set(true);
        }
        this.index.set(index + 1);
        return cast(value);
    }

    public static ChangesListener changesListener() {
        return new ChangesListener();
    }
}
