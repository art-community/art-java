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

import io.art.core.source.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.refresher.*;
import io.art.transport.constants.TransportModuleConstants.*;
import lombok.*;
import reactor.netty.http.client.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import java.util.function.*;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RsocketHttpClientConfiguration {
    @EqualsAndHashCode.Include
    private RsocketCommonConnectorConfiguration common;
    @EqualsAndHashCode.Include
    private String path;
    private UnaryOperator<HttpClient> decorator;

    @Builder(toBuilder = true)
    private RsocketHttpClientConfiguration(int port,
                                           String host,
                                           String path,
                                           String connector,
                                           UnaryOperator<HttpClient> decorator,
                                           PayloadDecoderMode payloadDecoderMode,
                                           int maxInboundPayloadSize,
                                           int fragment,
                                           RsocketKeepAliveConfiguration keepAlive,
                                           RsocketResumeConfiguration resume,
                                           RsocketRetryConfiguration retry,
                                           boolean logging,
                                           DataFormat dataFormat,
                                           DataFormat metaDataFormat
    ) {
        this.path = path;
        this.decorator = decorator;
        common = RsocketCommonConnectorConfiguration.builder()
                .connector(connector)
                .port(port)
                .host(host)
                .payloadDecoderMode(payloadDecoderMode)
                .maxInboundPayloadSize(maxInboundPayloadSize)
                .fragment(fragment)
                .keepAlive(keepAlive)
                .resume(resume)
                .retry(retry)
                .logging(logging)
                .dataFormat(dataFormat)
                .metaDataFormat(metaDataFormat)
                .build();
    }

    public static RsocketHttpClientConfiguration defaults(String connector) {
        RsocketHttpClientConfiguration configuration = RsocketHttpClientConfiguration.builder().build();
        configuration.path = DEFAULT_HTTP_PATH;
        configuration.decorator = UnaryOperator.identity();
        configuration.common = RsocketCommonConnectorConfiguration.defaults(connector);
        return configuration;
    }

    public static RsocketHttpClientConfiguration from(RsocketModuleRefresher refresher, RsocketHttpClientConfiguration current, ConfigurationSource source) {
        RsocketHttpClientConfiguration configuration = RsocketHttpClientConfiguration.builder().build();
        configuration.decorator = current.decorator;
        configuration.path = orElse(source.getString(TRANSPORT_WS_PATH_KEY), current.path);
        configuration.common = RsocketCommonConnectorConfiguration.from(refresher, current.common, source);
        return configuration;
    }
}
