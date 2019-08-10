/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.http.server.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.http.server.HttpServerModuleConfiguration;
import ru.art.http.server.HttpServerModuleState;
import ru.art.http.server.specification.HttpServiceSpecification;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.http.server.constants.HttpServerModuleConstants.HTTP_SERVER_MODULE_ID;
import static ru.art.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import static ru.art.service.ServiceModule.serviceModule;
import java.util.List;

@Getter
public class HttpServerModule implements Module<HttpServerModuleConfiguration, HttpServerModuleState> {
    @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}), value = PRIVATE)
    private final static List<HttpServiceSpecification> httpServices = serviceModule().getServiceRegistry()
            .getServices()
            .values()
            .stream()
            .filter(service -> service.getServiceTypes().contains(HTTP_SERVICE_TYPE))
            .map(service -> (HttpServiceSpecification) service)
            .collect(toList());
    @Getter(lazy = true, value = PRIVATE)
    private final static HttpServerModuleConfiguration httpServerModule = context().getModule(HTTP_SERVER_MODULE_ID, new HttpServerModule());
    @Getter
    private final HttpServerModuleConfiguration defaultConfiguration = new HttpServerModuleConfiguration.HttpServerModuleDefaultConfiguration();
    @Getter
    private final HttpServerModuleState state = new HttpServerModuleState();

    private final String id = HTTP_SERVER_MODULE_ID;

    public static HttpServerModuleConfiguration httpServerModule() {
        return getHttpServerModule();
    }

    public static List<HttpServiceSpecification> httpServices() {
        return getHttpServices();
    }

    public static HttpServerModuleState httpServerModuleState() {
        return context().getModuleState(HTTP_SERVER_MODULE_ID, new HttpServerModule());
    }
}
