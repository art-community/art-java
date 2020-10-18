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
import io.art.core.printer.*;
import io.art.server.configuration.*;
import io.art.server.configuration.ServerModuleConfiguration.*;
import io.art.server.registry.*;
import io.art.server.state.*;
import lombok.*;
import static io.art.core.constants.StringConstants.EMPTY_STRING;
import static io.art.core.context.Context.*;
import static io.art.core.printer.ColoredPrinter.*;
import static io.art.server.constants.ServerModuleConstants.ConfigurationKeys.*;
import static lombok.AccessLevel.*;

@Getter
public class ServerModule implements StatefulModule<ServerModuleConfiguration, Configurator, ServerModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatefulModuleProxy<ServerModuleConfiguration, ServerModuleState> serverModule = context().getStatefulModule(ServerModule.class.getSimpleName());
    private final String id = ServerModule.class.getSimpleName();
    private final ServerModuleConfiguration configuration = new ServerModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    private final ServerModuleState state = new ServerModuleState();

    public static StatefulModuleProxy<ServerModuleConfiguration, ServerModuleState> serverModule() {
        return getServerModule();
    }

    public static ServiceSpecificationRegistry specifications() {
        return serverModule().state().getSpecifications();
    }

    @Override
    public String print() {
        if (configuration.getServices().isEmpty()) {
            return EMPTY_STRING;
        }
        ColoredPrinter printer = printer()
                .mainSection(ServerModule.class.getSimpleName())
                .tabulation(1)
                .subSection(SERVER_SERVICES_KEY)
                .tabulation(2);
        configuration.getServices().forEach((serviceId, service) -> {
            printer
                    .tabulation(2)
                    .subSection(serviceId)
                    .tabulation(3)
                    .value(DEACTIVATED_KEY, service.isDeactivated())
                    .subSection(METHODS_KEY)
                    .tabulation(4);
            service.getMethods().forEach((methodId, method) -> {
                printer
                        .tabulation(4)
                        .subSection(methodId)
                        .tabulation(5)
                        .value(DEACTIVATED_KEY, method.isDeactivated());
            });
        });
        return printer.print();
    }
}
