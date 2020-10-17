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
import io.art.core.printer.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.launcher.*;
import io.art.rsocket.state.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.printer.ColoredPrinter.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.configuration.RsocketModuleConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static java.util.Objects.*;
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
    private final RsocketManager manager = new RsocketManager(configuration, state);

    public static StatefulModuleProxy<RsocketModuleConfiguration, RsocketModuleState> rsocketModule() {
        return getRsocketModule();
    }

    @Override
    public void afterLoad() {
        if (nonNull(configuration.getCommunicatorConfiguration())) {
            manager.startConnectors();
        }
        if (nonNull(configuration.getServerConfiguration())) {
            manager.startSever();
        }
    }

    @Override
    public void beforeUnload() {
        manager.stopSever();
        manager.stopConnectors();
    }

    @Override
    public String print() {
        ColoredPrinter printer = printer()
                .additional("RSocket configuration:")
                .tabulation(1)
                .additional(SERVER_SECTION)
                .tabulation(2)
                .additional(DEFAULT_DATA_FORMAT_KEY, configuration.getServerConfiguration().getDefaultDataFormat())
                .additional(DEFAULT_META_DATA_FORMAT_KEY, configuration.getServerConfiguration().getDefaultMetaDataFormat())
                .additional(DEFAULT_SERVICE_ID_KEY + SPACE + AMPERSAND + SPACE + DEFAULT_METHOD_ID_KEY, configuration.getServerConfiguration().getDefaultServiceMethod())
                .additional(FRAGMENTATION_MTU_KEY, configuration.getServerConfiguration().getFragmentationMtu())
                .additional(MAX_INBOUND_PAYLOAD_SIZE_KEY, configuration.getServerConfiguration().getMaxInboundPayloadSize())
                .additional(TRANSPORT_MODE_KEY, configuration.getServerConfiguration().getTransport())
                .additional("tcpServer", let(configuration.getServerConfiguration().getTcpServer(), TcpServer::configure));
        return printer.print();
    }
}
