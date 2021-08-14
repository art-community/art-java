package io.art.meta.registry;

import io.art.meta.transformer.*;
import static io.art.core.factory.MapFactory.*;
import static java.util.Objects.*;
import java.util.*;

public class CustomTransformerMutableRegistry {
    private final static Map<Class<?>, CustomTransformers> REGISTRY = map();

    public static void clear() {
        REGISTRY.clear();
    }

    public static CustomTransformers get(Class<?> type) {
        CustomTransformers transformers = REGISTRY.get(type);
        if (nonNull(transformers)) return transformers;

        for (Class<?> typeInterface : type.getInterfaces()) {
            transformers = REGISTRY.get(typeInterface);
            if (nonNull(transformers)) return transformers;
        }

        return null;
    }

    public static void register(Class<?> type, CustomTransformers transformers) {
        REGISTRY.put(type, transformers);
    }
}
