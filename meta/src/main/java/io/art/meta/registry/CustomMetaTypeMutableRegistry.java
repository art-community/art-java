package io.art.meta.registry;

import io.art.meta.model.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

public class CustomMetaTypeMutableRegistry {
    private final static Map<Class<?>, MetaType<?>> REGISTRY = map();

    public static void clear() {
        REGISTRY.clear();
    }

    public static MetaType<?> get(Class<?> type) {
        return REGISTRY.get(type);
    }

    public static void register(MetaType<?> metaType) {
        REGISTRY.put(metaType.type(), metaType);
    }
}
