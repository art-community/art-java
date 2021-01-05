package io.art.configurator.custom;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.source.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

@UsedByGenerator
public class CustomConfigurationRegistry {
    private final Map<Class<?>, CustomConfigurator<?>> configurators = map();

    public ImmutableMap<Class<?>, ?> configure(ImmutableArray<ConfigurationSource> sources) {
        Map<Class<?>, ?> configurations = mapOf(configurators.size());
        for (ConfigurationSource source : sources) {
            configurators.forEach((key, proxy) -> configurations.put(key, cast(proxy.configure(source))));
        }
        return immutableMapOf(configurations);
    }

    public CustomConfigurationRegistry register(Class<?> modelClass, CustomConfigurator<?> configurator) {
        configurators.put(modelClass, configurator);
        return this;
    }
}
