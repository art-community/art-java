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

package io.art.http.router;

import io.art.core.mime.*;
import io.art.core.model.*;
import io.art.http.configuration.*;
import io.art.logging.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.server.method.*;
import io.art.transport.constants.TransportModuleConstants.*;
import reactor.netty.http.server.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.http.constants.HttpModuleConstants.*;
import static io.art.http.constants.HttpModuleConstants.Messages.*;
import static io.art.http.constants.HttpModuleConstants.Warnings.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.transport.mime.MimeTypeDataFormatMapper.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.nio.file.*;

public class HttpRouter {
    private final HttpServerConfiguration httpConfiguration;

    public HttpRouter(HttpServerRoutes routes, HttpServerConfiguration httpConfiguration, ServerConfiguration serverConfiguration) {
        this.httpConfiguration = httpConfiguration;
        setupRoutes(routes, serverConfiguration);
    }

    private void setupRoutes(HttpServerRoutes routes, ServerConfiguration serverConfiguration) {
        for (HttpRouteConfiguration route : httpConfiguration.getRoutes().get()) {
            HttpRouteType httpMethodType = route.getType();
            ServiceMethodIdentifier serviceMethodId = route.getServiceMethodId();
            StringBuilder path = new StringBuilder(route.getPath().make(serviceMethodId));
            for (String parameter : route.getPathParameters()) {
                path.append(SLASH).append(OPENING_BRACES).append(parameter).append(CLOSING_BRACES);
            }
            ServiceMethod serviceMethod = serverConfiguration.getMethods().get().get(serviceMethodId);
            switch (httpMethodType) {
                case GET:
                    routes.get(path.toString(), handleHttp(serviceMethod, route));
                    break;
                case POST:
                    routes.post(path.toString(), handleHttp(serviceMethod, route));
                    break;
                case PUT:
                    routes.put(path.toString(), handleHttp(serviceMethod, route));
                    break;
                case DELETE:
                    routes.delete(path.toString(), handleHttp(serviceMethod, route));
                    break;
                case OPTIONS:
                    routes.options(path.toString(), handleHttp(serviceMethod, route));
                    break;
                case HEAD:
                    routes.head(path.toString(), handleHttp(serviceMethod, route));
                    break;
                case WS:
                    routes.ws(path.toString(), handleWebsocket(serviceMethod, route));
                    break;
                case PATH:
                    Path routePath = route.getPathConfiguration().getPath();
                    if (withLogging() && !routePath.toFile().exists()) {
                        Logging.logger(HTTP_SERVER_LOGGER).warn(format(ROUTE_PATH_NOT_EXISTS, routePath.toAbsolutePath()));
                        break;
                    }
                    if (routePath.toFile().isDirectory()) {
                        routes.directory(path.toString(), routePath);
                        break;
                    }
                    routes.file(path.toString(), routePath);
                    break;
            }
        }
    }

    private WsRouting handleWebsocket(ServiceMethod serviceMethod, HttpRouteConfiguration routeConfiguration) {
        DataFormat defaultDataFormat = orElse(routeConfiguration.getDefaultDataFormat(), httpConfiguration.getDefaultDataFormat());
        MimeType defaultMimeType = toMimeType(defaultDataFormat);
        MetaType<?> inputMappingType = serviceMethod.getInputType();
        if (nonNull(inputMappingType) && (inputMappingType.internalKind() == MONO || inputMappingType.internalKind() == FLUX)) {
            inputMappingType = inputMappingType.parameters().get(0);
        }
        MetaType<?> outputMappingType = serviceMethod.getOutputType();
        if (nonNull(outputMappingType) && (outputMappingType.internalKind() == MONO || outputMappingType.internalKind() == FLUX)) {
            outputMappingType = outputMappingType.parameters().get(0);
        }
        return WsRouting.builder()
                .serviceMethod(serviceMethod)
                .routeConfiguration(routeConfiguration)
                .defaultMimeType(defaultMimeType)
                .inputMappingType(inputMappingType)
                .outputMappingType(outputMappingType)
                .build();
    }

    private HttpRouting handleHttp(ServiceMethod serviceMethod, HttpRouteConfiguration routeConfiguration) {
        DataFormat defaultDataFormat = orElse(routeConfiguration.getDefaultDataFormat(), httpConfiguration.getDefaultDataFormat());
        MimeType defaultMimeType = toMimeType(defaultDataFormat);
        MetaType<?> inputMappingType = serviceMethod.getInputType();
        if (nonNull(inputMappingType) && (inputMappingType.internalKind() == MONO || inputMappingType.internalKind() == FLUX)) {
            inputMappingType = inputMappingType.parameters().get(0);
        }
        MetaType<?> outputMappingType = serviceMethod.getOutputType();
        if (nonNull(outputMappingType) && (outputMappingType.internalKind() == MONO || outputMappingType.internalKind() == FLUX)) {
            outputMappingType = outputMappingType.parameters().get(0);
        }
        return HttpRouting.builder()
                .serviceMethod(serviceMethod)
                .routeConfiguration(routeConfiguration)
                .defaultMimeType(defaultMimeType)
                .inputMappingType(inputMappingType)
                .outputMappingType(outputMappingType)
                .build();
    }
}
