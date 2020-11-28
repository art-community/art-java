package io.art.refactored.configuration;

import io.art.core.module.ModuleConfiguration;
import io.art.core.module.ModuleConfigurator;
import io.art.core.source.ConfigurationSource;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import static io.art.refactored.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static java.util.Optional.*;
import static io.art.core.factory.CollectionsFactory.mapOf;
import static io.art.core.checker.NullityChecker.*;


public class TarantoolModuleConfiguration implements ModuleConfiguration {
    public Map<String, TarantoolInstanceConfiguration> instances = mapOf();
    public boolean enableTracing;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator>{
        private final TarantoolModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            ofNullable(source.getNested(TARANTOOL_SECTION))
                    .map(tarantool -> tarantool.getNested(TARANTOOL_INSTANCES_SECTION))
                    .ifPresent(instance -> configuration.instances.put(instance.getSection(), TarantoolInstanceConfiguration.from(instance)));
            configuration.enableTracing = orElse(source.getNested(TARANTOOL_SECTION).getBool(TARANTOOL_TRACING_KEY), false);
            return this;
        }
    }
}
/*
tarantool_refactored:


*/