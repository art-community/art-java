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

package io.art.rsocket.configuration.server;

import io.art.core.model.*;
import io.art.rsocket.configuration.common.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.transport.payload.*;
import io.rsocket.frame.decoder.*;
import io.rsocket.plugins.*;
import lombok.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class RsocketCommonServerConfiguration {
    private int port;
    private String host;
    private ServiceMethodIdentifier defaultServiceMethod;
    private boolean logging;
    private int fragmentationMtu;
    private RsocketResumeConfiguration resume;
    private PayloadDecoder payloadDecoder;
    private int maxInboundPayloadSize;
    private DataFormat defaultDataFormat;
    private DataFormat defaultMetaDataFormat;
    private Function<DataFormat, TransportPayloadReader> setupReader;
    private UnaryOperator<InterceptorRegistry> interceptors;

    public static RsocketCommonServerConfiguration fromTcp(RsocketTcpServerConfiguration configuration) {
        RsocketCommonServerConfiguration common = new RsocketCommonServerConfiguration();
        common.resume = configuration.getResume();
        common.defaultServiceMethod = configuration.getDefaultServiceMethod();
        common.defaultDataFormat = configuration.getDefaultDataFormat();
        common.defaultMetaDataFormat = configuration.getDefaultMetaDataFormat();
        common.logging = configuration.isLogging();
        common.fragmentationMtu = configuration.getFragmentationMtu();
        common.payloadDecoder = configuration.getPayloadDecoder();
        common.maxInboundPayloadSize = configuration.getMaxInboundPayloadSize();
        common.port = configuration.getPort();
        common.host = configuration.getHost();
        common.setupReader = configuration.getSetupReader();
        common.interceptors = configuration.getInterceptors();
        return common;
    }

    public static RsocketCommonServerConfiguration fromHttp(RsocketHttpServerConfiguration configuration) {
        RsocketCommonServerConfiguration common = new RsocketCommonServerConfiguration();
        common.resume = configuration.getResume();
        common.defaultServiceMethod = configuration.getDefaultServiceMethod();
        common.defaultDataFormat = configuration.getDefaultDataFormat();
        common.defaultMetaDataFormat = configuration.getDefaultMetaDataFormat();
        common.logging = configuration.isLogging();
        common.fragmentationMtu = configuration.getFragmentationMtu();
        common.payloadDecoder = configuration.getPayloadDecoder();
        common.maxInboundPayloadSize = configuration.getMaxInboundPayloadSize();
        common.port = configuration.getPort();
        common.host = configuration.getHost();
        common.setupReader = configuration.getSetupReader();
        common.interceptors = configuration.getInterceptors();
        return common;
    }
}
