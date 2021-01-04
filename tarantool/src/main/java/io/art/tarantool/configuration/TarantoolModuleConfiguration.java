package io.art.tarantool.configuration;

import com.google.common.collect.*;
import io.art.core.module.*;
import io.art.core.source.*;
import lombok.*;
import static com.google.common.collect.ImmutableMap.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.*;


public class TarantoolModuleConfiguration implements ModuleConfiguration {
    public Map<String, TarantoolClusterConfiguration> clusters = map();
    public boolean logging = false;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator> {
        private final TarantoolModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            ConfigurationSource tarantoolSection = source.getNested(TARANTOOL_SECTION);
            if (isNull(tarantoolSection)) return this;

            configuration.clusters = ofNullable(tarantoolSection)
                    .map(section -> section.getNestedMap(TARANTOOL_CLUSTERS_SECTION))
                    .map(clusters -> clusters.entrySet()
                        .stream()
                        .collect(toImmutableMap(Map.Entry::getKey, entry -> TarantoolClusterConfiguration.from(entry.getValue()))))
                    .orElse(ImmutableMap.of());

            configuration.logging = orElse(tarantoolSection.getBool(TARANTOOL_LOGGING_KEY), configuration.logging);
            return this;
        }
    }
}
