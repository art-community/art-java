/*
 * ART
 *
 * Copyright 2019-2022 ART
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
import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.http.path.*;
import io.art.http.refresher.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.netty.handler.codec.http.cookie.*;
import lombok.Builder;
import lombok.*;
import reactor.netty.http.client.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.constants.CommonConfigurationKeys.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.constants.ProtocolConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static io.art.http.constants.HttpModuleConstants.Defaults.*;
import static io.art.http.path.HttpCommunicationUri.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import java.time.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class HttpConnectorConfiguration {
    private String connector;
    private boolean retry;
    private boolean keepAlive;
    private boolean verbose;
    private boolean compress;
    private boolean wiretapLog;
    private boolean followRedirect;
    private UnaryOperator<HttpClient> decorator;
    private DataFormat inputDataFormat;
    private DataFormat outputDataFormat;
    private String url;
    private ImmutableMap<String, String> headers;
    private ImmutableMap<String, Cookie> cookies;
    private Duration responseTimeout;
    private HttpCommunicationUri uri;
    private int wsAggregateFrames;

    public static HttpConnectorConfiguration httpConnectorConfiguration(String connector) {
        HttpConnectorConfiguration configuration = HttpConnectorConfiguration.builder().build();
        configuration.connector = connector;
        configuration.verbose = false;
        configuration.compress = false;
        configuration.wiretapLog = false;
        configuration.followRedirect = true;
        configuration.retry = true;
        configuration.keepAlive = true;
        configuration.inputDataFormat = JSON;
        configuration.outputDataFormat = JSON;
        configuration.decorator = UnaryOperator.identity();
        configuration.url = HTTP_SCHEME + SCHEME_DELIMITER + LOCALHOST_IP_ADDRESS + COLON + DEFAULT_PORT;
        configuration.uri = manual(SLASH);
        configuration.headers = emptyImmutableMap();
        configuration.cookies = emptyImmutableMap();
        configuration.wsAggregateFrames = DEFAULT_AGGREGATE_FRAMES;
        return configuration;
    }

    public static HttpConnectorConfiguration httpConnectorConfiguration(HttpModuleRefresher refresher, HttpConnectorConfiguration current, ConfigurationSource source) {
        HttpConnectorConfiguration configuration = current.toBuilder().build();
        configuration.connector = source.getParent();

        ChangesListener listener = refresher.connectorListeners().listenerFor(configuration.connector);

        configuration.verbose = listener.emit(orElse(source.getBoolean(VERBOSE_KEY), current.verbose));
        configuration.compress = listener.emit(orElse(source.getBoolean(COMPRESS_KEY), current.compress));
        configuration.inputDataFormat = listener.emit(dataFormat(source.getString(OUTPUT_DATA_FORMAT_KEY), current.inputDataFormat));
        configuration.outputDataFormat = listener.emit(dataFormat(source.getString(OUTPUT_DATA_FORMAT_KEY), current.outputDataFormat));
        configuration.keepAlive = listener.emit(orElse(source.getBoolean(KEEP_ALIVE_KEY), current.keepAlive));
        configuration.retry = listener.emit(orElse(source.getBoolean(RETRY_KEY), current.retry));
        configuration.followRedirect = listener.emit(orElse(source.getBoolean(FOLLOW_REDIRECT_KEY), current.followRedirect));
        configuration.responseTimeout = listener.emit(orElse(source.getDuration(RESPONSE_TIMEOUT_KEY), current.responseTimeout));
        configuration.wiretapLog = listener.emit(orElse(source.getBoolean(WIRETAP_LOG_KEY), current.wiretapLog));
        configuration.url = listener.emit(orElse(source.getString(URL_KEY), current.url));
        apply(source.getString(URI_KEY), path -> configuration.uri = manual(path));
        configuration.wsAggregateFrames = orElse(source.getInteger(WS_AGGREGATE_FRAMES_KEY), orElse(current.wsAggregateFrames, DEFAULT_AGGREGATE_FRAMES));
        return configuration;
    }
}
