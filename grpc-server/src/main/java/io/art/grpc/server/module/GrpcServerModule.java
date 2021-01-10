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

package io.art.grpc.server.module;

import lombok.*;
import io.art.core.module.Module;
import io.art.grpc.server.*;
import io.art.grpc.server.configuration.*;
import io.art.grpc.server.state.*;
import static lombok.AccessLevel.*;
import static io.art.core.context.Context.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.grpc.server.configuration.GrpcServerModuleConfiguration.*;
import static io.art.grpc.server.constants.GrpcServerModuleConstants.*;

@Getter
public class GrpcServerModule implements Module<GrpcServerModuleConfiguration, GrpcServerModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final GrpcServerModuleConfiguration grpcServerModule = context().getModule(GRPC_SERVER_MODULE_ID, GrpcServerModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private static final GrpcServerModuleState grpcServerModuleState = context().getModuleState(GRPC_SERVER_MODULE_ID, GrpcServerModule::new);
    private final String id = GRPC_SERVER_MODULE_ID;
    private final GrpcServerModuleConfiguration defaultConfiguration = GrpcServerModuleDefaultConfiguration.DEFAULT_CONFIGURATION;
    private final GrpcServerModuleState state = new GrpcServerModuleState();

    public static GrpcServerModuleConfiguration grpcServerModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
        }
        return getGrpcServerModule();
    }

    public static GrpcServerModuleState grpcServerModuleState() {
        return getGrpcServerModuleState();
    }

    @Override
    public void beforeUnload() {
        let(grpcServerModuleState().getServer(), GrpcServer::stop);
    }
}
