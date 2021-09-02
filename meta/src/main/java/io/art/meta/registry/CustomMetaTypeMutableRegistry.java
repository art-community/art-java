package io.art.meta.registry;

import io.art.meta.model.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.searcher.ClassSearcher.*;
import static java.util.Objects.*;
import java.util.*;

public class CustomMetaTypeMutableRegistry {
    private final Map<Class<?>, MetaType<?>> registry = map();
    private final Map<Class<?>, MetaType<?>> cache = map();

    public MetaType<?> get(Class<?> type) {
        MetaType<?> cached = cache.get(type);
        if (nonNull(cached)) return cached;
        cache.put(type, cached = searchByClass(registry, type));
        return cached;
    }

    public CustomMetaTypeMutableRegistry register(MetaType<?> metaType) {
        registry.put(metaType.type(), metaType);
        return this;
    }
}
