package io.art.tarantool.configuration;

import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.tarantool.module.refresher.*;
import lombok.*;

import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collector.SetCollector.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static java.util.Objects.*;

@RequiredArgsConstructor
public class TarantoolModuleConfiguration implements ModuleConfiguration {
    private final TarantoolModuleRefresher refresher;
    public ImmutableMap<String, TarantoolClusterConfiguration> clusters;
    public boolean logging = false;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator> {
        private final TarantoolModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            ConfigurationSource tarantoolSection = source.getNested(TARANTOOL_SECTION);
            if (isNull(tarantoolSection)) return this;


            configuration.clusters = tarantoolSection.getNestedMap(TARANTOOL_CLUSTERS_SECTION, clusterConfig ->
                    TarantoolClusterConfiguration.from(clusterConfig, configuration.refresher));
            configuration.logging = orElse(tarantoolSection.getBoolean(TARANTOOL_LOGGING_KEY), configuration.logging);


            configuration.refresher.clusterListeners().update(configuration.clusters.keySet());
            configuration.refresher.clientListeners().update(
                    configuration.clusters.keySet().stream()
                            .flatMap(clusterId -> configuration.clusters.get(clusterId).instances.keySet().stream()
                                    .map(tarantoolInstanceConfiguration -> clusterId + COLON + tarantoolInstanceConfiguration))
                            .collect(setCollector())
            );
            configuration.refresher.produce();


            return this;
        }
    }
}
