/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.http.configuration;

import io.art.communicator.configuration.*;
import io.art.communicator.refresher.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.http.refresher.*;
import io.art.server.configuration.*;
import io.art.server.refresher.*;
import lombok.*;
import static io.art.communicator.configuration.CommunicatorConfiguration.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.http.configuration.HttpConnectorConfiguration.*;
import static io.art.http.configuration.HttpServerConfiguration.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static io.art.server.configuration.ServerConfiguration.*;
import static io.art.server.constants.ServerConstants.ConfigurationKeys.*;
import static java.util.Optional.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class HttpModuleConfiguration implements ModuleConfiguration {
    private final HttpModuleRefresher refresher;
    private final ServerRefresher serverRefresher;
    private final CommunicatorRefresher communicatorRefresher;

    @Getter
    private HttpModuleRefresher.Consumer consumer;

    @Getter
    private ServerConfiguration server;

    @Getter
    private boolean enableServer;

    @Getter
    private HttpServerConfiguration httpServer;

    @Getter
    private CommunicatorConfiguration communicator;

    @Getter
    private ImmutableMap<String, HttpConnectorConfiguration> connectors;

    public HttpModuleConfiguration(HttpModuleRefresher refresher) {
        this.refresher = refresher;
        serverRefresher = new ServerRefresher();
        communicatorRefresher = new CommunicatorRefresher();
        consumer = refresher.consumer();

        server = serverConfiguration(serverRefresher);
        enableServer = false;
        httpServer = httpServerConfiguration();

        communicator = communicatorConfiguration(communicatorRefresher);
        connectors = emptyImmutableMap();
    }

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<HttpModuleConfiguration, Configurator> {
        private final HttpModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            Optional<NestedConfiguration> serverSection = ofNullable(source.getNested(HTTP_SECTION))
                    .map(http -> http.getNested(SERVER_SECTION));
            serverSection
                    .map(this::httpServer)
                    .ifPresent(server -> configuration.httpServer = server);
            serverSection
                    .map(this::server)
                    .ifPresent(server -> configuration.server = server);

            Optional<NestedConfiguration> communicatorSection = ofNullable(source.getNested(HTTP_SECTION))
                    .map(http -> http.getNested(COMMUNICATOR_SECTION));
            communicatorSection
                    .map(this::connectors)
                    .ifPresent(connectors -> configuration.connectors = merge(configuration.connectors, connectors));
            communicatorSection
                    .map(this::communicator)
                    .ifPresent(communicator -> configuration.communicator = communicator);

            configuration.refresher.produce();
            configuration.serverRefresher.produce();
            configuration.communicatorRefresher.produce();
            return this;
        }

        private ServerConfiguration server(NestedConfiguration server) {
            return serverConfiguration(configuration.serverRefresher, configuration.server, server);
        }

        private HttpServerConfiguration httpServer(NestedConfiguration server) {
            return httpServerConfiguration(configuration.refresher, configuration.httpServer, server);
        }


        private CommunicatorConfiguration communicator(NestedConfiguration communicator) {
            return communicatorConfiguration(configuration.communicatorRefresher, configuration.communicator, communicator);
        }

        private ImmutableMap<String, HttpConnectorConfiguration> connectors(NestedConfiguration communicator) {
            return communicator.getNestedMap(CONNECTORS_SECTION, nested -> connector(nested, configuration.connectors.get(nested.getParent())));
        }

        private HttpConnectorConfiguration connector(NestedConfiguration nested, HttpConnectorConfiguration current) {
            return httpConnectorConfiguration(configuration.refresher, current, nested);
        }


        @Override
        public Configurator initialize(HttpModuleConfiguration configuration) {
            this.configuration.enableServer = configuration.isEnableServer();

            this.configuration.server = configuration.getServer();
            this.configuration.httpServer = configuration.getHttpServer();

            this.configuration.communicator = configuration.getCommunicator();
            this.configuration.connectors = configuration.getConnectors();
            return this;
        }
    }

}
