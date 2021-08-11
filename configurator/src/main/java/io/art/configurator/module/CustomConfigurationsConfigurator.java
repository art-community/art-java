package io.art.configurator.module;

import io.art.configurator.model.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.core.source.*;
import static io.art.configurator.custom.CustomConfigurationParser.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.property.Property.*;
import static java.util.Objects.*;
import java.util.*;

public class CustomConfigurationsConfigurator {
    private final Set<CustomConfiguration> configurations = set();

    CustomConfigurationsConfigurator register(CustomConfiguration configuration) {
        configurations.add(configuration);
        return this;
    }

    ImmutableMap<CustomConfiguration, Property<?>> configure(ImmutableArray<ConfigurationSource> sources) {
        Map<CustomConfiguration, Property<?>> configurations = mapOf(this.configurations.size());
        for (ConfigurationSource source : sources) {
            for (CustomConfiguration configuration : this.configurations) {
                if (isNotEmpty(configuration.getSection())) {
                    NestedConfiguration nested = source.getNested(configuration.getSection());
                    if (nonNull(nested)) {
                        configurations.put(configuration, cast(property(() -> parse(configuration.getType(), source))));
                    }
                    continue;
                }
                configurations.put(configuration, cast(property(() -> parse(configuration.getType(), source))));
            }
        }
        return immutableMapOf(configurations);
    }
}
