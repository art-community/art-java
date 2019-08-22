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
    private String serverHost = super.getServerHost();
    private int serverTcpPort = super.getServerTcpPort();
    private int serverWebSocketPort = super.getServerWebSocketPort();
    private String balancerHost;
    private int balancerTcpPort;
    private int balancerWebSocketPort;
    private Map<String, RsocketCommunicationTargetConfiguration> communicationTargets;
    private boolean enableRawDataTracing;
    private boolean enableValueTracing;
    private boolean resumableServer;
    private long serverResumeSessionDuration;
    private boolean resumableClient;
    private long clientResumeSessionDuration;

    public RsocketAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        resumableServer = configBoolean(RSOCKET_SECTION_ID, RESUMABLE, super.isResumableServer());
        serverResumeSessionDuration = configLong(RSOCKET_SECTION_ID, RESUME_SESSION_DURATION, super.getServerResumeSessionDuration());
        resumableClient = configBoolean(RSOCKET_COMMUNICATION_SECTION_ID, RESUMABLE, super.isResumableClient());
        clientResumeSessionDuration = configLong(RSOCKET_COMMUNICATION_SECTION_ID, RESUME_SESSION_DURATION, super.getClientResumeSessionDuration());
        enableRawDataTracing = configBoolean(RSOCKET_SECTION_ID, ENABLE_RAW_DATA_TRACING, super.isEnableRawDataTracing());
        enableValueTracing = configBoolean(RSOCKET_SECTION_ID, ENABLE_VALUE_TRACING, super.isEnableValueTracing());
        dataFormat = ifException(() -> RsocketDataFormat.valueOf(configString(RSOCKET_SECTION_ID, DATA_FORMAT).toUpperCase()), super.getDataFormat());
        String newAcceptorHost = configString(RSOCKET_SERVER_SECTION_ID, HOST, super.getServerHost());
        boolean restart = !serverHost.equals(newAcceptorHost);
        serverHost = newAcceptorHost;
        int newAcceptorTcpPort = configInt(RSOCKET_SERVER_SECTION_ID, TCP_PORT, super.getServerTcpPort());
        restart |= serverTcpPort != newAcceptorTcpPort;
        serverTcpPort = newAcceptorTcpPort;
        int newAcceptorWebSocketPort = configInt(RSOCKET_SERVER_SECTION_ID, WEB_SOCKET_PORT, super.getServerWebSocketPort());
        restart |= serverWebSocketPort != newAcceptorWebSocketPort;
        serverWebSocketPort = newAcceptorWebSocketPort;
        balancerHost = configString(RSOCKET_BALANCER_SECTION_ID, HOST, super.getBalancerHost());
        balancerTcpPort = configInt(RSOCKET_BALANCER_SECTION_ID, TCP_PORT, super.getBalancerTcpPort());
        balancerWebSocketPort = configInt(RSOCKET_BALANCER_SECTION_ID, WEB_SOCKET_PORT, super.getBalancerWebSocketPort());
        communicationTargets = configMap(RSOCKET_COMMUNICATION_SECTION_ID, TARGETS, config -> rsocketCommunicationTarget()
                .host(ifEmpty(config.getString(HOST), balancerHost))
                .tcpPort(getOrElse(config.getInt(TCP_PORT), balancerTcpPort))
                .webSocketPort(getOrElse(config.getInt(WEB_SOCKET_PORT), balancerWebSocketPort))
                .dataFormat(super.getDataFormat())
                .resumable(config.getBool(RESUMABLE))
                .resumeSessionDuration(config.getLong(RESUME_SESSION_DURATION))
                .build(), super.getCommunicationTargets());
        if (restart && context().hasModule(RSOCKET_MODULE_ID)) {
            rsocketModuleState().getServer().restart();
        }
    }
}