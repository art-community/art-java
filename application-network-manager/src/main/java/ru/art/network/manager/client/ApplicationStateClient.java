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

package ru.art.network.manager.client;

import ru.art.state.api.model.ModuleConnectionRequest;
import ru.art.state.api.model.ModuleEndpoint;
import static java.lang.System.getProperty;
import static ru.art.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.art.core.constants.ContextConstants.LOCAL_PROFILE;
import static ru.art.core.constants.SystemProperties.PROFILE_PROPERTY;
import static ru.art.grpc.server.module.GrpcServerModule.grpcServerModule;
import static ru.art.network.manager.module.NetworkManagerModule.networkManagerModule;
import static ru.art.service.ServiceController.executeServiceMethod;
import static ru.art.state.api.constants.StateApiConstants.NetworkServiceConstants.Methods.*;
import static ru.art.state.api.constants.StateApiConstants.NetworkServiceConstants.NETWORK_SERVICE_ID;

public interface ApplicationStateClient {
    static void connect() {
        executeServiceMethod(NETWORK_SERVICE_ID, CONNECT, ModuleConnectionRequest.builder()
                .modulePath(grpcServerModule().getPath())
                .profile(ifEmpty(getProperty(PROFILE_PROPERTY), LOCAL_PROFILE))
                .moduleEndpoint(ModuleEndpoint.builder()
                        .host(networkManagerModule().getIpAddress())
                        .port(grpcServerModule().getPort())
                        .build())
                .build());
    }

    static void incrementSession() {
        executeServiceMethod(NETWORK_SERVICE_ID, INCREMENT_SESSION, ModuleConnectionRequest.builder()
                .modulePath(grpcServerModule().getPath())
                .profile(ifEmpty(getProperty(PROFILE_PROPERTY), LOCAL_PROFILE))
                .moduleEndpoint(ModuleEndpoint.builder()
                        .host(networkManagerModule().getIpAddress())
                        .port(grpcServerModule().getPort())
                        .build())
                .build());
    }

    static void decrementSession() {
        executeServiceMethod(NETWORK_SERVICE_ID, DECREMENT_SESSION, ModuleConnectionRequest.builder()
                .modulePath(grpcServerModule().getPath())
                .profile(ifEmpty(getProperty(PROFILE_PROPERTY), LOCAL_PROFILE))
                .moduleEndpoint(ModuleEndpoint.builder()
                        .host(networkManagerModule().getIpAddress())
                        .port(grpcServerModule().getPort())
                        .build())
                .build());
    }
}