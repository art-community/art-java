package io.art.core.singleton;

import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;
import java.util.function.*;

public class SingletonsRegistry {
    private static final Map<Class<?>, ?> SINGLETONS_BY_CLASS = concurrentMap();

    public static <T> T singleton(Class<?> objectClass, Supplier<T> factory) {
        return cast(putIfAbsent(SINGLETONS_BY_CLASS, objectClass, cast(factory)));
    }
}
