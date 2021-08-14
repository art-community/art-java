package io.art.meta.registry;

import io.art.meta.model.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.searcher.ClassMapSearcher.*;
import static java.util.Objects.*;
import java.util.*;

public class CustomMetaTypeMutableRegistry {
    private final static Map<Class<?>, MetaType<?>> REGISTRY = map();
    private final static Map<Class<?>, MetaType<?>> CACHE = map();

    public static void clear() {
        REGISTRY.clear();
    }

    public static MetaType<?> get(Class<?> type) {
        MetaType<?> cached = CACHE.get(type);
        if (nonNull(cached)) return cached;
        CACHE.put(type, cached = searchByClass(REGISTRY, type));
        return cached;
    }

    public static void register(MetaType<?> metaType) {
        REGISTRY.put(metaType.type(), metaType);
    }
}
