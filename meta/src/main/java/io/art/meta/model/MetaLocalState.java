package io.art.meta.model;

import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.Meta.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class MetaLocalState<T> {
    private final Map<Key, T> state = concurrentMap();

    public void remove(MetaClass<?> keyClass, MetaMethod<?> method) {
        state.remove(new Key(keyClass.definition().type(), method));
    }

    public void set(MetaClass<?> keyClass, MetaMethod<?> method, T value) {
        state.put(new Key(keyClass.definition().type(), method), value);
    }

    public <C, M extends MetaClass<C>> T get(Class<C> keyClass, Function<M, MetaMethod<?>> methodSelector) {
        return state.get(new Key(keyClass, methodSelector.apply(cast(declaration(keyClass)))));
    }

    public <C, M extends MetaClass<C>> void remove(Class<C> keyClass, Function<M, MetaMethod<?>> methodSelector) {
        state.remove(new Key(keyClass, methodSelector.apply(cast(declaration(keyClass)))));
    }

    public <C, M extends MetaClass<C>> void set(Class<C> keyClass, Function<M, MetaMethod<?>> methodSelector, T value) {
        state.put(new Key(keyClass, methodSelector.apply(cast(declaration(keyClass)))), value);
    }

    public <C, M extends MetaClass<C>> boolean contains(Class<C> keyClass, Function<M, MetaMethod<?>> methodSelector) {
        return nonNull(get(keyClass, methodSelector));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    private static class Key {
        final Class<?> keyClass;
        final MetaMethod<?> keyMethod;
    }
}
