package io.art.configurator.custom;

import io.art.configurator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.core.source.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.property.Property.*;
import static java.util.Objects.*;
import java.util.*;

@ForGenerator
public class CustomConfigurationRegistry {
    private final Map<CustomConfigurationModel, CustomConfigurator<?>> configurators = map();

    public ImmutableMap<CustomConfigurationModel, Property<?>> configure(ImmutableArray<ConfigurationSource> sources) {
        Map<CustomConfigurationModel, Property<?>> configurations = mapOf(configurators.size());
        for (ConfigurationSource source : sources) {
            for (Map.Entry<CustomConfigurationModel, CustomConfigurator<?>> entry : configurators.entrySet()) {
                if (isNotEmpty(entry.getKey().getSection())) {
                    NestedConfiguration nested = source.getNested(entry.getKey().getSection());
                    if (nonNull(nested)) {
                        configurations.put(entry.getKey(), cast(property(() -> entry.getValue().configure(nested))));
                    }
                    continue;
                }
                configurations.put(entry.getKey(), cast(property(() -> entry.getValue().configure(source))));
            }
        }
        return immutableMapOf(configurations);
    }

    public CustomConfigurationRegistry register(ImmutableSet<CustomConfigurationModel> models, CustomConfigurator<?> configurator) {
        models.forEach(model -> configurators.put(model, configurator));
        return this;
    }
}
