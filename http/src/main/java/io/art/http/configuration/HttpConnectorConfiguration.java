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

package io.art.http.configuration;

import io.art.core.changes.*;
import io.art.core.source.*;
import io.art.http.refresher.*;
import io.art.transport.constants.TransportModuleConstants.*;
import lombok.*;
import reactor.netty.http.client.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.constants.ProtocolConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.http.configuration.HttpKeepAliveConfiguration.*;
import static io.art.http.configuration.HttpRetryConfiguration.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.LOGGING_KEY;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class HttpConnectorConfiguration {
    private String connector;
    private int maxInboundPayloadSize;
    private int fragment;
    private HttpKeepAliveConfiguration keepAlive;
    private HttpRetryConfiguration retry;
    private int tcpMaxFrameLength;
    private UnaryOperator<HttpClient> decorator;
    private String httpWebSocketPath;
    private boolean logging;
    private DataFormat dataFormat;
    private String url;

    public static HttpConnectorConfiguration connectorConfiguration(String connector) {
        HttpConnectorConfiguration configuration = HttpConnectorConfiguration.builder().build();
        configuration.connector = connector;
        configuration.logging = false;
        configuration.fragment = 0;
        configuration.maxInboundPayloadSize = Integer.MAX_VALUE;
        configuration.dataFormat = JSON;
        configuration.httpWebSocketPath = SLASH;
        configuration.decorator = UnaryOperator.identity();
        configuration.url = HTTP_SCHEME + SCHEME_DELIMITER + LOCALHOST_IP_ADDRESS + SLASH;
        return configuration;
    }

    public static HttpConnectorConfiguration connectorConfiguration(HttpModuleRefresher refresher, HttpConnectorConfiguration current, ConfigurationSource source) {
        HttpConnectorConfiguration configuration = current.toBuilder().build();
        configuration.connector = source.getParent();

        ChangesListener listener = refresher.connectorListeners().listenerFor(configuration.connector);
        ChangesListener loggingListener = refresher.connectorLoggingListeners().listenerFor(configuration.connector);

        configuration.logging = loggingListener.emit(orElse(source.getBoolean(LOGGING_KEY), current.logging));

        configuration.dataFormat = listener.emit(dataFormat(source.getString(DATA_FORMAT_KEY), current.dataFormat));
        configuration.maxInboundPayloadSize = listener.emit(orElse(source.getInteger(MAX_INBOUND_PAYLOAD_SIZE_KEY), current.maxInboundPayloadSize));
        configuration.fragment = listener.emit(orElse(source.getInteger(FRAGMENTATION_MTU_KEY), current.fragment));
        configuration.keepAlive = listener.emit(let(source.getNested(KEEP_ALIVE_SECTION), section -> httpKeepAlive(section, current.keepAlive), current.keepAlive));
        configuration.retry = listener.emit(let(source.getNested(RECONNECT_SECTION), section -> httpRetry(section, current.retry), current.retry));
        configuration.url = listener.emit(source.getString(TRANSPORT_WS_BASE_URL_KEY));

        return configuration;
    }
}
