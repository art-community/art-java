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
import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.http.refresher.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.*;
import reactor.netty.http.client.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.CommonConfigurationKeys.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.constants.ProtocolConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import java.time.*;
import java.util.*;
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
    private DataFormat dataFormat;
    private String url;
    private ImmutableMap<String, String> headers;
    private ImmutableMap<String, Cookie> cookies;
    private Duration responseTimeout;

    public static HttpConnectorConfiguration connectorConfiguration(String connector) {
        HttpConnectorConfiguration configuration = HttpConnectorConfiguration.builder().build();
        configuration.connector = connector;
        configuration.verbose = false;
        configuration.compress = false;
        configuration.wiretapLog = false;
        configuration.followRedirect = true;
        configuration.retry = true;
        configuration.keepAlive = true;
        configuration.dataFormat = JSON;
        configuration.decorator = UnaryOperator.identity();
        configuration.url = HTTP_SCHEME + SCHEME_DELIMITER + LOCALHOST_IP_ADDRESS + SLASH;
        return configuration;
    }

    public static HttpConnectorConfiguration connectorConfiguration(HttpModuleRefresher refresher, HttpConnectorConfiguration current, ConfigurationSource source) {
        HttpConnectorConfiguration configuration = current.toBuilder().build();
        configuration.connector = source.getParent();

        ChangesListener listener = refresher.connectorListeners().listenerFor(configuration.connector);

        configuration.verbose = listener.emit(orElse(source.getBoolean(VERBOSE_KEY), current.verbose));
        configuration.compress = listener.emit(orElse(source.getBoolean(COMPRESS_KEY), current.compress));
        configuration.dataFormat = listener.emit(dataFormat(source.getString(DATA_FORMAT_KEY), current.dataFormat));
        configuration.keepAlive = listener.emit(orElse(source.getBoolean(KEEP_ALIVE_KEY), current.keepAlive));
        configuration.retry = listener.emit(orElse(source.getBoolean(RETRY_KEY), current.retry));
        configuration.followRedirect = listener.emit(orElse(source.getBoolean(FOLLOW_REDIRECT_KEY), current.followRedirect));
        configuration.responseTimeout = listener.emit(orElse(source.getDuration(RESPONSE_TIMEOUT_KEY), current.responseTimeout));
        configuration.wiretapLog = listener.emit(orElse(source.getBoolean(WIRETAP_LOG_KEY), current.wiretapLog));
        configuration.url = listener.emit(source.getString(URL_KEY));

        return configuration;
    }
}
