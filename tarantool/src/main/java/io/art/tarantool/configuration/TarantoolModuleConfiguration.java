package io.art.tarantool.configuration;

import io.art.core.collection.ImmutableMap;
import io.art.core.module.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static java.util.Objects.*;


public class TarantoolModuleConfiguration implements ModuleConfiguration {
    public ImmutableMap<String, TarantoolClusterConfiguration> clusters;
    public boolean logging = false;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator> {
        private final TarantoolModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            ConfigurationSource tarantoolSection = source.getNested(TARANTOOL_SECTION);
            if (isNull(tarantoolSection)) return this;

            configuration.clusters = tarantoolSection.getNestedMap(TARANTOOL_CLUSTERS_SECTION, TarantoolClusterConfiguration::from);

            configuration.logging = orElse(tarantoolSection.getBool(TARANTOOL_LOGGING_KEY), configuration.logging);
            return this;
        }
    }
}
