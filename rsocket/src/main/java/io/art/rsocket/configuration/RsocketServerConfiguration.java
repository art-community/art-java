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

import com.google.common.collect.*;
import io.art.core.source.*;
import io.art.entity.constants.EntityConstants.*;
import io.art.rsocket.constants.*;
import io.art.rsocket.interceptor.*;
import io.art.server.model.*;
import io.rsocket.core.*;
import io.rsocket.frame.decoder.*;
import io.rsocket.plugins.*;
import lombok.*;
import reactor.netty.http.server.*;
import reactor.netty.tcp.*;
import static com.google.common.collect.ImmutableMap.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.entity.constants.EntityConstants.DataFormat.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.rsocket.constants.RsocketModuleConstants.TransportMode.*;
import static io.art.server.model.ServiceMethodIdentifier.*;
import static io.rsocket.frame.FrameLengthCodec.*;
import static java.util.Optional.*;
import java.util.*;
import java.util.function.*;

@Getter
public class RsocketServerConfiguration {
    private TcpServer tcpServer;
    private HttpServer httpWebSocketServer;
    private ImmutableMap<String, RsocketServiceConfiguration> services;
    private ServiceMethodIdentifier defaultServiceMethod;
    private int tcpMaxFrameLength;
    private boolean tracing;
    private int fragmentationMtu;
    private Resume resume;
    private PayloadDecoder payloadDecoder;
    private int maxInboundPayloadSize;
    private RsocketModuleConstants.TransportMode transport;
    private Consumer<InterceptorRegistry> interceptorConfigurer;
    private DataFormat defaultDataFormat;
    private DataFormat defaultMetaDataFormat;

    public static RsocketServerConfiguration from(ConfigurationSource source) {
        RsocketServerConfiguration configuration = new RsocketServerConfiguration();
        configuration.defaultDataFormat = dataFormat(source.getString(DEFAULT_DATA_FORMAT_KEY), JSON);
        configuration.defaultMetaDataFormat = dataFormat(source.getString(DEFAULT_META_DATA_FORMAT_KEY), JSON);
        configuration.tracing = orElse(source.getBool(TRACING_KEY), false);
        configuration.fragmentationMtu = orElse(source.getInt(FRAGMENTATION_MTU_KEY), 0);

        String serviceId = source.getString(DEFAULT_SERVICE_ID_KEY);
        String methodId = source.getString(DEFAULT_METHOD_ID_KEY);

        if (isNotEmpty(serviceId) && isNotEmpty(methodId)) {
            configuration.defaultServiceMethod = serviceMethod(serviceId, methodId);
        }

        configuration.resume = let(source.getNested(RESUME_SECTION), RsocketResumeConfigurator::from);
        configuration.payloadDecoder = rsocketPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY)) == DEFAULT
                ? PayloadDecoder.DEFAULT
                : PayloadDecoder.ZERO_COPY;
        configuration.maxInboundPayloadSize = orElse(source.getInt(MAX_INBOUND_PAYLOAD_SIZE_KEY), FRAME_LENGTH_MASK);
        configuration.transport = rsocketTransport(source.getString(TRANSPORT_MODE_KEY));

        int port = orElse(source.getInt(TRANSPORT_PORT_KEY), DEFAULT_PORT);
        String host = orElse(source.getString(TRANSPORT_HOST_KEY), BROADCAST_IP_ADDRESS);

        switch (configuration.transport) {
            case TCP:
                configuration.tcpServer = TcpServer.create().port(port).host(host);
                configuration.tcpMaxFrameLength = orElse(source.getInt(TRANSPORT_TCP_MAX_FRAME_LENGTH), FRAME_LENGTH_MASK);
                break;
            case WS:
                configuration.httpWebSocketServer = HttpServer.create().port(port).host(host);
                break;
        }

        configuration.interceptorConfigurer = registry -> registry.forRequester(new RsocketLoggingInterceptor(configuration::isTracing));

        configuration.services = ofNullable(source.getNestedMap(SERVICES_KEY))
                .map(configurations -> configurations.entrySet()
                        .stream()
                        .collect(toImmutableMap(Map.Entry::getKey, entry -> RsocketServiceConfiguration.from(configuration, entry.getValue()))))
                .orElse(ImmutableMap.of());

        return configuration;
    }
}
