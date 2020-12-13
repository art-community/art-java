package io.art.tarantool.configuration;

import com.google.common.collect.ImmutableMap;
import io.art.core.module.ModuleConfiguration;
import io.art.core.module.ModuleConfigurator;
import io.art.core.source.ConfigurationSource;
import lombok.RequiredArgsConstructor;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static java.util.Optional.*;
import static io.art.core.factory.CollectionsFactory.mapOf;
import static io.art.core.checker.NullityChecker.*;


public class TarantoolModuleConfiguration implements ModuleConfiguration {
    public Map<String, TarantoolInstanceConfiguration> instances = mapOf();
    public boolean enableTracing = false;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator>{
        private final TarantoolModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            ConfigurationSource tntSection = source.getNested(TARANTOOL_SECTION);
            if (tntSection == null) return this;

            configuration.instances = ofNullable(tntSection)
                    .map(tarantool -> tarantool.getNestedMap(TARANTOOL_INSTANCES_SECTION))
                    .map(instances -> instances.entrySet()
                        .stream()
                        .collect(toImmutableMap(Map.Entry::getKey, entry -> TarantoolInstanceConfiguration.from(entry.getValue()))))
                    .orElse(ImmutableMap.of());

            configuration.enableTracing = orElse(tntSection.getBool(TARANTOOL_TRACING_KEY), configuration.enableTracing);
            return this;
        }
    }
}
