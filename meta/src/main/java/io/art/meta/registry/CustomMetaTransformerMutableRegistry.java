package io.art.meta.registry;

import io.art.meta.transformer.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.searcher.ClassSearcher.*;
import static java.util.Objects.*;
import java.util.*;

public class CustomMetaTransformerMutableRegistry {
    private final static Map<Class<?>, CustomTransformers> REGISTRY = map();
    private final static Map<Class<?>, CustomTransformers> CACHE = map();

    public static void clear() {
        REGISTRY.clear();
    }

    public static CustomTransformers get(Class<?> type) {
        CustomTransformers cached = CACHE.get(type);
        if (nonNull(cached)) return cached;
        CACHE.put(type, cached = searchByClass(REGISTRY, type));
        return cached;
    }

    public static void register(Class<?> type, CustomTransformers transformers) {
        REGISTRY.put(type, transformers);
    }
}
