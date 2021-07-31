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

import io.art.core.model.*;
import io.art.core.source.*;
import io.art.rsocket.refresher.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.transport.payload.*;
import io.rsocket.frame.decoder.*;
import lombok.*;
import reactor.netty.http.server.*;
import java.util.function.*;

@Getter
public class RsocketHttpServerConfiguration {
    private RsocketCommonServerConfiguration common;
    private UnaryOperator<HttpServer> decorator;

    @Builder(toBuilder = true)
    private RsocketHttpServerConfiguration(int port,
                                           String host,
                                           ServiceMethodIdentifier defaultServiceMethod,
                                           boolean logging,
                                           int fragmentationMtu,
                                           RsocketResumeConfiguration resume,
                                           PayloadDecoder payloadDecoder,
                                           int maxInboundPayloadSize,
                                           DataFormat defaultDataFormat,
                                           DataFormat defaultMetaDataFormat,
                                           Function<DataFormat, TransportPayloadReader> setupReader,
                                           UnaryOperator<HttpServer> decorator) {
        this.decorator = decorator;
        this.common = RsocketCommonServerConfiguration.builder()
                .port(port)
                .host(host)
                .defaultServiceMethod(defaultServiceMethod)
                .logging(logging)
                .fragmentationMtu(fragmentationMtu)
                .resume(resume)
                .payloadDecoder(payloadDecoder)
                .maxInboundPayloadSize(maxInboundPayloadSize)
                .defaultDataFormat(defaultDataFormat)
                .defaultMetaDataFormat(defaultMetaDataFormat)
                .setupReader(setupReader)
                .build();
    }

    public static RsocketHttpServerConfiguration defaults() {
        RsocketHttpServerConfiguration configuration = RsocketHttpServerConfiguration.builder().build();
        configuration.decorator = UnaryOperator.identity();
        configuration.common = RsocketCommonServerConfiguration.defaults();
        return configuration;
    }

    public static RsocketHttpServerConfiguration from(RsocketModuleRefresher refresher, RsocketHttpServerConfiguration current, ConfigurationSource source) {
        RsocketHttpServerConfiguration configuration = RsocketHttpServerConfiguration.builder().build();
        configuration.decorator = current.decorator;
        configuration.common = RsocketCommonServerConfiguration.from(refresher, current.common, source);
        return configuration;
    }
}
