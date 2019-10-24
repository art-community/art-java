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
import ru.art.grpc.server.specification.*;
import ru.art.information.generator.*;
import ru.art.information.model.*;
import static java.util.Objects.isNull;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.context.Context.context;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.core.network.provider.IpAddressProvider.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static ru.art.grpc.server.module.GrpcServerModule.*;
import static ru.art.service.ServiceModule.*;

@UtilityClass
public class GrpcInformationCollector {
    public static GrpcInformation collectGrpcInformation() {
        if (!context().hasModule(GRPC_SERVER_MODULE_ID) || isNull(grpcServerModuleState().getServer()) || !grpcServerModuleState().getServer().isWorking()) {
            return null;
        }
        return GrpcInformation.builder()
                .url(getIpAddress() + COLON + grpcServerModule().getPort() + grpcServerModule().getPath())
                .services(serviceModuleState()
                        .getServiceRegistry()
                        .getServices()
                        .values()
                        .stream()
                        .filter(service -> service.getServiceTypes().contains(GRPC_SERVICE_TYPE))
                        .map(service -> (GrpcServiceSpecification) service)
                        .map(service -> GrpcServiceInformation
                                .builder()
                                .id(service.getServiceId())
                                .methods(service.getGrpcService()
                                        .getGrpcMethods()
                                        .entrySet()
                                        .stream()
                                        .map(entry -> GrpcServiceMethodInformation
                                                .builder()
                                                .id(entry.getKey())
                                                .exampleRequest(doIfNotNull(entry.getValue().requestMapper(), ExampleJsonGenerator::generateExampleJson, () -> EMPTY_STRING))
                                                .exampleResponse(doIfNotNull(entry.getValue().responseMapper(), ExampleJsonGenerator::generateExampleJson, () -> EMPTY_STRING))
                                                .build())
                                        .collect(toMap(GrpcServiceMethodInformation::getId, identity()))
                                )
                                .build())
                        .collect(toMap(GrpcServiceInformation::getId, identity())))
                .build();
    }
}
