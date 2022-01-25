/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.http.module;

import io.art.communicator.configuration.*;
import io.art.communicator.configurator.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.http.configuration.*;
import io.art.http.refresher.*;
import io.art.server.configuration.*;
import io.art.server.configurator.*;
import lombok.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.http.module.HttpModule.*;
import java.util.function.*;

@Public
public class HttpInitializer implements ModuleInitializer<HttpModuleConfiguration, HttpModuleConfiguration.Configurator, HttpModule> {
    private boolean enableServer = false;
    private final HttpServerConfigurator serverConfigurator = new HttpServerConfigurator();
    private final HttpCommunicatorConfigurator communicatorConfigurator = new HttpCommunicatorConfigurator();

    public HttpInitializer server(Function<HttpServerConfigurator, ? extends ServerConfigurator<HttpServerConfigurator>> configurator) {
        enableServer = true;
        configurator.apply(serverConfigurator);
        return this;
    }

    public HttpInitializer communicator(Function<HttpCommunicatorConfigurator, ? extends CommunicatorConfigurator<HttpCommunicatorConfigurator>> configurator) {
        configurator.apply(communicatorConfigurator);
        return this;
    }

    @Override
    public HttpModuleConfiguration initialize(HttpModule module) {
        Initial initial = new Initial(module.getRefresher());

        initial.enableServer = enableServer;
        initial.httpServer = serverConfigurator.configure(initial.httpServer);
        initial.server = serverConfigurator.configureServer(lazy(() -> httpModule().configuration().getServer()), initial.server);

        initial.connectors = communicatorConfigurator.connectors();
        initial.communicator = communicatorConfigurator.configureCommunicator(lazy(() -> httpModule().configuration().getCommunicator()), initial.communicator);

        return initial;
    }

    @Getter
    public static class Initial extends HttpModuleConfiguration {
        private boolean enableServer = super.isEnableServer();
        private HttpServerConfiguration httpServer = super.getHttpServer();
        private ServerConfiguration server = super.getServer();

        private ImmutableMap<String, HttpConnectorConfiguration> connectors = super.getConnectors();
        private CommunicatorConfiguration communicator = super.getCommunicator();

        public Initial(HttpModuleRefresher refresher) {
            super(refresher);
        }
    }
}
