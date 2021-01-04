package io.art.configurator.custom;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.source.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

@UsedByGenerator
public class CustomConfigurationRegistry {
    private final Map<Class<?>, CustomConfigurationProxy<?>> proxies = map();

    public ImmutableMap<Class<?>, ?> configure(ImmutableArray<ConfigurationSource> sources) {
        Map<Class<?>, ?> configurations = mapOf(proxies.size());
        for (ConfigurationSource source : sources) {
            proxies.forEach((key, proxy) -> configurations.put(key, cast(proxy.configure(source))));
        }
        return immutableMapOf(configurations);
    }

    public CustomConfigurationRegistry register(Class<?> modelClass, CustomConfigurationProxy<?> proxy) {
        proxies.put(modelClass, proxy);
        return this;
    }
}
