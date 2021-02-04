package io.art.core.managed;

import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.managed.ManagedValue.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

@NoArgsConstructor(access = PRIVATE)
public class ChangesListener {
    private final AtomicInteger index = new AtomicInteger();
    private final ReentrantLock lock = new ReentrantLock();
    private final AtomicBoolean created = new AtomicBoolean();
    private final AtomicBoolean pending = new AtomicBoolean();
    private final List<Runnable> consumers = copyOnWriteList();
    private final List<ManagedValue<Object>> values = copyOnWriteList();

    public ChangesListener reset() {
        index.set(0);
        return this;
    }

    public ChangesListener lock() {
        lock.lock();
        return this;
    }

    public ChangesListener unlock() {
        lock.unlock();
        return this;
    }

    public ChangesListener produce() {
        if (created.compareAndSet(false, true)) {
            return this;
        }
        if (pending.compareAndSet(true, false)) {
            consumers.forEach(Runnable::run);
        }
        return this;
    }

    public ChangesListener consume(Runnable action) {
        consumers.add(action);
        return this;
    }

    public <T> ManagedValue<T> consume(ManagedValue<T> value) {
        consumers.add(value::change);
        return value;
    }

    public <T> T register(T value) {
        int index = this.index.getAndIncrement();
        if (values.size() > index) {
            return cast(values.get(index).set(value).get());
        }
        values.add(managed(() -> cast(value)).consume(ignore -> pending.set(created.get())));
        return value;
    }

    public static ChangesListener listener() {
        return new ChangesListener();
    }
}
