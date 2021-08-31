package io.art.meta.registry;

import io.art.meta.transformer.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.searcher.ClassSearcher.*;
import static java.util.Objects.*;
import java.util.*;

public class CustomMetaTransformerMutableRegistry {
    private final static Map<Class<?>, CustomTransformers> registry = map();
    private final static Map<Class<?>, CustomTransformers> cache = map();

    public static void clear() {
        registry.clear();
    }

    public static CustomTransformers get(Class<?> type) {
        CustomTransformers cached = cache.get(type);
        if (nonNull(cached)) return cached;
        cache.put(type, cached = searchByClass(registry, type));
        return cached;
    }

    public static void register(Class<?> type, CustomTransformers transformers) {
        registry.put(type, transformers);
    }
}
