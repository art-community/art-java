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

import io.art.core.model.*;
import io.art.core.source.*;
import io.art.http.strategy.*;
import io.art.transport.constants.TransportModuleConstants.*;
import lombok.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static io.art.http.constants.HttpModuleConstants.*;
import static io.art.http.strategy.RouteByServiceMethodStrategy.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.LOGGING_KEY;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import java.nio.file.*;

@Getter
@Builder(toBuilder = true)
public class HttpRouteConfiguration {
    private RouteByServiceMethodStrategy path;
    private HttpRouteType type;
    private boolean deactivated;
    private boolean logging;
    private DataFormat defaultDataFormat;
    private HttpWsRouteConfiguration wsConfiguration;
    private HttpPathRouteConfiguration pathConfiguration;
    private ServiceMethodIdentifier serviceMethodId;

    public static HttpRouteConfiguration routeConfiguration() {
        HttpRouteConfiguration configuration = HttpRouteConfiguration.builder().build();
        configuration.path = manual(SLASH);
        configuration.deactivated = false;
        configuration.logging = false;
        configuration.type = HttpRouteType.GET;
        configuration.defaultDataFormat = JSON;
        return configuration;
    }

    public static HttpRouteConfiguration routeConfiguration(HttpRouteConfiguration current, ConfigurationSource source) {
        HttpRouteConfiguration configuration = HttpRouteConfiguration.builder().build();
        configuration.deactivated = source.getBoolean(DEACTIVATED_KEY);
        configuration.logging = source.getBoolean(LOGGING_KEY);
        configuration.type = HttpRouteType.valueOf(source.getString(METHOD_KEY).toUpperCase());
        switch (configuration.type) {
            case DIRECTORY:
            case FILE:
                configuration.pathConfiguration = HttpPathRouteConfiguration.builder()
                        .path(Paths.get(source.getString(ROUTED_PATH_KEY)))
                        .build();
                break;
            case WEBSOCKET:
                configuration.wsConfiguration = HttpWsRouteConfiguration.builder()
                        .aggregateFrames(source.getInteger(WS_AGGREGATE_FRAMES_KEY))
                        .build();
                break;
        }
        String serviceId = source.getString(SERVICE_ID_KEY);
        String methodId = source.getString(METHOD_ID_KEY);

        if (isNotEmpty(serviceId) && isNotEmpty(methodId)) {
            configuration.serviceMethodId = serviceMethodId(serviceId, methodId);
            configuration.path = byServiceMethod();
        }
        apply(source.getString(PATH_KEY), path -> configuration.path = manual(path));

        return configuration;
    }

    @Getter
    @Builder(toBuilder = true)
    public static class HttpWsRouteConfiguration {
        private final int aggregateFrames;
    }

    @Getter
    @Builder(toBuilder = true)
    public static class HttpPathRouteConfiguration {
        private final Path path;
    }
}
