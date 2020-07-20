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

package io.art.server.module;

import io.art.core.module.*;
import io.art.logging.*;
import io.art.resilience.module.*;
import io.art.server.configuration.*;
import io.art.server.interceptor.*;
import io.art.server.registry.*;
import io.art.server.service.specification.*;
import io.art.server.state.*;
import lombok.*;
import static com.google.common.base.Throwables.*;
import static io.art.core.context.Context.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.Value.*;
import static io.art.entity.mapping.PrimitiveMapping.*;
import static io.art.logging.LoggingModule.*;
import static io.art.server.constants.ServerModuleConstants.ServiceMethodProcessingMode.*;
import static io.art.server.service.implementation.ServiceMethodImplementation.*;
import static lombok.AccessLevel.*;

@Getter
public class ServerModule implements StatefulModule<ServerModuleConfiguration, ServerModuleConfiguration.Configurator, ServerModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatefulModuleProxy<ServerModuleConfiguration, ServerModuleState> serverModule = context().getStatefulModule(ServerModule.class.getSimpleName());
    private final String id = ServerModule.class.getSimpleName();
    private final ServerModuleConfiguration configuration = new ServerModuleConfiguration();
    private final ServerModuleConfiguration.Configurator configurator = new ServerModuleConfiguration.Configurator(configuration);
    private final ServerModuleState state = new ServerModuleState();

    public static StatefulModuleProxy<ServerModuleConfiguration, ServerModuleState> serverModule() {
        return getServerModule();
    }

    public static ServiceRegistry services() {
        return serverModule().state().getServices();
    }

    public static void main(String[] args) {
        context()
                .loadModule(new ResilienceModule())
                .loadModule(new ServerModule())
                .loadModule(new LoggingModule());
        ServiceSpecification specification = services()
                .register(ServiceSpecification.builder()
                        .id("id-1")
                        .method("id", ServiceMethodSpecification.builder()
                                .requestProcessingMode(BLOCKING)
                                .responseProcessingMode(BLOCKING)
                                .requestMapper(value -> toString.map(asPrimitive(value)))
                                .exceptionMapper(model -> fromString.map(getStackTraceAsString(model)))
                                .responseMapper(model -> fromString.map((String) model))
                                .implementation(handler(request -> request, "id-1", "id"))
                                .build())
                        .build())
                .register(ServiceSpecification.builder()
                        .id("id-2")
                        .method("id", ServiceMethodSpecification.builder()
                                .implementation(handler(request -> request, "id", "id"))
                                .build())
                        .build())
                .register(ServiceSpecification.builder()
                        .id("id-3")
                        .method("id", ServiceMethodSpecification.builder()
                                .implementation(handler(request -> request, "id", "id"))
                                .build())
                        .build())
                .get("id-1");
        logger().info(specification.executeBlocking("id", stringPrimitive("test")));
    }
}
