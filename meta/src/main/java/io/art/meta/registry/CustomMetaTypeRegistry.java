package io.art.meta.registry;

import io.art.core.collection.*;
import io.art.meta.model.*;
import lombok.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.searcher.ClassSearcher.*;
import static java.util.Objects.*;
import java.util.*;

@RequiredArgsConstructor
public class CustomMetaTypeRegistry {
    private final ImmutableMap<Class<?>, MetaType<?>> registry;
    private final Map<Class<?>, MetaType<?>> cache = concurrentMap();

    public MetaType<?> get(Class<?> type) {
        MetaType<?> cached = cache.get(type);
        if (nonNull(cached)) return cached;
        cached = searchByClass(registry.toMutable(), type);
        if (isNull(cached)) return cached;
        cache.put(type, cached);
        return cached;
    }
}
