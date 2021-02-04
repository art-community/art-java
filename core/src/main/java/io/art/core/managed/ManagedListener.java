package io.art.core.managed;

import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.managed.ManagedValue.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public class ManagedListener {
    private volatile int index = 0;
    private final AtomicBoolean pending = new AtomicBoolean();
    private final List<Runnable> consumers = copyOnWriteList();
    private final List<ManagedValue<Object>> values = copyOnWriteList();

    public void reset() {
        index = 0;
        pending.set(false);
    }

    public void produce() {
        if (pending.compareAndSet(true, false)) {
            consumers.forEach(Runnable::run);
        }
    }

    public ManagedListener consume(Runnable action) {
        consumers.add(action);
        return this;
    }

    public <T> ManagedValue<T> consume(ManagedValue<T> value) {
        consumers.add(value::change);
        return value;
    }

    public <T> T register(T value) {
        if (values.size() > index) {
            return cast(values.get(index).set(value).get());
        }
        values.add(managed(() -> cast(value)).consume(ignore -> pending.set(true)));
        pending.set(true);
        index = values.size();
        return value;
    }

    public static ManagedListener listener() {
        return new ManagedListener();
    }
}
