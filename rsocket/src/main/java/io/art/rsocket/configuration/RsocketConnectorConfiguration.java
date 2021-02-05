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

package io.art.rsocket.configuration;

import io.art.core.source.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.model.*;
import io.art.rsocket.model.RsocketSetupPayload.*;
import io.rsocket.core.*;
import lombok.*;
import reactor.netty.http.client.*;
import reactor.netty.tcp.*;
import reactor.util.retry.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.combiner.SectionCombiner.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.rsocket.constants.RsocketModuleConstants.TransportMode.*;
import static io.art.value.constants.ValueModuleConstants.*;
import static io.art.value.constants.ValueModuleConstants.DataFormat.*;
import static io.rsocket.frame.FrameLengthCodec.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static reactor.netty.http.client.HttpClient.*;

@Getter
@RequiredArgsConstructor
public class RsocketConnectorConfiguration {
    private String connectorId;
    private PayloadDecoderMode payloadDecoderMode;
    private int maxInboundPayloadSize;
    private int fragment;
    private RsocketKeepAliveConfiguration keepAlive;
    private Resume resume;
    private Retry retry;
    private RsocketSetupPayload setupPayload;
    private TransportMode transport;
    private TcpClient tcpClient;
    private int tcpMaxFrameLength;
    private HttpClient httpWebSocketClient;
    private String httpWebSocketPath;
    private boolean logging;
    private DataFormat dataFormat;
    private DataFormat metaDataFormat;

    public static RsocketConnectorConfiguration from(RsocketCommunicatorConfiguration communicatorConfiguration, ConfigurationSource source) {
        RsocketConnectorConfiguration configuration = new RsocketConnectorConfiguration();
        RsocketConnectorConfiguration defaults = communicatorConfiguration.getDefaultConnectorConfiguration();

        configuration.dataFormat = dataFormat(source.getString(DEFAULT_DATA_FORMAT_KEY), defaults.dataFormat);
        configuration.metaDataFormat = dataFormat(source.getString(DEFAULT_META_DATA_FORMAT_KEY), defaults.metaDataFormat);
        configuration.connectorId = source.getSection();
        configuration.logging = orElse(source.getBool(LOGGING_KEY), defaults.logging);
        configuration.payloadDecoderMode = rsocketPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY), defaults.payloadDecoderMode);
        configuration.maxInboundPayloadSize = orElse(source.getInt(MAX_INBOUND_PAYLOAD_SIZE_KEY), defaults.maxInboundPayloadSize);
        configuration.fragment = orElse(source.getInt(FRAGMENTATION_MTU_KEY), defaults.fragment);
        configuration.keepAlive = let(source.getNested(KEEP_ALIVE_SECTION), section -> RsocketKeepAliveConfiguration.from(section, defaults.keepAlive), defaults.keepAlive);
        configuration.resume = let(source.getNested(RESUME_SECTION), section -> RsocketResumeConfigurator.from(section, defaults.resume), defaults.resume);
        configuration.retry = let(source.getNested(RECONNECT_SECTION), section -> RsocketRetryConfigurator.from(section, defaults.retry), defaults.retry);

        RsocketSetupPayloadBuilder setupPayloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(configuration.dataFormat)
                .metadataFormat(configuration.metaDataFormat);

        String serviceId = source.getString(DEFAULT_SERVICE_ID_KEY);
        String methodId = source.getString(DEFAULT_METHOD_ID_KEY);

        if (isNotEmpty(serviceId) && isNotEmpty(methodId)) {
            setupPayloadBuilder.serviceMethod(serviceMethod(serviceId, methodId));
        }

        configuration.setupPayload = setupPayloadBuilder.build();

        if (!source.has(TRANSPORT_SECTION) && isNull(defaults.transport)) {
            throw new RsocketException(format(CONFIGURATION_PARAMETER_NOT_EXISTS, combine(source.getSection(), TRANSPORT_SECTION)));
        }

        configuration.transport = rsocketTransport(source.getString(TRANSPORT_MODE_KEY), defaults.transport);
        switch (configuration.transport) {
            case TCP:
                String host = source.getString(TRANSPORT_TCP_HOST_KEY);
                Integer port = source.getInt(TRANSPORT_TCP_PORT_KEY);
                if (isEmpty(host) && isNull(defaults.tcpClient)) {
                    throw new RsocketException(format(CONFIGURATION_PARAMETER_NOT_EXISTS, combine(source.getSection(), TRANSPORT_TCP_HOST_KEY)));
                }
                if (isEmpty(port) && isNull(defaults.tcpClient)) {
                    throw new RsocketException(format(CONFIGURATION_PARAMETER_NOT_EXISTS, combine(source.getSection(), TRANSPORT_PORT_KEY)));
                }
                if (isEmpty(host) || isEmpty(port)) {
                    configuration.tcpClient = defaults.tcpClient;
                }
                configuration.tcpClient = orElse(configuration.tcpClient, TcpClient.create().port(port).host(host));
                configuration.tcpMaxFrameLength = orElse(source.getInt(TRANSPORT_TCP_MAX_FRAME_LENGTH), defaults.tcpMaxFrameLength);
                break;
            case WS:
                String url = source.getString(TRANSPORT_WS_BASE_URL_KEY);
                if (isEmpty(url) && isNull(defaults.httpWebSocketClient)) {
                    throw new RsocketException(format(CONFIGURATION_PARAMETER_NOT_EXISTS, combine(source.getSection(), TRANSPORT_WS_BASE_URL_KEY)));
                }
                if (isEmpty(url)) {
                    configuration.httpWebSocketClient = defaults.httpWebSocketClient;
                }
                configuration.httpWebSocketClient = orElse(configuration.httpWebSocketClient, create().baseUrl(url));
                configuration.httpWebSocketPath = orElse(source.getString(TRANSPORT_WS_PATH_KEY), defaults.httpWebSocketPath);
                break;
        }

        return configuration;
    }

    public static RsocketConnectorConfiguration from(ConfigurationSource source) {
        RsocketConnectorConfiguration configuration = new RsocketConnectorConfiguration();

        configuration.logging = orElse(source.getBool(LOGGING_KEY), false);
        configuration.fragment = orElse(source.getInt(FRAGMENTATION_MTU_KEY), 0);
        configuration.maxInboundPayloadSize = orElse(source.getInt(MAX_INBOUND_PAYLOAD_SIZE_KEY), FRAME_LENGTH_MASK);
        configuration.keepAlive = source.getNested(KEEP_ALIVE_SECTION, RsocketKeepAliveConfiguration::from);
        configuration.resume = source.getNested(RESUME_SECTION, RsocketResumeConfigurator::from);
        configuration.retry = source.getNested(RECONNECT_SECTION, RsocketRetryConfigurator::from);
        configuration.payloadDecoderMode = rsocketPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY));
        configuration.dataFormat = dataFormat(source.getString(DEFAULT_DATA_FORMAT_KEY), JSON);
        configuration.metaDataFormat = dataFormat(source.getString(DEFAULT_META_DATA_FORMAT_KEY), JSON);

        RsocketSetupPayloadBuilder setupPayloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(configuration.dataFormat)
                .metadataFormat(configuration.metaDataFormat);

        String serviceId = source.getString(DEFAULT_SERVICE_ID_KEY);
        String methodId = source.getString(DEFAULT_METHOD_ID_KEY);

        if (isNotEmpty(serviceId) && isNotEmpty(methodId)) {
            setupPayloadBuilder.serviceMethod(serviceMethod(serviceId, methodId));
        }

        configuration.setupPayload = setupPayloadBuilder.build();

        if (!source.has(TRANSPORT_SECTION)) {
            return configuration;
        }

        configuration.transport = rsocketTransport(source.getString(TRANSPORT_MODE_KEY));
        switch (configuration.transport) {
            case TCP:
                String host = source.getString(TRANSPORT_TCP_HOST_KEY);
                int port = source.getInt(TRANSPORT_TCP_PORT_KEY);
                if (isEmpty(host)) {
                    throw new RsocketException(format(CONFIGURATION_PARAMETER_NOT_EXISTS, combine(source.getSection(), TRANSPORT_TCP_HOST_KEY)));
                }
                if (isEmpty(port)) {
                    throw new RsocketException(format(CONFIGURATION_PARAMETER_NOT_EXISTS, combine(source.getSection(), TRANSPORT_PORT_KEY)));
                }
                configuration.tcpClient = TcpClient.create().port(port).host(host);
                configuration.tcpMaxFrameLength = orElse(source.getInt(TRANSPORT_TCP_MAX_FRAME_LENGTH), FRAME_LENGTH_MASK);
                break;
            case WS:
                String url = source.getString(TRANSPORT_WS_BASE_URL_KEY);
                if (isEmpty(url)) {
                    throw new RsocketException(format(CONFIGURATION_PARAMETER_NOT_EXISTS, combine(source.getSection(), TRANSPORT_WS_BASE_URL_KEY)));
                }
                configuration.httpWebSocketClient = create().baseUrl(url);
                configuration.httpWebSocketPath = orElse(source.getString(TRANSPORT_WS_PATH_KEY), SLASH);
                break;
            default:
                throw new RsocketException(format(CONFIGURATION_PARAMETER_NOT_EXISTS, combine(source.getSection(), TRANSPORT_MODE_KEY)));
        }

        return configuration;
    }
}
