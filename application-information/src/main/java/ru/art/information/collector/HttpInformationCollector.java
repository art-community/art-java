/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.information.collector;

import lombok.experimental.*;
import ru.art.information.generator.*;
import ru.art.information.model.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.core.network.provider.IpAddressProvider.*;
import static ru.art.http.constants.HttpCommonConstants.*;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.information.constants.InformationModuleConstants.*;

@UtilityClass
public class HttpInformationCollector {
    public static HttpInformation collectHttpInformation() {
        return HttpInformation.builder().services(httpServices()
                .stream()
                .filter(service -> !service.getServiceId().contains(INFORMATION_PATH))
                .filter(service -> !service.getServiceId().contains(STATUS_PATH))
                .map(service -> HttpServiceInformation
                        .builder()
                        .id(service.getServiceId())
                        .methods(service.getHttpService()
                                .getHttpMethods()
                                .stream()
                                .map(method -> HttpServiceMethodInformation
                                        .builder()
                                        .id(method.getMethodId())
                                        .method(method.getMethodType().toString())
                                        .url(HTTP_SCHEME
                                                + SCHEME_DELIMITER
                                                + getIpAddress()
                                                + COLON
                                                + httpServerModule().getPort()
                                                + service.getHttpService().getPath()
                                                + method.getPath())
                                        .exampleRequest(doIfNotNull(method.getRequestMapper(), ExampleJsonGenerator::generateExampleJson, () -> EMPTY_STRING))
                                        .exampleResponse(doIfNotNull(method.getResponseMapper(), ExampleJsonGenerator::generateExampleJson, () -> EMPTY_STRING))
                                        .build())
                                .collect(toMap(HttpServiceMethodInformation::getId, identity()))
                        )
                        .build())
                .collect(toMap(HttpServiceInformation::getId, identity())))
                .build();
    }
}
