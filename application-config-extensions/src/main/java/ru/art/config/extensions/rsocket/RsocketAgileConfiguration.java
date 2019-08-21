/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.config.extensions.rsocket;

import lombok.*;
import ru.art.rsocket.configuration.RsocketModuleConfiguration.*;
import ru.art.rsocket.model.*;
import java.util.*;

import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.rsocket.RsocketConfigKeys.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.extension.ExceptionExtensions.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.model.RsocketCommunicationTargetConfiguration.*;
import static ru.art.rsocket.module.RsocketModule.*;

@Getter
public class RsocketAgileConfiguration extends RsocketModuleDefaultConfiguration {
    private RsocketDataFormat dataFormat;
    private String acceptorHost = super.getAcceptorHost();
    private int acceptorTcpPort = super.getAcceptorTcpPort();
    private int acceptorWebSocketPort = super.getAcceptorWebSocketPort();
    private String balancerHost;
    private int balancerTcpPort;
    private int balancerWebSocketPort;
    private Map<String, RsocketCommunicationTargetConfiguration> communicationTargets;
    private boolean enableRawDataTracing;
    private boolean enableValueTracing;

    public RsocketAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        enableRawDataTracing = configBoolean(RSOCKET_SECTION_ID, ENABLE_RAW_DATA_TRACING, super.isEnableRawDataTracing());
        enableValueTracing = configBoolean(RSOCKET_SECTION_ID, ENABLE_VALUE_TRACING, super.isEnableValueTracing());
        dataFormat = ifException(() -> RsocketDataFormat.valueOf(configString(RSOCKET_SECTION_ID, DATA_FORMAT).toUpperCase()), super.getDataFormat());
        String newAcceptorHost = configString(RSOCKET_ACCEPTOR_SECTION_ID, HOST, super.getAcceptorHost());
        boolean restart = !acceptorHost.equals(newAcceptorHost);
        acceptorHost = newAcceptorHost;
        int newAcceptorTcpPort = configInt(RSOCKET_ACCEPTOR_SECTION_ID, TCP_PORT, super.getAcceptorTcpPort());
        restart |= acceptorTcpPort != newAcceptorTcpPort;
        acceptorTcpPort = newAcceptorTcpPort;
        int newAcceptorWebSocketPort = configInt(RSOCKET_ACCEPTOR_SECTION_ID, WEB_SOCKET_PORT, super.getAcceptorWebSocketPort());
        restart |= acceptorWebSocketPort != newAcceptorWebSocketPort;
        acceptorWebSocketPort = newAcceptorWebSocketPort;
        balancerHost = configString(RSOCKET_BALANCER_SECTION_ID, HOST, super.getBalancerHost());
        balancerTcpPort = configInt(RSOCKET_BALANCER_SECTION_ID, TCP_PORT, super.getBalancerTcpPort());
        balancerWebSocketPort = configInt(RSOCKET_BALANCER_SECTION_ID, WEB_SOCKET_PORT, super.getBalancerWebSocketPort());
        communicationTargets = configMap(RSOCKET_COMMUNICATION_SECTION_ID, TARGETS, config -> rsocketCommunicationTarget()
                .host(ifEmpty(config.getString(HOST), balancerHost))
                .tcpPort(getOrElse(config.getInt(TCP_PORT), balancerTcpPort))
                .webSocketPort(getOrElse(config.getInt(WEB_SOCKET_PORT), balancerWebSocketPort))
                .dataFormat(super.getDataFormat())
                .build(), super.getCommunicationTargets());
        if (restart && context().hasModule(RSOCKET_MODULE_ID)) {
            rsocketModuleState().getServer().restart();
        }
    }
}