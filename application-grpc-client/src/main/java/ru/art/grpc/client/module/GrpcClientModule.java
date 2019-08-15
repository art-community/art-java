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

package ru.art.grpc.client.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.grpc.client.configuration.GrpcClientModuleConfiguration;
import ru.art.grpc.client.constants.GrpcClientModuleConstants;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.grpc.client.configuration.GrpcClientModuleConfiguration.GrpcClientModuleDefaultConfiguration;
import static ru.art.grpc.client.constants.GrpcClientModuleConstants.GRPC_CLIENT_MODULE_ID;

@Getter
public class GrpcClientModule implements Module<GrpcClientModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final GrpcClientModuleConfiguration grpcModule = context().getModule(GRPC_CLIENT_MODULE_ID, GrpcClientModule::new);
    private final String id = GrpcClientModuleConstants.GRPC_CLIENT_MODULE_ID;
    private final GrpcClientModuleConfiguration defaultConfiguration = new GrpcClientModuleDefaultConfiguration();

    public static GrpcClientModuleConfiguration grpcClientModule() {
        return getGrpcModule();
    }
}
