package io.art.tarantool.configuration;

import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.tarantool.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collector.SetCollector.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.tarantool.configuration.TarantoolConnectorConfiguration.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static java.util.Objects.*;

@RequiredArgsConstructor
public class TarantoolModuleConfiguration implements ModuleConfiguration {
    private final TarantoolModuleRefresher refresher;
    public ImmutableMap<String, TarantoolConnectorConfiguration> connectors;
    public boolean logging = false;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator> {
        private final TarantoolModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            ConfigurationSource tarantoolSection = source.getNested(TARANTOOL_SECTION);
            if (isNull(tarantoolSection)) return this;

            configuration.connectors = tarantoolSection.getNestedMap(
                    TARANTOOL_CLUSTERS_SECTION,
                    clusterConfig -> tarantoolConnectorConfiguration(clusterConfig, configuration.refresher)
            );

            configuration.logging = orElse(tarantoolSection.getBoolean(TARANTOOL_LOGGING_KEY), configuration.logging);

            configuration.refresher.clusterListeners().update(configuration.connectors.keySet());

            configuration.refresher.clientListeners().update(
                    configuration.connectors.keySet()
                            .stream()
                            .flatMap(clusterId -> configuration.connectors.get(clusterId)
                                    .getClients()
                                    .keySet()
                                    .stream()
                                    .map(tarantoolInstanceConfiguration -> clusterId + COLON + tarantoolInstanceConfiguration))
                            .collect(setCollector())
            );

            configuration.refresher.produce();

            return this;
        }
    }
}
