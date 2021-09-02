package io.art.meta.registry;

import io.art.meta.transformer.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.searcher.ClassSearcher.*;
import static java.util.Objects.*;
import java.util.*;

public class CustomMetaTransformerMutableRegistry {
    private final Map<Class<?>, CustomTransformers> registry = map();
    private final Map<Class<?>, CustomTransformers> cache = map();

    public CustomTransformers get(Class<?> type) {
        CustomTransformers cached = cache.get(type);
        if (nonNull(cached)) return cached;
        cache.put(type, cached = searchByClass(registry, type));
        return cached;
    }

    public CustomMetaTransformerMutableRegistry register(Class<?> type, CustomTransformers transformers) {
        registry.put(type, transformers);
        return this;
    }
}
