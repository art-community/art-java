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

package io.art.rsocket.configuration;

import io.art.core.changes.*;
import io.art.core.source.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.refresher.*;
import io.art.transport.constants.TransportModuleConstants.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.rsocket.configuration.RsocketKeepAliveConfiguration.*;
import static io.art.rsocket.configuration.RsocketResumeConfiguration.*;
import static io.art.rsocket.configuration.RsocketRetryConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import static io.rsocket.frame.FrameLengthCodec.*;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RsocketCommonConnectorConfiguration {
    private String connector;
    private PayloadDecoderMode payloadDecoderMode;
    private int maxInboundPayloadSize;
    private int fragment;
    private RsocketKeepAliveConfiguration keepAlive;
    private RsocketResumeConfiguration resume;
    private RsocketRetryConfiguration retry;
    private int tcpMaxFrameLength;
    private String httpWebSocketPath;
    private boolean logging;
    private DataFormat dataFormat;
    private DataFormat metaDataFormat;
    private int port;
    private String host;

    public static RsocketCommonConnectorConfiguration defaults(String connector) {
        RsocketCommonConnectorConfiguration configuration = RsocketCommonConnectorConfiguration.builder().build();
        configuration.connector = connector;
        configuration.logging = false;
        configuration.fragment = 0;
        configuration.maxInboundPayloadSize = Integer.MAX_VALUE;
        configuration.payloadDecoderMode = ZERO_COPY;
        configuration.dataFormat = JSON;
        configuration.metaDataFormat = JSON;
        configuration.tcpMaxFrameLength = FRAME_LENGTH_MASK;
        configuration.httpWebSocketPath = SLASH;
        configuration.port = DEFAULT_PORT;
        configuration.host = LOCALHOST;
        return configuration;
    }

    public static RsocketCommonConnectorConfiguration from(RsocketModuleRefresher refresher, RsocketCommonConnectorConfiguration current, ConfigurationSource source) {
        RsocketCommonConnectorConfiguration configuration = RsocketCommonConnectorConfiguration.builder().build();
        configuration.connector = current.connector;

        ChangesListener listener = refresher.connectorListeners().listenerFor(current.connector);
        ChangesListener loggingListener = refresher.connectorLoggingListeners().listenerFor(configuration.connector);

        configuration.logging = loggingListener.emit(orElse(source.getBoolean(LOGGING_KEY), current.logging));

        configuration.dataFormat = listener.emit(dataFormat(source.getString(DATA_FORMAT_KEY), current.dataFormat));
        configuration.metaDataFormat = listener.emit(dataFormat(source.getString(META_DATA_FORMAT_KEY), current.metaDataFormat));
        configuration.payloadDecoderMode = listener.emit(rsocketPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY), current.payloadDecoderMode));
        configuration.maxInboundPayloadSize = listener.emit(orElse(source.getInteger(MAX_INBOUND_PAYLOAD_SIZE_KEY), current.maxInboundPayloadSize));
        configuration.fragment = listener.emit(orElse(source.getInteger(FRAGMENTATION_MTU_KEY), current.fragment));
        configuration.keepAlive = listener.emit(let(source.getNested(KEEP_ALIVE_SECTION), section -> rsocketKeepAlive(section, current.keepAlive), current.keepAlive));
        configuration.resume = listener.emit(let(source.getNested(RESUME_SECTION), section -> rsocketResume(section, current.resume), current.resume));
        configuration.retry = listener.emit(let(source.getNested(RECONNECT_SECTION), section -> rsocketRetry(section, current.retry), current.retry));
        configuration.port = listener.emit(orElse(source.getInteger(TRANSPORT_PORT_KEY), current.port));
        configuration.host = listener.emit(orElse(source.getString(TRANSPORT_HOST_KEY), current.host));

        return configuration;
    }
}
