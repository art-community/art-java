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

import io.netty.buffer.*;
import lombok.*;
import ru.art.rsocket.configuration.RsocketModuleConfiguration.*;
import ru.art.rsocket.model.*;
import ru.art.rsocket.resume.*;
import ru.art.rsocket.server.*;
import static java.util.Objects.*;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.common.DataFormats.*;
import static ru.art.config.extensions.rsocket.RsocketConfigKeys.*;
import static ru.art.config.extensions.rsocket.RsocketResumableStateRepositories.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.extension.ExceptionExtensions.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.model.RsocketCommunicationTargetConfiguration.*;
import static ru.art.rsocket.module.RsocketModule.*;
import static ru.art.rsocket.reader.ByteBufReader.*;
import java.util.*;
import java.util.function.*;

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
    private int fragmentationMtu;
    private boolean resumableServer;
    private long serverResumeSessionDuration;
    private long serverResumeStreamDuration;
    private boolean resumableClient;
    private long clientResumeSessionDuration;
    private long clientResumeStreamDuration;
    private Function<? extends ByteBuf, ? extends ResumableFramesStateRepository> resumableFramesServerStateRepository = super.getResumableFramesServerStateRepository();

    public RsocketAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        resumableServer = configBoolean(RSOCKET_SECTION_ID, RESUMABLE, super.isResumableServer());
        serverResumeSessionDuration = configLong(RSOCKET_SECTION_ID, RESUME_SESSION_DURATION, super.getServerResumeSessionDuration());
        serverResumeStreamDuration = configLong(RSOCKET_SECTION_ID, RESUME_STREAM_TIMEOUT, super.getServerResumeStreamTimeout());
        resumableClient = configBoolean(RSOCKET_COMMUNICATION_SECTION_ID, RESUMABLE, super.isResumableClient());
        clientResumeSessionDuration = configLong(RSOCKET_COMMUNICATION_SECTION_ID, RESUME_SESSION_DURATION, super.getClientResumeSessionDuration());
        clientResumeStreamDuration = configLong(RSOCKET_COMMUNICATION_SECTION_ID, RESUME_STREAM_TIMEOUT, super.getClientResumeStreamTimeout());
        enableRawDataTracing = configBoolean(RSOCKET_SECTION_ID, ENABLE_RAW_DATA_TRACING, super.isEnableRawDataTracing());
        enableValueTracing = configBoolean(RSOCKET_SECTION_ID, ENABLE_VALUE_TRACING, super.isEnableValueTracing());
        fragmentationMtu = configInt(RSOCKET_SECTION_ID, FRAGMENTATION_MTU, super.getFragmentationMtu());
        dataFormat = ifException(() -> parseRsocketDataFormat(configString(RSOCKET_SECTION_ID, DATA_FORMAT).toUpperCase()), super.getDataFormat());
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
        if (hasPath(RSOCKET_SERVER_RESUME_SECTION_ID, RESUME_STATE_STORAGE_TYPE)) {
            switch (RsocketResumeStateStorageType.valueOf(configString(RSOCKET_SERVER_RESUME_SECTION_ID, RESUME_STATE_STORAGE_TYPE).toUpperCase())) {
                case TARANTOOL:
                    String instanceId = configString(RSOCKET_SERVER_RESUME_SECTION_ID, RESUME_STATE_TARANTOOL_INSTANCE_ID);
                    resumableFramesServerStateRepository = token -> storeInTarantool(readByteBufToString(token), instanceId);
                    break;
                case ROCKS:
                    resumableFramesServerStateRepository = token -> storeInRocksDb(readByteBufToString(token));
                    break;
            }
        }
        communicationTargets = configInnerMap(RSOCKET_COMMUNICATION_SECTION_ID, TARGETS, config -> rsocketCommunicationTarget()
                .host(ifEmpty(config.getString(HOST), balancerHost))
                .port(getOrElse(config.getInt(PORT), balancerTcpPort))
                .dataFormat(super.getDataFormat())
                .resumable(getOrElse(config.getBool(RESUMABLE), super.isResumableClient()))
                .resumeSessionDuration(getOrElse(config.getLong(RESUME_SESSION_DURATION), super.getClientResumeSessionDuration()))
                .build(), super.getCommunicationTargets());
        if (restart && context().hasModule(RSOCKET_MODULE_ID)) {
            RsocketServer server = rsocketModuleState().getTcpServer();
            if (nonNull(server) && server.isWorking()) {
                server.restart();
            }
            server = rsocketModuleState().getWebSocketServer();
            if (nonNull(server) && server.isWorking()) {
                server.restart();
            }
        }
    }

    private RsocketDataFormat parseRsocketDataFormat(String format) {
        if (isEmpty(format)) return super.getDataFormat();
        switch (format) {
            case JSON:
                return RsocketDataFormat.JSON;
            case PROTOBUF:
                return RsocketDataFormat.PROTOBUF;
            case MESSAGE_PACK:
                return RsocketDataFormat.MESSAGE_PACK;
            case XML:
                return RsocketDataFormat.XML;
        }
        return super.getDataFormat();
    }
}