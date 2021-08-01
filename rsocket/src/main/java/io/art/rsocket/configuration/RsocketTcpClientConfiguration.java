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
import reactor.netty.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.rsocket.frame.FrameLengthCodec.*;
import java.util.function.*;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RsocketTcpClientConfiguration {
    @EqualsAndHashCode.Include
    private RsocketCommonConnectorConfiguration common;
    private int maxFrameLength;
    private UnaryOperator<TcpClient> decorator;

    @Builder(toBuilder = true)
    private RsocketTcpClientConfiguration(int port,
                                          String host,
                                          UnaryOperator<TcpClient> decorator,
                                          String connector,
                                          PayloadDecoderMode payloadDecoderMode,
                                          int maxInboundPayloadSize,
                                          int fragment,
                                          RsocketKeepAliveConfiguration keepAlive,
                                          RsocketResumeConfiguration resume,
                                          RsocketRetryConfiguration retry,
                                          boolean logging,
                                          DataFormat dataFormat,
                                          DataFormat metaDataFormat,
                                          int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
        this.decorator = decorator;
        common = RsocketCommonConnectorConfiguration.builder()
                .port(port)
                .host(host)
                .connector(connector)
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

    public static RsocketTcpClientConfiguration defaults(String connector) {
        RsocketTcpClientConfiguration configuration = RsocketTcpClientConfiguration.builder().build();
        configuration.maxFrameLength = FRAME_LENGTH_MASK;
        configuration.decorator = UnaryOperator.identity();
        configuration.common = RsocketCommonConnectorConfiguration.defaults(connector);
        return configuration;
    }

    public static RsocketTcpClientConfiguration from(RsocketModuleRefresher refresher, RsocketTcpClientConfiguration current, ConfigurationSource source) {
        RsocketTcpClientConfiguration configuration = RsocketTcpClientConfiguration.builder().build();
        configuration.decorator = current.decorator;
        configuration.maxFrameLength = orElse(source.getInteger(TRANSPORT_TCP_MAX_FRAME_LENGTH), current.maxFrameLength);
        configuration.common = RsocketCommonConnectorConfiguration.from(refresher, current.common, source);
        return configuration;
    }
}
