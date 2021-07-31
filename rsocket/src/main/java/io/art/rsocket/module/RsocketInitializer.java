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

package io.art.rsocket.module;

import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.refresher.*;
import io.art.server.method.*;
import io.art.server.registrator.*;
import lombok.*;
import java.util.function.*;

public class RsocketInitializer implements ModuleInitializer<RsocketModuleConfiguration, RsocketModuleConfiguration.Configurator, RsocketModule> {
    private boolean activateServer;
    private boolean activateCommunicator;
    private final RsocketServerConfigurator serverConfigurator = new RsocketServerConfigurator();

    public RsocketInitializer activateCommunicator() {
        activateCommunicator = true;
        return this;
    }

    public RsocketInitializer server(Function<RsocketServerConfigurator, ? extends ServerConfigurator> registrator) {
        activateServer = true;
        registrator.apply(serverConfigurator);
        return this;
    }

    @Override
    public RsocketModuleConfiguration initialize(RsocketModule module) {
        Initial initial = new Initial(module.getRefresher());
        initial.activateCommunicator = activateCommunicator;
        initial.activateServer = activateServer;
        initial.serverTransportConfiguration = serverConfigurator.configure(initial.serverTransportConfiguration);
        initial.serviceMethodProviders = serverConfigurator.serviceMethods();
        return initial;
    }

    @Getter
    public static class Initial extends RsocketModuleConfiguration {
        private boolean activateServer;
        private boolean activateCommunicator;
        private RsocketServerConfiguration serverTransportConfiguration = super.getServerTransportConfiguration();
        private LazyProperty<ImmutableArray<ServiceMethod>> serviceMethodProviders = super.getServiceMethodProviders();

        public Initial(RsocketModuleRefresher refresher) {
            super(refresher);
        }
    }
}
