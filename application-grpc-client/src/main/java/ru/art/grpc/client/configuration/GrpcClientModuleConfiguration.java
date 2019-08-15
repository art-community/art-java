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

package ru.art.grpc.client.configuration;

import io.grpc.ClientInterceptor;
import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import ru.art.grpc.client.exception.GrpcClientException;
import ru.art.grpc.client.interceptor.GrpcClientTracingInterceptor;
import ru.art.grpc.client.model.GrpcCommunicationTargetConfiguration;
import static java.text.MessageFormat.format;
import static java.util.Collections.emptyMap;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static ru.art.core.constants.NetworkConstants.LOCALHOST;
import static ru.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.art.core.extension.ExceptionExtensions.exceptionIfNull;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.grpc.client.constants.GrpcClientExceptionMessages.GRPC_COMMUNICATION_TARGET_CONFIGURATION_NOT_FOUND;
import static ru.art.grpc.client.constants.GrpcClientModuleConstants.DEFAULT_GRPC_PORT;
import static ru.art.grpc.client.constants.GrpcClientModuleConstants.DEFAULT_TIMEOUT;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public interface GrpcClientModuleConfiguration extends ModuleConfiguration {
    List<ClientInterceptor> getInterceptors();

    long getTimeout();

    Executor getOverridingExecutor();

    String getBalancerHost();

    int getBalancerPort();

    Map<String, GrpcCommunicationTargetConfiguration> getCommunicationTargets();

    default GrpcCommunicationTargetConfiguration getCommunicationTargetConfiguration(String serviceId) {
        return exceptionIfNull(getCommunicationTargets().get(serviceId), new GrpcClientException(format(GRPC_COMMUNICATION_TARGET_CONFIGURATION_NOT_FOUND, serviceId))).toBuilder().build();
    }

    GrpcClientModuleDefaultConfiguration DEFAULT_CONFIGURATION = new GrpcClientModuleDefaultConfiguration();

	@Getter
	class GrpcClientModuleDefaultConfiguration implements GrpcClientModuleConfiguration {
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<ClientInterceptor> interceptors = linkedListOf(new GrpcClientTracingInterceptor());
        private final long timeout = DEFAULT_TIMEOUT;
        private final Executor overridingExecutor = newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        private final String balancerHost = LOCALHOST;
        private final int balancerPort = DEFAULT_GRPC_PORT;
        private final Map<String, GrpcCommunicationTargetConfiguration> communicationTargets = emptyMap();
    }
}
