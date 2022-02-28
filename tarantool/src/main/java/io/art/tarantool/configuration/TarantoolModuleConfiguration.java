package io.art.tarantool.configuration;

import io.art.communicator.configuration.*;
import io.art.communicator.refresher.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.server.configuration.*;
import io.art.server.refresher.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.refresher.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.storage.*;
import lombok.*;
import static io.art.communicator.configuration.CommunicatorConfiguration.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.collector.SetCollector.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.server.configuration.ServerConfiguration.*;
import static io.art.tarantool.configuration.TarantoolStorageConfiguration.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.*;

public class TarantoolModuleConfiguration implements ModuleConfiguration {
    private final TarantoolModuleRefresher refresher;
    private final CommunicatorRefresher communicatorRefresher;
    private final ServerRefresher serverRefresher;

    @Getter
    private ImmutableMap<String, TarantoolStorageConfiguration> storageConfigurations;

    @Getter
    private ImmutableMap<String, TarantoolStorage> storages;

    @Getter
    private TarantoolServiceRegistry services;

    @Getter
    private TarantoolSubscriptionRegistry subscriptions;

    @Getter
    private final TarantoolModelWriter writer = new TarantoolModelWriter();

    @Getter
    private final TarantoolModelReader reader = new TarantoolModelReader();

    @Getter
    private CommunicatorConfiguration communicator;

    @Getter
    private ServerConfiguration server;

    @Getter
    private boolean logging;

    public TarantoolModuleConfiguration(TarantoolModuleRefresher refresher) {
        this.refresher = refresher;
        communicatorRefresher = new CommunicatorRefresher();
        serverRefresher = new ServerRefresher();
        communicator = communicatorConfiguration(communicatorRefresher);
        server = serverConfiguration(serverRefresher);
        storageConfigurations = emptyImmutableMap();
        storages = emptyImmutableMap();
        services = new TarantoolServiceRegistry(lazy(ImmutableMap::emptyImmutableMap), lazy(ImmutableMap::emptyImmutableMap));
        subscriptions = new TarantoolSubscriptionRegistry(lazy(ImmutableMap::emptyImmutableMap));
    }

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TarantoolModuleConfiguration, Configurator> {
        private final TarantoolModuleConfiguration configuration;

        @Override
        public Configurator initialize(TarantoolModuleConfiguration configuration) {
            this.configuration.communicator = configuration.getCommunicator();
            this.configuration.server = configuration.getServer();
            this.configuration.storageConfigurations = configuration.getStorageConfigurations();
            this.configuration.storages = configuration.getStorages();
            this.configuration.services = configuration.getServices();
            this.configuration.subscriptions = configuration.getSubscriptions();
            return this;
        }

        @Override
        public Configurator from(ConfigurationSource source) {
            ConfigurationSource tarantoolSection = source.getNested(TARANTOOL_SECTION);
            if (isNull(tarantoolSection)) return this;

            configuration.storageConfigurations = tarantoolSection.getNestedMap(
                    TARANTOOL_CLUSTERS_SECTION,
                    clusterConfig -> tarantoolStorageConfiguration(clusterConfig, configuration.refresher)
            );

            configuration.logging = orElse(tarantoolSection.getBoolean(TARANTOOL_LOGGING_KEY), configuration.logging);

            configuration.refresher.clusterListeners().update(configuration.storageConfigurations.keySet());

            configuration.refresher.clientListeners().update(
                    configuration.storageConfigurations.keySet()
                            .stream()
                            .flatMap(connector -> configuration.storageConfigurations.get(connector)
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
            configuration.serverRefresher.produce();

            return this;
        }

        private CommunicatorConfiguration communicator(NestedConfiguration communicator) {
            return communicatorConfiguration(configuration.communicatorRefresher, configuration.communicator, communicator);
        }
    }
}
