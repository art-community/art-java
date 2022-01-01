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
import io.art.core.property.*;
import io.art.core.source.*;
import io.art.http.refresher.*;
import lombok.*;
import reactor.netty.http.*;
import reactor.netty.http.server.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.CommonConfigurationKeys.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static io.art.http.constants.HttpModuleConstants.Defaults.*;
import static io.art.http.constants.HttpModuleConstants.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import static reactor.netty.http.HttpProtocol.*;
import java.time.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class HttpServerConfiguration {
    private UnaryOperator<HttpServer> decorator;
    private LazyProperty<ImmutableArray<HttpRouteConfiguration>> routes;
    private DataFormat defaultDataFormat;
    private int port;
    private boolean verbose;
    private boolean accessLog;
    private boolean wiretapLog;
    private boolean compress;
    private boolean forward;
    private Duration idleTimeout;
    private HttpProtocol protocol;
    private String host;

    public static HttpServerConfiguration httpServerConfiguration() {
        HttpServerConfiguration configuration = HttpServerConfiguration.builder().build();
        configuration.defaultDataFormat = JSON;
        configuration.verbose = false;
        configuration.wiretapLog = false;
        configuration.accessLog = false;
        configuration.compress = false;
        configuration.forward = true;
        configuration.protocol = HTTP11;
        configuration.decorator = UnaryOperator.identity();
        configuration.port = DEFAULT_PORT;
        configuration.host = BROADCAST_IP_ADDRESS;
        configuration.routes = lazy(ImmutableArray::emptyImmutableArray);
        return configuration;
    }

    public static HttpServerConfiguration httpServerConfiguration(HttpModuleRefresher refresher, HttpServerConfiguration current, ConfigurationSource source) {
        HttpServerConfiguration configuration = current.toBuilder().build();

        ChangesListener serverListener = refresher.serverListener();

        configuration.defaultDataFormat = serverListener.emit(dataFormat(source.getString(DATA_FORMAT_KEY), JSON));
        configuration.port = serverListener.emit(orElse(source.getInteger(PORT_KEY), current.port));
        configuration.host = serverListener.emit(orElse(source.getString(HOST_KEY), current.host));
        configuration.verbose = serverListener.emit(orElse(source.getBoolean(VERBOSE_KEY), current.verbose));
        configuration.accessLog = serverListener.emit(orElse(source.getBoolean(ACCESS_LOG_KEY), current.accessLog));
        configuration.wiretapLog = serverListener.emit(orElse(source.getBoolean(WIRETAP_LOG_KEY), current.wiretapLog));
        configuration.compress = serverListener.emit(orElse(source.getBoolean(COMPRESS_KEY), current.compress));
        configuration.forward = serverListener.emit(orElse(source.getBoolean(FORWARD_KEY), current.forward));
        configuration.idleTimeout = serverListener.emit(orElse(source.getDuration(IDLE_TIMEOUT_KEY), current.idleTimeout));
        configuration.protocol = serverListener.emit(httpProtocol(source.getString(PROTOCOL_KEY), current.protocol));
        configuration.routes = lazy(() -> merge(source.getNestedArray(ROUTES_SECTION, HttpServerConfiguration::routeConfiguration), current.routes.get()));

        return configuration;
    }

    private static HttpRouteConfiguration routeConfiguration(NestedConfiguration nested) {
        return HttpRouteConfiguration.routeConfiguration(HttpRouteConfiguration.routeConfiguration(), nested);
    }

}
