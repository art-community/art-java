/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.rsocket.configuration;

import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.core.source.*;
import io.art.rsocket.refresher.*;
import io.art.server.configuration.*;
import io.art.server.method.*;
import io.art.server.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static java.util.Optional.*;

@RequiredArgsConstructor
public class RsocketModuleConfiguration implements ModuleConfiguration {
    private final RsocketModuleRefresher refresher;
    private final ServerRefresher serverRefresher;

    @Getter
    private RsocketModuleRefresher.Consumer consumer;

    @Getter
    private ServerConfiguration serverConfiguration;

    @Getter
    private RsocketServerConfiguration serverTransportConfiguration;

    @Getter
    private ImmutableArray<LazyProperty<ServiceMethod>> serviceMethodProviders;

    @Getter
    private RsocketCommunicatorConfiguration communicatorConfiguration;

    @Getter
    private boolean activateServer;

    @Getter
    private boolean activateCommunicator;

    public RsocketModuleConfiguration(RsocketModuleRefresher refresher) {
        this.refresher = refresher;
        serverRefresher = refresher.serverRefresher();
        consumer = refresher.consumer();
        serverConfiguration = ServerConfiguration.builder().refresher(serverRefresher).build();
        serverTransportConfiguration = RsocketServerConfiguration.defaults();
        serviceMethodProviders = emptyImmutableArray();
        activateServer = false;
        activateCommunicator = false;
    }

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<RsocketModuleConfiguration, Configurator> {
        private final RsocketModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            ofNullable(source.getNested(RSOCKET_SECTION))
                    .map(rsocket -> rsocket.getNested(SERVER_SECTION))
                    .map(server -> RsocketServerConfiguration.from(configuration.refresher, server))
                    .ifPresent(serverConfiguration -> configuration.serverTransportConfiguration = serverConfiguration);
            ofNullable(source.getNested(RSOCKET_SECTION))
                    .map(rsocket -> rsocket.getNested(COMMUNICATOR_SECTION))
                    .map(communicator -> RsocketCommunicatorConfiguration.from(configuration.refresher, communicator))
                    .ifPresent(communicatorConfiguration -> configuration.communicatorConfiguration = communicatorConfiguration);
            configuration.serverTransportConfiguration = orElse(configuration.serverTransportConfiguration, RsocketServerConfiguration::defaults);

            configuration.refresher.produce();
            return this;
        }

        @Override
        public Configurator initialize(RsocketModuleConfiguration configuration) {
            this.configuration.activateCommunicator = configuration.isActivateCommunicator();
            this.configuration.activateServer = configuration.isActivateServer();
            this.configuration.serverTransportConfiguration = configuration.getServerTransportConfiguration();
            this.configuration.serverConfiguration = configuration.getServerConfiguration();
            this.configuration.serviceMethodProviders = configuration.getServiceMethodProviders();
            return this;
        }
    }

}
