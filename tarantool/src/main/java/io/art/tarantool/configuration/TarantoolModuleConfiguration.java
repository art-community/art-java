package io.art.tarantool.configuration;

import io.art.communicator.configuration.*;
import io.art.communicator.refresher.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.refresher.*;
import lombok.*;
import static io.art.communicator.configuration.CommunicatorConfiguration.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.collector.SetCollector.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.tarantool.configuration.TarantoolConnectorConfiguration.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.*;

public class TarantoolModuleConfiguration implements ModuleConfiguration {
    private final TarantoolModuleRefresher refresher;
    private final CommunicatorRefresher communicatorRefresher;

    @Getter
    private ImmutableMap<String, TarantoolConnectorConfiguration> connectors;

    @Getter
    private final TarantoolModelWriter writer = new TarantoolModelWriter();

    @Getter
    private final TarantoolModelReader reader = new TarantoolModelReader();

    @Getter
    private CommunicatorConfiguration communicator;

    @Getter
    private boolean logging;

    public TarantoolModuleConfiguration(TarantoolModuleRefresher refresher) {
        this.refresher = refresher;
        communicatorRefresher = new CommunicatorRefresher();
        communicator = communicatorConfiguration(communicatorRefresher);
        connectors = emptyImmutableMap();
    }

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator> {
        private final TarantoolModuleConfiguration configuration;

        @Override
        public Configurator initialize(TarantoolModuleConfiguration configuration) {
            this.configuration.communicator = configuration.getCommunicator();
            this.configuration.connectors = configuration.getConnectors();
            return this;
        }

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
                            .flatMap(connector -> configuration.connectors.get(connector)
                                    .getClients()
                                    .stream()
                                    .map(tarantoolInstanceConfiguration -> connector + COLON + tarantoolInstanceConfiguration))
                            .collect(setCollector())
            );

            Optional<NestedConfiguration> communicatorSection = ofNullable(source.getNested(TARANTOOL_SECTION))
                    .map(rsocket -> rsocket.getNested(COMMUNICATOR_SECTION));

            communicatorSection
                    .map(this::communicator)
                    .ifPresent(communicator -> configuration.communicator = communicator);

            configuration.refresher.produce();
            configuration.communicatorRefresher.produce();

            return this;
        }

        private CommunicatorConfiguration communicator(NestedConfiguration communicator) {
            return communicatorConfiguration(configuration.communicatorRefresher, configuration.communicator, communicator);
        }
    }
}
