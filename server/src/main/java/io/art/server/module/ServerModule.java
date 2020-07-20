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
import io.art.server.configuration.*;
import io.art.server.registry.*;
import io.art.server.service.specification.*;
import io.art.server.state.*;
import lombok.*;
import static io.art.core.context.Context.*;
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
        services()
                .register(ServiceSpecification.builder()
                        .id("id")
                        .method("id", ServiceMethodSpecification.builder()
                                .implementation(handler(request -> request, "id", "id"))
                                .build())
                        .build())
                .register(ServiceSpecification.builder()
                        .id("id")
                        .method("id", ServiceMethodSpecification.builder()
                                .implementation(handler(request -> request, "id", "id"))
                                .build())
                        .build())
                .register(ServiceSpecification.builder()
                        .id("id")
                        .method("id", ServiceMethodSpecification.builder()
                                .implementation(handler(request -> request, "id", "id"))
                                .build())
                        .build());
    }
}
