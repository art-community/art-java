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

package io.art.grpc.client.configuration;

import io.grpc.*;
import lombok.*;
import io.art.core.module.*;
import io.art.entity.*;
import io.art.entity.interceptor.*;
import io.art.grpc.client.exception.*;
import io.art.grpc.client.interceptor.*;
import io.art.grpc.client.model.*;
import io.art.logging.*;
import static io.grpc.internal.GrpcUtil.*;
import static java.lang.Long.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.concurrent.ForkJoinPool.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.constants.ThreadConstants.*;
import static io.art.core.extension.ExceptionExtensions.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.grpc.client.constants.GrpcClientExceptionMessages.*;
import static io.art.grpc.client.constants.GrpcClientModuleConstants.*;
import java.util.*;
import java.util.concurrent.*;

public interface GrpcClientModuleConfiguration extends ModuleConfiguration {
    List<ClientInterceptor> getInterceptors();

    long getTimeout();

    long getKeepAliveTimeNanos();

    long getKeepAliveTimeOutNanos();

    boolean isKeepAliveWithoutCalls();

    boolean isWaitForReady();

    Executor getOverridingExecutor();

    String getBalancerHost();

    int getBalancerPort();

    Executor getAsynchronousFuturesExecutor();

    Map<String, GrpcCommunicationTargetConfiguration> getCommunicationTargets();

    boolean isEnableRawDataTracing();

    boolean isEnableValueTracing();

    List<ValueInterceptor<Entity, Entity>> getRequestValueInterceptors();

    List<ValueInterceptor<Entity, Entity>> getResponseValueInterceptors();

    default GrpcCommunicationTargetConfiguration getCommunicationTargetConfiguration(String serviceId) {
        return exceptionIfNull(getCommunicationTargets().get(serviceId),
                new GrpcClientException(format(GRPC_COMMUNICATION_TARGET_CONFIGURATION_NOT_FOUND, serviceId))).toBuilder().build();
    }

    GrpcClientModuleDefaultConfiguration DEFAULT_CONFIGURATION = new GrpcClientModuleDefaultConfiguration();

    @Getter
    class GrpcClientModuleDefaultConfiguration implements GrpcClientModuleConfiguration {
        private final boolean enableRawDataTracing = false;
        private final boolean enableValueTracing = false;
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<ClientInterceptor> interceptors = initializeInterceptors();
        private final Executor asynchronousFuturesExecutor = commonPool();
        private final long timeout = DEFAULT_GRPC_DEADLINE;
        private final Executor overridingExecutor = new ForkJoinPool(DEFAULT_THREAD_POOL_SIZE);
        private final String balancerHost = LOCALHOST;
        private final int balancerPort = DEFAULT_GRPC_PORT;
        private final Map<String, GrpcCommunicationTargetConfiguration> communicationTargets = emptyMap();
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<ValueInterceptor<Entity, Entity>> requestValueInterceptors = initializeValueInterceptors();
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<ValueInterceptor<Entity, Entity>> responseValueInterceptors = initializeValueInterceptors();
        private long keepAliveTimeNanos = MAX_VALUE;
        private long keepAliveTimeOutNanos = DEFAULT_KEEPALIVE_TIMEOUT_NANOS;
        private boolean keepAliveWithoutCalls = false;
        private boolean waitForReady = false;

        private List<ClientInterceptor> initializeInterceptors() {
            return linkedListOf(new GrpcClientTracingIdentifiersInterceptor(), new GrpcClientLoggingInterceptor());
        }

        private List<ValueInterceptor<Entity, Entity>> initializeValueInterceptors() {
            return linkedListOf(new LoggingValueInterceptor<>(this::isEnableValueTracing));
        }
    }
}
