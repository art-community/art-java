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

import io.art.core.mime.*;
import io.art.core.source.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.model.*;
import io.art.rsocket.model.RsocketSetupPayload.*;
import io.rsocket.core.*;
import io.rsocket.frame.decoder.*;
import io.rsocket.plugins.*;
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
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.rsocket.constants.RsocketModuleConstants.TransportMode.*;
import static io.art.value.constants.ValueModuleConstants.*;
import static io.art.value.constants.ValueModuleConstants.DataFormat.*;
import static io.art.value.mime.MimeTypeDataFormatMapper.*;
import static io.rsocket.frame.FrameLengthCodec.*;
import static java.text.MessageFormat.*;
import static reactor.netty.http.client.HttpClient.*;
import java.util.function.*;

@Getter
@RequiredArgsConstructor
public class RsocketConnectorConfiguration {
    private PayloadDecoder payloadDecoder;
    private int maxInboundPayloadSize;
    private MimeType dataMimeType;
    private MimeType metaDataMimeType;
    private int fragment;
    private Consumer<InterceptorRegistry> interceptors;
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

    public static RsocketConnectorConfiguration from(RsocketCommunicatorConfiguration communicatorConfiguration, ConfigurationSource source) {
        RsocketConnectorConfiguration configuration = new RsocketConnectorConfiguration();
        configuration.logging = orElse(source.getBool(LOGGING_KEY), communicatorConfiguration.isLogging());
        DataFormat dataFormat = dataFormat(source.getString(DEFAULT_DATA_FORMAT_KEY), communicatorConfiguration.getDefaultDataFormat());
        DataFormat metaDataFormat = dataFormat(source.getString(DEFAULT_META_DATA_FORMAT_KEY), communicatorConfiguration.getDefaultMetaDataFormat());
        configuration.payloadDecoder = rsocketPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY)) == DEFAULT ? PayloadDecoder.DEFAULT : PayloadDecoder.ZERO_COPY;
        configuration.maxInboundPayloadSize = orElse(source.getInt(MAX_INBOUND_PAYLOAD_SIZE_KEY), communicatorConfiguration.getMaxInboundPayloadSize());
        configuration.dataMimeType = toMimeType(dataFormat);
        configuration.metaDataMimeType = toMimeType(metaDataFormat);
        configuration.fragment = orElse(source.getInt(FRAGMENTATION_MTU_KEY), communicatorConfiguration.getFragmentationMtu());
        configuration.interceptors = registry -> registry
                .forResponder(new RsocketLoggingInterceptor(configuration::isLogging))
                .forRequester(new RsocketLoggingInterceptor(configuration::isLogging));
        apply(source.getNested(KEEP_ALIVE_SECTION), section -> configuration.keepAlive = RsocketKeepAliveConfiguration.from(section));
        apply(source.getNested(RESUME_SECTION), section -> configuration.resume = RsocketResumeConfigurator.from(section, communicatorConfiguration.getResume()));
        apply(source.getNested(RECONNECT_SECTION), section -> configuration.retry = RsocketRetryConfigurator.from(section, communicatorConfiguration.getReconnect()));

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
