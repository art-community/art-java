/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.http.server.module;

import lombok.*;
import io.art.core.module.Module;
import io.art.http.server.*;
import io.art.http.server.specification.*;
import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.*;
import static io.art.core.context.Context.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.http.server.HttpServerModuleConfiguration.*;
import static io.art.http.server.constants.HttpServerModuleConstants.*;
import java.util.*;

@Getter
public class HttpServerModule implements Module<HttpServerModuleConfiguration, HttpServerModuleState> {
    @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}), value = PRIVATE)
    private final static List<HttpServiceSpecification> httpServices = serviceModuleState().getServiceRegistry()
            .getServices()
            .values()
            .stream()
            .filter(service -> service.getServiceTypes().contains(HTTP_SERVICE_TYPE))
            .map(service -> (HttpServiceSpecification) service)
            .collect(toList());
    @Getter(lazy = true, value = PRIVATE)
    private final static HttpServerModuleConfiguration httpServerModule = context().getModule(HTTP_SERVER_MODULE_ID, HttpServerModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private final static HttpServerModuleState httpServerModuleState = context().getModuleState(HTTP_SERVER_MODULE_ID, HttpServerModule::new);
    private final String id = HTTP_SERVER_MODULE_ID;
    private final HttpServerModuleConfiguration defaultConfiguration = DEFAULT_CONFIGURATION;
    private final HttpServerModuleState state = new HttpServerModuleState();

    public static HttpServerModuleConfiguration httpServerModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
        }
        return getHttpServerModule();
    }

    public static List<HttpServiceSpecification> httpServices() {
        return getHttpServices();
    }

    public static HttpServerModuleState httpServerModuleState() {
        return getHttpServerModuleState();
    }

    @Override
    public void onUnload() {
        let(httpServerModuleState().getServer(), HttpServer::stop);
    }
}
