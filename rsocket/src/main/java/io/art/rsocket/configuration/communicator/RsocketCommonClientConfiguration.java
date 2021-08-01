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

package io.art.rsocket.configuration.communicator;

import io.art.core.changes.*;
import io.art.core.source.*;
import io.art.rsocket.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RsocketCommonClientConfiguration {
    private String connector;
    private int port;
    private String host;

    public static RsocketCommonClientConfiguration defaults(String connector) {
        RsocketCommonClientConfiguration configuration = RsocketCommonClientConfiguration.builder().build();
        configuration.connector = connector;
        configuration.port = DEFAULT_PORT;
        configuration.host = LOCALHOST;
        return configuration;
    }

    public static RsocketCommonClientConfiguration from(RsocketModuleRefresher refresher, RsocketCommonClientConfiguration current, ConfigurationSource source) {
        RsocketCommonClientConfiguration configuration = RsocketCommonClientConfiguration.builder().build();
        ChangesListener listener = refresher.connectorListeners().listenerFor(current.connector);
        configuration.connector = current.connector;
        configuration.port = listener.emit(orElse(source.getInteger(TRANSPORT_PORT_KEY), current.port));
        configuration.host = listener.emit(orElse(source.getString(TRANSPORT_HOST_KEY), current.host));
        return configuration;
    }
}
