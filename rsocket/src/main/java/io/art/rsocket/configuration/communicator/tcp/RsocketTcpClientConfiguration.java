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

package io.art.rsocket.configuration.communicator.tcp;

import io.art.core.changes.*;
import io.art.core.source.*;
import io.art.rsocket.refresher.*;
import lombok.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.rsocket.frame.FrameLengthCodec.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RsocketTcpClientConfiguration {
    @EqualsAndHashCode.Include
    private String connector;
    @EqualsAndHashCode.Include
    private int port;
    @EqualsAndHashCode.Include
    private String host;
    private int maxFrameLength;
    private UnaryOperator<TcpClient> decorator;

    public static RsocketTcpClientConfiguration defaults(String connector) {
        RsocketTcpClientConfiguration configuration = RsocketTcpClientConfiguration.builder().build();
        configuration.maxFrameLength = FRAME_LENGTH_MASK;
        configuration.decorator = UnaryOperator.identity();
        configuration.connector = connector;
        configuration.port = DEFAULT_PORT;
        configuration.host = LOCALHOST;
        return configuration;
    }

    public static RsocketTcpClientConfiguration from(RsocketModuleRefresher refresher, RsocketTcpClientConfiguration current, ConfigurationSource source) {
        RsocketTcpClientConfiguration configuration = RsocketTcpClientConfiguration.builder().build();
        ChangesListener listener = refresher.connectorListeners().listenerFor(current.connector);
        configuration.decorator = current.decorator;
        configuration.maxFrameLength = orElse(source.getInteger(TRANSPORT_TCP_MAX_FRAME_LENGTH), current.maxFrameLength);
        configuration.connector = current.connector;
        configuration.port = listener.emit(orElse(source.getInteger(TRANSPORT_PORT_KEY), current.port));
        configuration.host = listener.emit(orElse(source.getString(TRANSPORT_HOST_KEY), current.host));
        return configuration;
    }
}