package io.art.meta.model;

import static io.art.core.factory.MapFactory.*;
import static java.util.Objects.*;
import java.util.*;

public class MetaLocalState<T> {
    private final Map<MetaMethod<MetaClass<?>, ?>, T> state = concurrentMap();

    public void remove(MetaMethod<MetaClass<?>, ?> method) {
        state.remove(method);
    }

    public T get(MetaMethod<MetaClass<?>, ?> method) {
        return state.get(method);
    }

    public void set(MetaMethod<MetaClass<?>, ?> method, T value) {
        state.put(method, value);
    }

    public boolean contains(MetaMethod<MetaClass<?>, ?> method) {
        return nonNull(get(method));
    }
}
