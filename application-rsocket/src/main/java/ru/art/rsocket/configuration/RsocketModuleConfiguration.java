/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.rsocket.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import ru.art.rsocket.exception.RsocketClientException;
import ru.art.rsocket.model.RsocketCommunicationTargetConfiguration;
import static java.text.MessageFormat.format;
import static ru.art.core.constants.NetworkConstants.BROADCAST_IP_ADDRESS;
import static ru.art.core.constants.NetworkConstants.LOCALHOST;
import static ru.art.core.extension.ExceptionExtensions.exceptionIfNull;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.core.network.selector.PortSelector.findAvailableTcpPort;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat.PROTOBUF;
import java.util.Map;

public interface RsocketModuleConfiguration extends ModuleConfiguration {
    String getAcceptorHost();

    int getAcceptorTcpPort();

    int getAcceptorWebSocketPort();

    String getBalancerHost();

    int getBalancerTcpPort();

    int getBalancerWebSocketPort();

    RsocketDataFormat getDefaultDataFormat();

    Map<String, RsocketCommunicationTargetConfiguration> getCommunicationTargets();

    default RsocketCommunicationTargetConfiguration getCommunicationTargetConfiguration(String serviceId) {
        return exceptionIfNull(getCommunicationTargets().get(serviceId),
                new RsocketClientException(format(RSOCKET_COMMUNICATION_TARGET_CONFIGURATION_NOT_FOUND, serviceId)))
                .toBuilder()
                .build();
    }

    @Getter
    class RsocketModuleDefaultConfiguration implements RsocketModuleConfiguration {
        private final RsocketDataFormat defaultDataFormat = PROTOBUF;
        private final String acceptorHost = BROADCAST_IP_ADDRESS;
        private final int acceptorTcpPort = findAvailableTcpPort();
        private final int acceptorWebSocketPort = findAvailableTcpPort();
        private final String balancerHost = LOCALHOST;
        private final int balancerTcpPort = DEFAULT_RSOCKET_TCP_PORT;
        private final int balancerWebSocketPort = DEFAULT_RSOCKET_WEB_SOCKET_PORT;
        private final Map<String, RsocketCommunicationTargetConfiguration> communicationTargets = mapOf();
    }
}
