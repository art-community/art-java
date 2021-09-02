package io.art.meta.registry;

import io.art.core.collection.*;
import io.art.meta.transformer.*;
import lombok.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.searcher.ClassSearcher.*;
import static java.util.Objects.*;
import java.util.*;

@RequiredArgsConstructor
public class CustomMetaTransformerRegistry {
    private final ImmutableMap<Class<?>, CustomTransformers> registry;
    private final Map<Class<?>, CustomTransformers> cache = map();

    public CustomTransformers get(Class<?> type) {
        CustomTransformers cached = cache.get(type);
        if (nonNull(cached)) return cached;
        cache.put(type, cached = searchByClass(registry.toMutable(), type));
        return cached;
    }
}

