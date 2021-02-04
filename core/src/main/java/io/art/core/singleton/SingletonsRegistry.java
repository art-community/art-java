package io.art.core.singleton;

import io.art.core.managed.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.managed.LazyValue.*;
import java.util.*;
import java.util.function.*;

public class SingletonsRegistry {
    private static final Map<Supplier<?>, LazyValue<?>> SINGLETONS_BY_FUNCTION = concurrentMap();
    private static final Map<Class<?>, LazyValue<?>> SINGLETONS_BY_CLASS = concurrentMap();

    public static <T> T singleton(Supplier<T> factory) {
        return cast(putIfAbsent(SINGLETONS_BY_FUNCTION, factory, () -> lazy(factory)).get());
    }

    public static <T> T singleton(Class<?> objectClass, Supplier<T> factory) {
        return cast(putIfAbsent(SINGLETONS_BY_CLASS, objectClass, () -> lazy(factory)).get());
    }
}
