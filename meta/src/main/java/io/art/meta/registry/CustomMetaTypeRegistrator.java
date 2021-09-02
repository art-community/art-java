package io.art.meta.registry;

import io.art.core.collection.*;
import io.art.meta.model.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

public class CustomMetaTypeRegistrator {
    private final Map<Class<?>, MetaType<?>> registry = map();

    public CustomMetaTypeRegistrator register(MetaType<?> metaType) {
        registry.put(metaType.type(), metaType);
        return this;
    }

    public ImmutableMap<Class<?>, MetaType<?>> getRegistry() {
        return immutableMapOf(registry);
    }
}
