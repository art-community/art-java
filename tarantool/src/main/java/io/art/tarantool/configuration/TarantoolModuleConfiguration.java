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
import static io.art.core.factory.MapFactory.map;
import static io.art.core.checker.NullityChecker.*;


public class TarantoolModuleConfiguration implements ModuleConfiguration {
    public Map<String, TarantoolClusterConfiguration> clusters = map();
    public boolean enableTracing = false;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator>{
        private final TarantoolModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            ConfigurationSource tarantoolSection = source.getNested(TARANTOOL_SECTION);
            if (tarantoolSection == null) return this;

            configuration.clusters = ofNullable(tarantoolSection)
                    .map(section -> section.getNestedMap(TARANTOOL_CLUSTERS_SECTION))
                    .map(clusters -> clusters.entrySet()
                        .stream()
                        .collect(toImmutableMap(Map.Entry::getKey, entry -> TarantoolClusterConfiguration.from(entry.getValue()))))
                    .orElse(ImmutableMap.of());

            configuration.enableTracing = orElse(tarantoolSection.getBool(TARANTOOL_LOGGING_KEY), configuration.enableTracing);
            return this;
        }
    }
}
