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

import io.art.communicator.configuration.*;
import io.art.communicator.proxy.*;
import io.art.communicator.refresher.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.core.source.*;
import io.art.rsocket.configuration.communicator.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.refresher.*;
import io.art.server.configuration.*;
import io.art.server.method.*;
import io.art.server.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static java.util.Optional.*;
import java.util.*;

@RequiredArgsConstructor
public class RsocketModuleConfiguration implements ModuleConfiguration {
    private final RsocketModuleRefresher refresher;
    private final ServerRefresher serverRefresher;
    private final CommunicatorRefresher communicatorRefresher;

    @Getter
    private RsocketModuleRefresher.Consumer consumer;

    @Getter
    private ServerConfiguration serverConfiguration;

    @Getter
    private RsocketTcpServerConfiguration tcpServerConfiguration;

    @Getter
    private RsocketHttpServerConfiguration httpServerConfiguration;

    @Getter
    private LazyProperty<ImmutableArray<ServiceMethod>> serviceMethodProvider;

    @Getter
    private LazyProperty<ImmutableMap<Class<?>, CommunicatorProxy<?>>> communicatorProxyProvider;

    @Getter
    private CommunicatorConfiguration communicatorConfiguration;

    @Getter
    private ImmutableMap<String, RsocketTcpConnectorConfiguration> tcpConnectorConfigurations;

    @Getter
    private ImmutableMap<String, RsocketHttpConnectorConfiguration> httpConnectorConfigurations;

    @Getter
    private boolean enableTcpServer;

    @Getter
    private boolean enableHttpServer;

    public RsocketModuleConfiguration(RsocketModuleRefresher refresher) {
        this.refresher = refresher;
        serverRefresher = new ServerRefresher();
        communicatorRefresher = new CommunicatorRefresher();
        consumer = refresher.consumer();
        serverConfiguration = ServerConfiguration.defaults(serverRefresher);
        tcpServerConfiguration = RsocketTcpServerConfiguration.defaults();
        httpServerConfiguration = RsocketHttpServerConfiguration.defaults();
        serviceMethodProvider = lazy(ImmutableArray::emptyImmutableArray);
        enableTcpServer = false;
        enableHttpServer = false;
        tcpConnectorConfigurations = emptyImmutableMap();
        httpConnectorConfigurations = emptyImmutableMap();
        communicatorProxyProvider = lazy(ImmutableMap::emptyImmutableMap);
        communicatorConfiguration = CommunicatorConfiguration.defaults(communicatorRefresher);
    }

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<RsocketModuleConfiguration, Configurator> {
        private final RsocketModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            Optional<NestedConfiguration> serverSection = ofNullable(source.getNested(RSOCKET_SECTION))
                    .map(rsocket -> rsocket.getNested(SERVER_SECTION));
            serverSection
                    .map(server -> RsocketTcpServerConfiguration.from(configuration.refresher, configuration.tcpServerConfiguration, server))
                    .ifPresent(serverConfiguration -> configuration.tcpServerConfiguration = serverConfiguration);
            serverSection
                    .map(server -> RsocketHttpServerConfiguration.from(configuration.refresher, configuration.httpServerConfiguration, server))
                    .ifPresent(serverConfiguration -> configuration.httpServerConfiguration = serverConfiguration);
            serverSection
                    .map(server -> ServerConfiguration.from(configuration.serverRefresher, server))
                    .ifPresent(serverConfiguration -> configuration.serverConfiguration = serverConfiguration);

            Optional<NestedConfiguration> communicatorSection = ofNullable(source.getNested(RSOCKET_SECTION))
                    .map(rsocket -> rsocket.getNested(COMMUNICATOR_SECTION));
            communicatorSection
                    .map(this::getTcpConnectors)
                    .ifPresent(communicatorConfiguration -> configuration.tcpConnectorConfigurations = merge(configuration.tcpConnectorConfigurations, communicatorConfiguration));
            communicatorSection
                    .map(this::getHttpConnectors)
                    .ifPresent(communicatorConfiguration -> configuration.httpConnectorConfigurations = merge(configuration.httpConnectorConfigurations, communicatorConfiguration));
            communicatorSection
                    .map(communicator -> CommunicatorConfiguration.from(configuration.communicatorRefresher, communicator))
                    .ifPresent(communicatorConfiguration -> configuration.communicatorConfiguration = communicatorConfiguration);

            configuration.refresher.produce();
            configuration.serverRefresher.produce();
            configuration.communicatorRefresher.produce();
            return this;
        }

        private ImmutableMap<String, RsocketHttpConnectorConfiguration> getHttpConnectors(NestedConfiguration communicator) {
            return communicator.getNestedMap(CONNECTORS_KEY, nested -> let(configuration.httpConnectorConfigurations.get(nested.getSection()),
                    current -> RsocketHttpConnectorConfiguration.from(configuration.refresher, current, nested),
                    RsocketHttpConnectorConfiguration.from(configuration.refresher, nested)
            ));
        }

        private ImmutableMap<String, RsocketTcpConnectorConfiguration> getTcpConnectors(NestedConfiguration communicator) {
            return communicator.getNestedMap(CONNECTORS_KEY, nested -> let(configuration.tcpConnectorConfigurations.get(nested.getSection()),
                    current -> RsocketTcpConnectorConfiguration.from(configuration.refresher, current, nested),
                    RsocketTcpConnectorConfiguration.from(configuration.refresher, nested)
            ));
        }

        @Override
        public Configurator initialize(RsocketModuleConfiguration configuration) {
            this.configuration.enableTcpServer = configuration.isEnableTcpServer();
            this.configuration.enableHttpServer = configuration.isEnableHttpServer();
            this.configuration.serverConfiguration = configuration.getServerConfiguration();
            this.configuration.tcpServerConfiguration = configuration.getTcpServerConfiguration();
            this.configuration.httpServerConfiguration = configuration.getHttpServerConfiguration();
            this.configuration.serviceMethodProvider = configuration.getServiceMethodProvider();
            this.configuration.httpConnectorConfigurations = configuration.getHttpConnectorConfigurations();
            this.configuration.tcpConnectorConfigurations = configuration.getTcpConnectorConfigurations();
            this.configuration.communicatorConfiguration = configuration.getCommunicatorConfiguration();
            this.configuration.communicatorProxyProvider = configuration.getCommunicatorProxyProvider();
            return this;
        }
    }

}
