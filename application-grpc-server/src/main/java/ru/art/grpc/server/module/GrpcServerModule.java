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

package ru.art.grpc.server.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.grpc.server.configuration.GrpcServerModuleConfiguration;
import ru.art.grpc.server.state.GrpcServerModuleState;
import static ru.art.core.context.Context.context;
import static ru.art.grpc.server.configuration.GrpcServerModuleConfiguration.GrpcServerModuleDefaultConfiguration;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVER_MODULE_ID;

@Getter
public class GrpcServerModule implements Module<GrpcServerModuleConfiguration, GrpcServerModuleState> {
    private final GrpcServerModuleState state = new GrpcServerModuleState();
    private final String id = GRPC_SERVER_MODULE_ID;
    private final GrpcServerModuleConfiguration defaultConfiguration = new GrpcServerModuleDefaultConfiguration();

    public static GrpcServerModuleConfiguration grpcServerModule() {
        return context().getModule(GRPC_SERVER_MODULE_ID, GrpcServerModule::new);
    }

    public static GrpcServerModuleState grpcServerModuleState() {
        return context().getModuleState(GRPC_SERVER_MODULE_ID, GrpcServerModule::new);
    }
}
