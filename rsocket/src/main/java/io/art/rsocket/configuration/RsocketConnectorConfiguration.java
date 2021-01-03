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
import io.art.rsocket.interceptor.*;
import io.art.rsocket.model.*;
import io.art.rsocket.model.RsocketSetupPayload.*;
import io.rsocket.core.*;
import io.rsocket.frame.decoder.*;
import lombok.*;
import reactor.netty.http.client.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.combiner.SectionCombiner.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.rsocket.constants.RsocketModuleConstants.TransportMode.*;
import static io.art.value.constants.ValueConstants.*;
import static io.art.value.constants.ValueConstants.DataFormat.*;
import static io.art.value.mime.MimeTypeDataFormatMapper.*;
import static io.rsocket.frame.FrameLengthCodec.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static reactor.netty.http.client.HttpClient.*;

@Getter
@RequiredArgsConstructor
public class RsocketConnectorConfiguration {
    private final RSocketConnector connector;
    private RsocketSetupPayload setupPayload;
    private TransportMode transport;
    private TcpClient tcpClient;
    private int tcpMaxFrameLength;
    private HttpClient httpWebSocketClient;
    private String httpWebSocketPath;
    private boolean logging;

    public static RsocketConnectorConfiguration from(RsocketCommunicatorConfiguration communicatorConfiguration, ConfigurationSource source) {
        RSocketConnector connector = RSocketConnector.create();
        RsocketConnectorConfiguration configuration = new RsocketConnectorConfiguration(connector);
        configuration.logging = orElse(source.getBool(LOGGING_KEY), communicatorConfiguration.isLogging());
        DataFormat dataFormat = dataFormat(source.getString(DEFAULT_DATA_FORMAT_KEY), communicatorConfiguration.getDefaultDataFormat());
        DataFormat metaDataFormat = dataFormat(source.getString(DEFAULT_META_DATA_FORMAT_KEY), communicatorConfiguration.getDefaultMetaDataFormat());
        connector.payloadDecoder(rsocketPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY)) == DEFAULT ? PayloadDecoder.DEFAULT : PayloadDecoder.ZERO_COPY)
                .maxInboundPayloadSize(orElse(source.getInt(MAX_INBOUND_PAYLOAD_SIZE_KEY), communicatorConfiguration.getMaxInboundPayloadSize()))
                .dataMimeType(toMimeType(dataFormat).toString())
                .metadataMimeType(toMimeType(metaDataFormat).toString())
                .fragment(orElse(source.getInt(FRAGMENTATION_MTU_KEY), communicatorConfiguration.getFragmentationMtu()))
                .interceptors(registry -> registry
                        .forResponder(new RsocketLoggingInterceptor(configuration::isLogging))
                        .forRequester(new RsocketLoggingInterceptor(configuration::isLogging)));

        apply(source.getNested(RESUME_SECTION), section -> connector.resume(RsocketResumeConfigurator.from(section, communicatorConfiguration.getResume())));
        apply(source.getNested(RECONNECT_SECTION), section -> connector.reconnect(RsocketRetryConfigurator.from(section, communicatorConfiguration.getReconnect())));

        ConfigurationSource keepAlive;
        if (nonNull(keepAlive = source.getNested(KEEP_ALIVE_SECTION))) {
            RsocketKeepAliveConfiguration keepAliveConfiguration = RsocketKeepAliveConfiguration.from(keepAlive);
            connector.keepAlive(keepAliveConfiguration.getInterval(), keepAliveConfiguration.getMaxLifeTime());
        }

        RsocketSetupPayloadBuilder setupPayloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(dataFormat)
                .metadataFormat(metaDataFormat);

        String serviceId = source.getString(DEFAULT_SERVICE_ID_KEY);
        String methodId = source.getString(DEFAULT_METHOD_ID_KEY);

        if (isNotEmpty(serviceId) && isNotEmpty(methodId)) {
            setupPayloadBuilder.serviceMethod(serviceMethod(serviceId, methodId));
        }

        configuration.setupPayload = setupPayloadBuilder.build();

        configuration.transport = rsocketTransport(source.getString(TRANSPORT_MODE_KEY));
        switch (configuration.transport) {
            case TCP:
                String host = source.getString(TRANSPORT_TCP_HOST_KEY);
                int port = orElse(source.getInt(TRANSPORT_TCP_PORT_KEY), DEFAULT_PORT);
                if (isEmpty(host)) {
                    throw new RsocketException(format(CONFIGURATION_PARAMETER_NOT_EXISTS, combine(source.getSection(), TRANSPORT_TCP_HOST_KEY)));
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
        }

        return configuration;
    }
}
