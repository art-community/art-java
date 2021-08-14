package io.art.meta.registry;

import io.art.meta.model.*;
import static io.art.core.factory.MapFactory.*;
import static java.util.Objects.*;
import java.util.*;

public class CustomMetaTypeMutableRegistry {
    private final static Map<Class<?>, MetaType<?>> REGISTRY = map();

    public static void clear() {
        REGISTRY.clear();
    }

    public static MetaType<?> get(Class<?> type) {
        MetaType<?> metaType = REGISTRY.get(type);
        if (nonNull(metaType)) return metaType;

        for (Class<?> typeInterface : type.getInterfaces()) {
            metaType = REGISTRY.get(typeInterface);
            if (nonNull(metaType)) return metaType;
        }

        return null;
    }

    public static void register(MetaType<?> metaType) {
        REGISTRY.put(metaType.type(), metaType);
    }
}
