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

package ru.art.config.extensions.grpc;

import lombok.Getter;
import ru.art.grpc.server.configuration.GrpcServerModuleConfiguration.GrpcServerModuleDefaultConfiguration;
import static java.util.Objects.isNull;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.grpc.GrpcConfigKeys.GRPC_SERVER_CONFIG_SECTION_ID;
import static ru.art.config.extensions.grpc.GrpcConfigKeys.HANDSHAKE_TIMEOUT;
import static ru.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.art.core.context.Context.context;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVER_MODULE_ID;
import static ru.art.grpc.server.module.GrpcServerModule.grpcServerModuleState;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Getter
public class GrpcServerAgileConfiguration extends GrpcServerModuleDefaultConfiguration {
    private int port;
    private String path;
    private int handshakeTimeout;
    private Executor overridingExecutor;
    private boolean enableTracing;

    public GrpcServerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        int newPort = configInt(GRPC_SERVER_CONFIG_SECTION_ID, PORT, super.getPort());
        boolean restart = port != newPort;
        port = newPort;
        int newHandshakeTimeout = configInt(GRPC_SERVER_CONFIG_SECTION_ID, HANDSHAKE_TIMEOUT, super.getHandshakeTimeout());
        restart |= handshakeTimeout != newHandshakeTimeout;
        handshakeTimeout = newHandshakeTimeout;
        String newPath = configString(GRPC_SERVER_CONFIG_SECTION_ID, PATH, super.getPath());
        restart |= !newPath.equals(path);
        path = newPath;
        int newPoolSize = configInt(GRPC_SERVER_CONFIG_SECTION_ID, THREAD_POOL_SIZE, DEFAULT_THREAD_POOL_SIZE);
        restart |= isNull(overridingExecutor) || ((ThreadPoolExecutor) overridingExecutor).getCorePoolSize() != newPoolSize;
        overridingExecutor = newFixedThreadPool(newPoolSize);
        enableTracing = configBoolean(GRPC_SERVER_CONFIG_SECTION_ID, ENABLE_TRACING, super.isEnableTracing());
        if (restart && context().hasModule(GRPC_SERVER_MODULE_ID)) {
            grpcServerModuleState().getServer().restart();
        }
    }
}
