package io.art.core.singleton;

import io.art.core.lazy.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.lazy.LazyValue.*;
import java.util.*;
import java.util.function.*;

public class SingletonObject {
    private final static Map<Supplier<?>, LazyValue<?>> SINGLETONS = concurrentHashMap();

    public static <T> T singleton(Supplier<T> factory) {
        return cast(putIfAbsent(SINGLETONS, factory, () -> lazy(factory)).get());
    }
}
