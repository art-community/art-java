package io.art.meta.registry;

import io.art.core.collection.*;
import io.art.meta.transformer.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

public class CustomMetaTransformerRegistrator {
    private final Map<Class<?>, CustomTransformers> registry = map();

    public CustomMetaTransformerRegistrator register(Class<?> type, CustomTransformers transformers) {
        registry.put(type, transformers);
        return this;
    }

    public ImmutableMap<Class<?>, CustomTransformers> registry() {
        return immutableMapOf(registry);
    }
}
