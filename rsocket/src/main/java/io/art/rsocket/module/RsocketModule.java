/*
 * ART
 *
 * Copyright 2020 ART
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

import io.art.core.module.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.state.*;
import lombok.*;
import org.apache.logging.log4j.*;
import static io.art.core.context.Context.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.configuration.RsocketModuleConfiguration.*;
import static io.art.rsocket.manager.RsocketManager.*;
import static lombok.AccessLevel.*;

@Getter
public class RsocketModule implements StatefulModule<RsocketModuleConfiguration, Configurator, RsocketModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatefulModuleProxy<RsocketModuleConfiguration, RsocketModuleState> rsocketModule = context().getStatefulModule(RsocketModule.class.getSimpleName());
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(RsocketModule.class);
    private final String id = RsocketModule.class.getSimpleName();
    private final RsocketModuleConfiguration configuration = new RsocketModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    private final RsocketModuleState state = new RsocketModuleState();

    public static StatefulModuleProxy<RsocketModuleConfiguration, RsocketModuleState> rsocketModule() {
        return getRsocketModule();
    }

    @Override
    public void onLoad() {
        if (configuration.isActivateCommunicator()) {
            initializeCommunicators();
        }
        if (configuration.isActivateServer()) {
            initializeServer();
        }
    }

    @Override
    public void beforeReload() {
        disposeCommunicators();
        disposeServer();
    }

    @Override
    public void afterReload() {
        if (configuration.isActivateCommunicator()) {
            initializeCommunicators();
        }
        if (configuration.isActivateServer()) {
            initializeServer();
        }
    }

    @Override
    public void onUnload() {
        disposeCommunicators();
        disposeServer();
    }
}
