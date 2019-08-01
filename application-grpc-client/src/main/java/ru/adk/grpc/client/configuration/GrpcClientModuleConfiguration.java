package ru.adk.grpc.client.configuration;

import io.grpc.ClientInterceptor;
import lombok.Getter;
import ru.adk.core.module.ModuleConfiguration;
import ru.adk.grpc.client.exception.GrpcClientException;
import ru.adk.grpc.client.interceptor.GrpcClientTracingInterceptor;
import ru.adk.grpc.client.model.GrpcCommunicationTargetConfiguration;
import static java.text.MessageFormat.format;
import static java.util.Collections.emptyMap;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static ru.adk.core.constants.NetworkConstants.LOCALHOST;
import static ru.adk.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.adk.core.extension.ExceptionExtensions.exceptionIfNull;
import static ru.adk.core.factory.CollectionsFactory.linkedListOf;
import static ru.adk.grpc.client.constants.GrpcClientExceptionMessages.GRPC_COMMUNICATION_TARGET_CONFIGURATION_NOT_FOUND;
import static ru.adk.grpc.client.constants.GrpcClientModuleConstants.DEFAULT_GRPC_PORT;
import static ru.adk.grpc.client.constants.GrpcClientModuleConstants.DEFAULT_TIMEOUT;
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

    @Getter
    class GrpcClientModuleDefaultConfiguration implements GrpcClientModuleConfiguration {
        @Getter(lazy = true)
        private final List<ClientInterceptor> interceptors = linkedListOf(new GrpcClientTracingInterceptor());
        private final long timeout = DEFAULT_TIMEOUT;
        private final Executor overridingExecutor = newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        private final String balancerHost = LOCALHOST;
        private final int balancerPort = DEFAULT_GRPC_PORT;
        private final Map<String, GrpcCommunicationTargetConfiguration> communicationTargets = emptyMap();
    }
}
