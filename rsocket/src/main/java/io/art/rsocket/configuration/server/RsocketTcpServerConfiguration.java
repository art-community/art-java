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

import io.art.core.source.*;
import io.art.rsocket.refresher.*;
import lombok.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.rsocket.frame.FrameLengthCodec.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class RsocketTcpServerConfiguration {
    private int maxFrameLength;
    private RsocketCommonServerConfiguration common;
    private UnaryOperator<TcpServer> decorator;

    public static RsocketTcpServerConfiguration defaults() {
        RsocketTcpServerConfiguration configuration = RsocketTcpServerConfiguration.builder().build();
        configuration.decorator = UnaryOperator.identity();
        configuration.maxFrameLength = FRAME_LENGTH_MASK;
        configuration.common = RsocketCommonServerConfiguration.defaults();
        return configuration;
    }

    public static RsocketTcpServerConfiguration from(RsocketModuleRefresher refresher, RsocketTcpServerConfiguration current, ConfigurationSource source) {
        RsocketTcpServerConfiguration configuration = RsocketTcpServerConfiguration.builder().build();
        configuration.decorator = current.decorator;
        configuration.common = RsocketCommonServerConfiguration.from(refresher, current.common, source);
        configuration.maxFrameLength = refresher.serverListener().emit(orElse(source.getInteger(TRANSPORT_TCP_MAX_FRAME_LENGTH), current.maxFrameLength));
        return configuration;
    }
}
