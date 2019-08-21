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

package ru.art.grpc.server.configuration;

import io.grpc.ServerInterceptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import ru.art.entity.Entity;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.grpc.server.interceptor.GrpcServerLoggingInterceptor;
import ru.art.logging.LoggingValueInterceptor;
import static ru.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.core.network.selector.PortSelector.findAvailableTcpPort;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public interface GrpcServerModuleConfiguration extends ModuleConfiguration {
    boolean isExecuteServiceInTransportThread();

    Executor getOverridingExecutor();

    GrpcServerSecurityConfiguration getSecurityConfiguration();

    String getPath();

    List<ServerInterceptor> getInterceptors();

    int getMaxInboundMessageSize();

    int getHandshakeTimeout();

    int getPort();

    boolean isEnableRawDataTracing();

    boolean isEnableValueTracing();

    List<ValueInterceptor<Entity, Entity>> getRequestValueInterceptors();

    List<ValueInterceptor<Entity, Entity>> getResponseValueInterceptors();

    @Getter
    @AllArgsConstructor
    class GrpcServerSecurityConfiguration {
        private File certificateFile;
        private File privateKeyFile;
    }

    GrpcServerModuleDefaultConfiguration DEFAULT_CONFIGURATION = new GrpcServerModuleDefaultConfiguration();

    @Getter
    class GrpcServerModuleDefaultConfiguration implements GrpcServerModuleConfiguration {
        private final String path = DEFAULT_MODULE_PATH;
        private final boolean executeServiceInTransportThread = false;
        private final Executor overridingExecutor = new ForkJoinPool(DEFAULT_THREAD_POOL_SIZE);
        private final GrpcServerSecurityConfiguration securityConfiguration = null;
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<ServerInterceptor> interceptors = initializeInterceptors();
        private final int maxInboundMessageSize = DEFAULT_MAX_INBOUND_MESSAGE_SIZE;
        private final int handshakeTimeout = DEFAULT_HANDSHAKE_TIMEOUT;
        private final int port = findAvailableTcpPort();
        private final boolean enableRawDataTracing = true;
        private final boolean enableValueTracing = true;
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<ValueInterceptor<Entity, Entity>> requestValueInterceptors = initializeValueInterceptors();
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<ValueInterceptor<Entity, Entity>> responseValueInterceptors = initializeValueInterceptors();

        private List<ValueInterceptor<Entity, Entity>> initializeValueInterceptors() {
            return isEnableValueTracing() ? linkedListOf(new LoggingValueInterceptor<>()) : linkedListOf();
        }
        private List<ServerInterceptor> initializeInterceptors() {
            return isEnableRawDataTracing() ? linkedListOf(new GrpcServerLoggingInterceptor()) : linkedListOf();
        }
    }
}
