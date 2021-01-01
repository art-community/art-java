package io.art.core.singleton;

import io.art.core.lazy.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.lazy.LazyValue.*;
import java.util.*;
import java.util.function.*;

public class SingletonsRegistry {
    private final static Map<Supplier<?>, LazyValue<?>> SINGLETONS_BY_FUNCTION = concurrentHashMap();
    private final static Map<Class<?>, LazyValue<?>> SINGLETONS_BY_CLASS = concurrentHashMap();

    public static <T> T singleton(Supplier<T> factory) {
        return cast(putIfAbsent(SINGLETONS_BY_FUNCTION, factory, () -> lazy(factory)).get());
    }

    public static <T> T singleton(Class<?> objectClass, Supplier<T> factory) {
        return cast(putIfAbsent(SINGLETONS_BY_CLASS, objectClass, () -> lazy(factory)).get());
    }
}
