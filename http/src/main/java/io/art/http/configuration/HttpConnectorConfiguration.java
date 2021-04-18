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
import io.art.http.constants.HttpModuleConstants.*;
import io.art.http.refresher.*;
import lombok.*;
import reactor.netty.http.client.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.http.configuration.HttpKeepAliveConfiguration.*;
import static io.art.http.configuration.HttpRetryConfiguration.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static io.art.http.constants.HttpModuleConstants.PayloadDecoderMode.*;
import static io.art.value.constants.ValueModuleConstants.*;
import static io.art.value.constants.ValueModuleConstants.DataFormat.*;
import static reactor.netty.http.client.HttpClient.*;

@Getter
@RequiredArgsConstructor
public class HttpConnectorConfiguration {
    private String connectorId;
    private PayloadDecoderMode payloadDecoderMode;
    private int maxInboundPayloadSize;
    private int fragment;
    private HttpKeepAliveConfiguration keepAlive;
    private HttpRetryConfiguration retry;
    private int tcpMaxFrameLength;
    private HttpClient httpClient;
    private String httpWebSocketPath;
    private boolean logging;
    private DataFormat dataFormat;

    public static HttpConnectorConfiguration defaults() {
        HttpConnectorConfiguration configuration = new HttpConnectorConfiguration();
        configuration.logging = false;
        configuration.fragment = 0;
        configuration.maxInboundPayloadSize = Integer.MAX_VALUE;
        configuration.payloadDecoderMode = DEFAULT;
        configuration.dataFormat = JSON;
        configuration.httpWebSocketPath = SLASH;
        return configuration;
    }

    public static HttpConnectorConfiguration httpConnector(ConfigurationSource source) {
        HttpConnectorConfiguration configuration = new HttpConnectorConfiguration();
        HttpConnectorConfiguration defaults = defaults();

        configuration.logging = orElse(source.getBool(LOGGING_KEY), defaults.logging);
        configuration.fragment = orElse(source.getInt(FRAGMENTATION_MTU_KEY), defaults.fragment);
        configuration.maxInboundPayloadSize = orElse(source.getInt(MAX_INBOUND_PAYLOAD_SIZE_KEY), defaults.maxInboundPayloadSize);
        configuration.payloadDecoderMode = httpPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY), defaults.payloadDecoderMode);
        configuration.dataFormat = dataFormat(source.getString(DATA_FORMAT_KEY), defaults.dataFormat);
        configuration.keepAlive = source.getNested(KEEP_ALIVE_SECTION, HttpKeepAliveConfiguration::httpKeepAlive);
        configuration.retry = source.getNested(RECONNECT_SECTION, HttpRetryConfiguration::httpRetry);

        if (!source.has(TRANSPORT_SECTION)) {
            configuration.tcpMaxFrameLength = defaults.tcpMaxFrameLength;
            configuration.httpWebSocketPath = defaults.httpWebSocketPath;
            return configuration;
        }

        String url = source.getString(TRANSPORT_WS_BASE_URL_KEY);
        configuration.httpClient = create().baseUrl(url);

        return configuration;
    }

    public static HttpConnectorConfiguration httpConnector(HttpModuleRefresher refresher, HttpConnectorConfiguration defaults, ConfigurationSource source) {
        HttpConnectorConfiguration configuration = new HttpConnectorConfiguration();
        configuration.connectorId = source.getParent();

        ChangesListener listener = refresher.connectorListeners().listenerFor(configuration.connectorId);
        ChangesListener loggingListener = refresher.connectorLoggingListeners().listenerFor(configuration.connectorId);

        configuration.logging = loggingListener.emit(orElse(source.getBool(LOGGING_KEY), defaults.logging));

        configuration.dataFormat = listener.emit(dataFormat(source.getString(DATA_FORMAT_KEY), defaults.dataFormat));
        configuration.payloadDecoderMode = listener.emit(httpPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY), defaults.payloadDecoderMode));
        configuration.maxInboundPayloadSize = listener.emit(orElse(source.getInt(MAX_INBOUND_PAYLOAD_SIZE_KEY), defaults.maxInboundPayloadSize));
        configuration.fragment = listener.emit(orElse(source.getInt(FRAGMENTATION_MTU_KEY), defaults.fragment));
        configuration.keepAlive = listener.emit(let(source.getNested(KEEP_ALIVE_SECTION), section -> httpKeepAlive(section, defaults.keepAlive), defaults.keepAlive));
        configuration.retry = listener.emit(let(source.getNested(RECONNECT_SECTION), section -> httpRetry(section, defaults.retry), defaults.retry));

        String url = listener.emit(source.getString(TRANSPORT_WS_BASE_URL_KEY));
        configuration.httpClient = orElse(configuration.httpClient, create().baseUrl(url));

        return configuration;
    }
}
