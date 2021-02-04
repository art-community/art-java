package io.art.core.module;

import io.art.core.collection.*;
import static io.art.core.collection.ImmutableMap.*;

public class ModuleInitializerRegistry {
    private final ImmutableMap.Builder<ModuleFactory<?>, ModuleDecorator<?>> MODULES = immutableMapBuilder();

    public <T extends Module> ModuleInitializerRegistry put(ModuleFactory<T> factory, ModuleDecorator<T> decorator) {
        MODULES.put(factory, decorator);
        return this;
    }

    public ImmutableMap<ModuleFactory<?>, ModuleDecorator<?>> get() {
        return MODULES.build();
    }
}
