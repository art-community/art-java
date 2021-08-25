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
import lombok.Builder;
import lombok.*;
import reactor.netty.http.server.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.constants.CommonConfigurationKeys.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.http.configuration.HttpRouteConfiguration.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.LOGGING_KEY;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static io.art.http.constants.HttpModuleConstants.Defaults.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class HttpServerConfiguration {
    private UnaryOperator<HttpServer> decorator;
    private ImmutableMap<String, HttpRouteConfiguration> routes;
    private boolean logging;
    private int fragmentationMtu;
    private DataFormat defaultDataFormat;
    private final Function<? extends Throwable, ?> exceptionMapper;
    private int port;
    private String host;

    public static HttpServerConfiguration httpServerConfiguration() {
        HttpServerConfiguration configuration = HttpServerConfiguration.builder().build();
        configuration.defaultDataFormat = JSON;
        configuration.logging = false;
        configuration.fragmentationMtu = 0;
        configuration.decorator = UnaryOperator.identity();
        configuration.port = DEFAULT_PORT;
        configuration.host = BROADCAST_IP_ADDRESS;
        configuration.routes = emptyImmutableMap();
        return configuration;
    }

    public static HttpServerConfiguration httpServerConfiguration(HttpModuleRefresher refresher, HttpServerConfiguration current, ConfigurationSource source) {
        HttpServerConfiguration configuration = HttpServerConfiguration.builder().build();

        ChangesListener serverListener = refresher.serverListener();
        ChangesListener serverLoggingListener = refresher.serverLoggingListener();

        configuration.logging = serverLoggingListener.emit(orElse(source.getBoolean(LOGGING_KEY), false));

        configuration.defaultDataFormat = serverListener.emit(dataFormat(source.getString(DATA_FORMAT_KEY), JSON));
        configuration.fragmentationMtu = serverListener.emit(orElse(source.getInteger(FRAGMENTATION_MTU_KEY), 0));

        configuration.port = serverListener.emit(orElse(source.getInteger(PORT_KEY), current.port));
        configuration.host = serverListener.emit(orElse(source.getString(HOST_KEY), current.host));
        configuration.routes = source.getNestedMap(ROUTES_SECTION, nested -> routeConfiguration(routeConfiguration(), nested));

        return configuration;
    }

}
