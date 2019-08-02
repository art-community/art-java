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

package ru.art.config.extensions.rsocket;

import lombok.Getter;
import ru.art.rsocket.configuration.RsocketModuleConfiguration.RsocketModuleDefaultConfiguration;
import ru.art.rsocket.model.RsocketCommunicationTargetConfiguration;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.rsocket.RsocketConfigKeys.*;
import static ru.art.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.art.core.context.Context.context;
import static ru.art.core.extension.ExceptionExtensions.ifException;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_MODULE_ID;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.art.rsocket.model.RsocketCommunicationTargetConfiguration.rsocketCommunicationTarget;
import static ru.art.rsocket.module.RsocketModule.rsocketModuleState;
import java.util.Map;

@Getter
public class RsocketAgileConfiguration extends RsocketModuleDefaultConfiguration {
    private RsocketDataFormat dataFormat;
    private String acceptorHost = super.getAcceptorHost();
    private int acceptorTcpPort = super.getAcceptorTcpPort();
    private int acceptorWebSocketPort = super.getAcceptorWebSocketPort();
    private String balancerHost;
    private int balancerPort;
    private Map<String, RsocketCommunicationTargetConfiguration> communicationTargets;

    public RsocketAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        dataFormat = ifException(() -> RsocketDataFormat.valueOf(configString(RSOCKET_SECTION_ID, DEFAULT_DATA_FORMAT).toUpperCase()), super.getDefaultDataFormat());
        String newAcceptorHost = configString(RSOCKET_ACCEPTOR_SECTION_ID, HOST, super.getAcceptorHost());
        boolean restart = !acceptorHost.equals(newAcceptorHost);
        int newAcceptorTcpPort = configInt(RSOCKET_ACCEPTOR_SECTION_ID, RSOCKET_ACCEPTOR_TCP_PORT, super.getAcceptorTcpPort());
        restart |= acceptorTcpPort != newAcceptorTcpPort;
        int newAcceptorWebSocketPort = configInt(RSOCKET_ACCEPTOR_SECTION_ID, RSOCKET_ACCEPTOR_WEB_SOCKET_PORT, super.getAcceptorWebSocketPort());
        restart |= acceptorWebSocketPort != newAcceptorWebSocketPort;
        balancerHost = configString(RSOCKET_BALANCER_SECTION_ID, HOST, super.getBalancerHost());
        balancerPort = configInt(RSOCKET_BALANCER_SECTION_ID, PORT, super.getBalancerPort());
        communicationTargets = configMap(RSOCKET_SECTION_ID, TARGETS, config -> rsocketCommunicationTarget()
                .host(ifEmpty(config.getString(HOST), balancerHost))
                .port(getOrElse(config.getInt(PORT), balancerPort))
                .dataFormat(ifException(() -> RsocketDataFormat.valueOf(config.getString(DEFAULT_DATA_FORMAT).toUpperCase()), super.getDefaultDataFormat()))
                .build(), super.getCommunicationTargets());
        if (restart && context().hasModule(RSOCKET_MODULE_ID)) {
            rsocketModuleState().getServer().restart();
        }
    }
}