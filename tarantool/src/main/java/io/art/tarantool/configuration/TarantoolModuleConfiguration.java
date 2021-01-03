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
    public Map<String, TarantoolInstanceConfiguration> instances = map();
    public boolean enableTracing = false;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator> {
        private final TarantoolModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            ConfigurationSource tarantoolSection = source.getNested(TARANTOOL_SECTION);
            if (isNull(tarantoolSection)) return this;

            configuration.instances = ofNullable(tarantoolSection.getNestedMap(TARANTOOL_INSTANCES_SECTION))
                    .map(instances -> instances.entrySet()
                            .stream()
                            .collect(toImmutableMap(Map.Entry::getKey, entry -> TarantoolInstanceConfiguration.from(entry.getValue()))))
                    .orElse(ImmutableMap.of());

            configuration.enableTracing = orElse(tarantoolSection.getBool(TARANTOOL_TRACING_KEY), configuration.enableTracing);
            return this;
        }
    }
}
