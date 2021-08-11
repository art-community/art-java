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

import io.art.core.context.*;
import io.art.core.module.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.manager.*;
import io.art.rsocket.meta.*;
import io.art.rsocket.refresher.*;
import io.art.rsocket.state.*;
import lombok.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.context.Context.*;
import static io.art.meta.registry.MetaLibraryMutableRegistry.*;
import static io.art.rsocket.configuration.RsocketModuleConfiguration.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Hooks.*;

@Getter
public class RsocketModule implements StatefulModule<RsocketModuleConfiguration, Configurator, RsocketModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatefulModuleProxy<RsocketModuleConfiguration, RsocketModuleState> rsocketModule = context().getStatefulModule(RsocketModule.class.getSimpleName());
    private final String id = RsocketModule.class.getSimpleName();
    private final RsocketModuleState state = new RsocketModuleState();
    private final RsocketModuleRefresher refresher = new RsocketModuleRefresher();
    private final RsocketModuleConfiguration configuration = new RsocketModuleConfiguration(refresher);
    private final RsocketManager manager = new RsocketManager(refresher, configuration);
    private final Configurator configurator = new Configurator(configuration);

    public static StatefulModuleProxy<RsocketModuleConfiguration, RsocketModuleState> rsocketModule() {
        return getRsocketModule();
    }

    @Override
    public void load(Context.Service contextService) {
        withMeta(() -> registerMetaLibrary(MetaRsocket.class, MetaRsocket::new));
    }

    @Override
    public void launch(Context.Service contextService) {
        onErrorDropped(emptyConsumer());
        if (configuration.isEnableTcpServer() || configuration.isEnableHttpServer()) {
            manager.initializeServer();
        }
        if (!configuration.getHttpConnectors().isEmpty() || !configuration.getTcpConnectors().isEmpty()) {
            manager.initializeCommunicators();
        }
    }

    @Override
    public void shutdown(Context.Service contextService) {
        if (configuration.isEnableTcpServer() || configuration.isEnableHttpServer()) {
            manager.disposeServer();
        }
        if (!configuration.getHttpConnectors().isEmpty() || !configuration.getTcpConnectors().isEmpty()) {
            manager.disposeCommunicators();
        }
    }
}
