/*
 * ART
 *
 * Copyright 2019-2022 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.rsocket.configuration.server;

import io.art.core.annotation.*;
import io.art.core.changes.*;
import io.art.core.model.*;
import io.art.core.source.*;
import io.art.rsocket.configuration.common.*;
import io.art.rsocket.constants.*;
import io.art.rsocket.refresher.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.rsocket.core.*;
import io.rsocket.frame.decoder.*;
import io.rsocket.plugins.*;
import lombok.*;
import reactor.netty.http.server.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.CommonConfigurationKeys.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.rsocket.configuration.common.RsocketResumeConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.DEFAULT;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import static io.rsocket.frame.FrameLengthCodec.*;
import static io.rsocket.frame.decoder.PayloadDecoder.ZERO_COPY;
import static java.util.Optional.*;
import static java.util.function.UnaryOperator.identity;
import java.util.function.*;

@Getter
@Public
@Builder(toBuilder = true)
public class RsocketWsServerConfiguration {
    private int port;
    private String host;
    private ServiceMethodIdentifier defaultServiceMethod;
    private boolean verbose;
    private int fragmentationMtu;
    private RsocketResumeConfiguration resume;
    private PayloadDecoder payloadDecoder;
    private int maxInboundPayloadSize;
    private DataFormat defaultDataFormat;
    private DataFormat defaultMetaDataFormat;
    private UnaryOperator<InterceptorRegistry> interceptors;
    private UnaryOperator<HttpServer> wsDecorator;
    private UnaryOperator<RSocketServer> decorator;
    private RsocketSslConfiguration ssl;

    public static RsocketWsServerConfiguration wsServerConfiguration() {
        RsocketWsServerConfiguration configuration = RsocketWsServerConfiguration.builder().build();
        configuration.wsDecorator = identity();
        configuration.decorator = identity();
        configuration.defaultDataFormat = MESSAGE_PACK;
        configuration.defaultMetaDataFormat = MESSAGE_PACK;
        configuration.verbose = false;
        configuration.fragmentationMtu = 0;
        configuration.payloadDecoder = ZERO_COPY;
        configuration.maxInboundPayloadSize = FRAME_LENGTH_MASK;
        configuration.port = DEFAULT_PORT;
        configuration.host = BROADCAST_IP_ADDRESS;
        configuration.interceptors = identity();
        configuration.resume = rsocketResume();
        return configuration;
    }

    public static RsocketWsServerConfiguration wsServerConfiguration(RsocketModuleRefresher refresher, RsocketWsServerConfiguration current, ConfigurationSource source) {
        RsocketWsServerConfiguration configuration = RsocketWsServerConfiguration.builder().build();
        configuration.interceptors = current.interceptors;
        configuration.decorator = current.decorator;
        configuration.wsDecorator = current.wsDecorator;

        ChangesListener serverListener = refresher.serverListener();
        ChangesListener serverLoggingListener = refresher.serverLoggingListener();

        configuration.verbose = serverLoggingListener.emit(orElse(source.getBoolean(VERBOSE_KEY), current.verbose));

        configuration.defaultDataFormat = serverListener.emit(dataFormat(source.getString(DATA_FORMAT_KEY), current.defaultDataFormat));
        configuration.defaultMetaDataFormat = serverListener.emit(dataFormat(source.getString(META_DATA_FORMAT_KEY), current.defaultMetaDataFormat));
        configuration.fragmentationMtu = serverListener.emit(orElse(source.getInteger(FRAGMENTATION_MTU_KEY), current.fragmentationMtu));
        configuration.payloadDecoder = serverListener.emit(ofNullable(source.getString(PAYLOAD_DECODER_KEY))
                .map(RsocketModuleConstants.PayloadDecoderMode::rsocketPayloadDecoder)
                .map(decoder -> decoder == DEFAULT ? PayloadDecoder.DEFAULT : ZERO_COPY)
                .orElse(current.payloadDecoder));
        configuration.maxInboundPayloadSize = serverListener.emit(orElse(source.getInteger(MAX_INBOUND_PAYLOAD_SIZE_KEY), current.maxInboundPayloadSize));
        configuration.resume = serverListener.emit(orElse(source.getNested(RESUME_SECTION, RsocketResumeConfiguration::rsocketResume), current.resume));

        String serviceId = source.getString(SERVICE_ID_KEY);
        String methodId = source.getString(METHOD_ID_KEY);

        if (isNotEmpty(serviceId) && isNotEmpty(methodId)) {
            configuration.defaultServiceMethod = serverListener.emit(serviceMethodId(serviceId, methodId));
        }

        configuration.defaultServiceMethod = orElse(configuration.defaultServiceMethod, current.defaultServiceMethod);
        configuration.port = serverListener.emit(orElse(source.getInteger(PORT_KEY), current.port));
        configuration.host = serverListener.emit(orElse(source.getString(HOST_KEY), current.host));
        configuration.ssl = serverListener.emit(orElse(source.getNested(SSL_SECTION, RsocketSslConfiguration::rsocketSsl), current.ssl));
        return configuration;
    }
}
