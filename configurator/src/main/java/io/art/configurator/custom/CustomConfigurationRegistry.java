package io.art.configurator.custom;

import io.art.core.annotation.*;
import io.art.core.source.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

@UsedByGenerator
public class CustomConfigurationRegistry {
    private final Map<Class<?>, CustomConfigurationProxy<?>> proxies = map();
    private final Map<Class<?>, ?> configurations = map();

    public CustomConfigurationRegistry configure(ConfigurationSource source) {
        proxies.forEach((key, value) -> configurations.put(key, cast(value.configure(source))));
        return this;
    }

    public CustomConfigurationRegistry register(Class<?> modelClass, CustomConfigurationProxy<?> proxy) {
        proxies.put(modelClass, proxy);
        return this;
    }

    public <T> T getConfiguration(Class<T> modelClass) {
        return cast(configurations.get(modelClass));
    }
}
