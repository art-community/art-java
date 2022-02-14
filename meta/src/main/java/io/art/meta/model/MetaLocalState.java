package io.art.meta.model;

import static io.art.core.factory.MapFactory.*;
import static java.util.Objects.*;
import java.util.*;

public class MetaLocalState<T> {
    private final Map<MetaMethod<?>, T> state = concurrentMap();

    public void remove(MetaMethod<?> method) {
        state.remove(method);
    }

    public T get(MetaMethod<?> method) {
        return state.get(method);
    }

    public void set(MetaMethod<?> method, T value) {
        state.put(method, value);
    }

    public boolean contains(MetaMethod<?> method) {
        return nonNull(get(method));
    }
}
