package io.art.meta.registry;

import io.art.meta.transformer.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

public class CustomTransformerMutableRegistry {
    private final static Map<Class<?>, CustomTransformers> REGISTRY = map();

    public static void clear() {
        REGISTRY.clear();
    }

    public static CustomTransformers get(Class<?> type) {
        return REGISTRY.get(type);
    }

    public static void register(Class<?> type, CustomTransformers transformers) {
        REGISTRY.put(type, transformers);
    }
}
