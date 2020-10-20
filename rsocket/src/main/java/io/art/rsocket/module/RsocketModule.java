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
import io.art.rsocket.manager.*;
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
    public void onLoad() {
        if (nonNull(configuration.getCommunicatorConfiguration())) {
            manager.startConnectors();
        }
        if (nonNull(configuration.getServerConfiguration())) {
            manager.startSever();
        }
    }

    @Override
    public void onUnload() {
        manager.stopSever();
        manager.stopConnectors();
    }

    @Override
    public String print() {
        RsocketServerConfiguration serverConfiguration = configuration.getServerConfiguration();
        RsocketCommunicatorConfiguration communicatorConfiguration = configuration.getCommunicatorConfiguration();
        if (isNull(serverConfiguration) && isNull(communicatorConfiguration)) {
            return EMPTY_STRING;
        }
        ColoredPrinter printer = printer()
                .mainSection(RsocketModule.class.getSimpleName())
                .tabulation(1);
        if (nonNull(serverConfiguration)) {
            printer.subSection(SERVER_SECTION)
                    .tabulation(2)
                    .value(DEFAULT_DATA_FORMAT_KEY, serverConfiguration.getDefaultDataFormat())
                    .value(DEFAULT_META_DATA_FORMAT_KEY, serverConfiguration.getDefaultMetaDataFormat())
                    .value(DEFAULT_SERVICE_ID_KEY + SPACE + AMPERSAND + SPACE + DEFAULT_METHOD_ID_KEY, serverConfiguration.getDefaultServiceMethod())
                    .value(FRAGMENTATION_MTU_KEY, serverConfiguration.getFragmentationMtu())
                    .value(MAX_INBOUND_PAYLOAD_SIZE_KEY, serverConfiguration.getMaxInboundPayloadSize())
                    .value(TRANSPORT_MODE_KEY, serverConfiguration.getTransport())
                    .value("tcpServer", let(serverConfiguration.getTcpServer(), TcpServer::configure))
                    .value("server available", manager.isServerAvailable());
        }
        if (nonNull(communicatorConfiguration)) {
            printer
                    .tabulation(1)
                    .subSection(COMMUNICATOR_SECTION)
                    .tabulation(2)
                    .value(DEFAULT_DATA_FORMAT_KEY, communicatorConfiguration.getDefaultDataFormat())
                    .value(DEFAULT_META_DATA_FORMAT_KEY, communicatorConfiguration.getDefaultMetaDataFormat())
                    .value(DEFAULT_SERVICE_ID_KEY + SPACE + AMPERSAND + SPACE + DEFAULT_METHOD_ID_KEY, communicatorConfiguration.getDefaultServiceMethod())
                    .value(FRAGMENTATION_MTU_KEY, communicatorConfiguration.getFragmentationMtu())
                    .value(MAX_INBOUND_PAYLOAD_SIZE_KEY, communicatorConfiguration.getMaxInboundPayloadSize());
        }
        return printer.print();
    }
}
