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

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.source.*;
import io.art.http.configuration.HttpWsRouteConfiguration.*;
import io.art.http.path.*;
import io.art.transport.constants.TransportModuleConstants.*;
import lombok.Builder;
import lombok.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static io.art.http.constants.HttpModuleConstants.*;
import static io.art.http.constants.HttpModuleConstants.HttpRouteType.*;
import static io.art.http.path.HttpServingUri.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import java.nio.file.*;

@Getter
@Builder(toBuilder = true)
public class HttpRouteConfiguration {
    private HttpServingUri uri;
    private HttpRouteType type;
    private DataFormat defaultDataFormat;
    private HttpWsRouteConfiguration wsConfiguration;
    private HttpPathRouteConfiguration pathConfiguration;
    private ImmutableSet<String> pathParameters;
    private ServiceMethodIdentifier serviceMethodId;

    public static HttpRouteConfiguration routeConfiguration() {
        HttpRouteConfiguration configuration = HttpRouteConfiguration.builder().build();
        configuration.uri = manual(SLASH);
        configuration.type = GET;
        configuration.defaultDataFormat = JSON;
        configuration.pathParameters = emptyImmutableSet();
        return configuration;
    }

    public static HttpRouteConfiguration routeConfiguration(HttpRouteConfiguration current, ConfigurationSource source) {
        HttpRouteConfiguration configuration = current.toBuilder().build();
        configuration.type = httpRouteType(source.getString(METHOD_KEY).toUpperCase(), configuration.type);
        configuration.defaultDataFormat = dataFormat(source.getString(DATA_FORMAT_KEY), current.defaultDataFormat);
        configuration.pathParameters = merge(immutableSetOf(source.getStringArray(PATH_PARAMETERS_KEY)), current.pathParameters);
        switch (configuration.type) {
            case PATH:
                Path path = let(source.getString(FILE_PATH_KEY),
                        Paths::get,
                        let(current.pathConfiguration, HttpPathRouteConfiguration::getPath)
                );
                configuration.pathConfiguration = HttpPathRouteConfiguration.builder()
                        .path(path)
                        .build();
                break;
            case WS:
                Integer aggregateFrames = source.getInteger(WS_AGGREGATE_FRAMES_KEY);
                HttpWsRouteConfigurationBuilder wsBuilder = HttpWsRouteConfiguration.builder();
                apply(aggregateFrames, wsBuilder::aggregateFrames);
                configuration.wsConfiguration = wsBuilder.build();
                break;
        }
        String serviceId = source.getString(SERVICE_ID_KEY);
        String methodId = source.getString(METHOD_ID_KEY);

        if (isNotEmpty(serviceId) && isNotEmpty(methodId)) {
            configuration.serviceMethodId = serviceMethodId(serviceId, methodId);
            configuration.uri = byServiceMethod();
        }
        apply(source.getString(URI_KEY), path -> configuration.uri = manual(path));

        return configuration;
    }
}
