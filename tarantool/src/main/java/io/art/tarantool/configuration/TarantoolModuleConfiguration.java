package io.art.tarantool.configuration;

import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static java.util.Objects.*;


public class TarantoolModuleConfiguration implements ModuleConfiguration {
    public ImmutableMap<String, TarantoolInstanceConfiguration> instances = emptyImmutableMap();
    public boolean enableTracing = false;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator> {
        private final TarantoolModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            ConfigurationSource tarantoolSection = source.getNested(TARANTOOL_SECTION);
            if (isNull(tarantoolSection)) return this;

            configuration.instances = tarantoolSection.getNestedMap(TARANTOOL_INSTANCES_SECTION, TarantoolInstanceConfiguration::from);

            configuration.enableTracing = orElse(tarantoolSection.getBool(TARANTOOL_TRACING_KEY), configuration.enableTracing);
            return this;
        }
    }
}
