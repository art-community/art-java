package io.art.core.singleton;

import io.art.core.property.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.property.LazyProperty.*;
import java.util.*;
import java.util.function.*;

public class SingletonsRegistry {
    private static final Map<Supplier<?>, LazyProperty<?>> SINGLETONS_BY_FUNCTION = concurrentMap();
    private static final Map<Class<?>, LazyProperty<?>> SINGLETONS_BY_CLASS = concurrentMap();

    public static <T> T singleton(Supplier<T> factory) {
        return cast(putIfAbsent(SINGLETONS_BY_FUNCTION, factory, () -> lazy(factory)).get());
    }

    public static <T> T singleton(Class<?> objectClass, Supplier<T> factory) {
        return cast(putIfAbsent(SINGLETONS_BY_CLASS, objectClass, () -> lazy(factory)).get());
    }
}
