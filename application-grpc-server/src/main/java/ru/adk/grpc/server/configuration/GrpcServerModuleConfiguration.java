package ru.adk.grpc.server.configuration;

import io.grpc.ServerInterceptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.adk.core.module.ModuleConfiguration;
import ru.adk.grpc.server.interceptor.GrpcServerLoggingInterceptor;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static ru.adk.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.adk.core.factory.CollectionsFactory.linkedListOf;
import static ru.adk.core.network.selector.PortSelector.findAvailableTcpPort;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;

public interface GrpcServerModuleConfiguration extends ModuleConfiguration {
    boolean isExecuteServiceInTransportThread();

    Executor getOverridingExecutor();

    GrpcServerSecurityConfiguration getSecurityConfiguration();

    String getPath();

    List<ServerInterceptor> getInterceptors();

    int getMaxInboundMessageSize();

    int getHandshakeTimeout();

    int getPort();

    @Getter
    @AllArgsConstructor
    class GrpcServerSecurityConfiguration {
        private File certificateFile;
        private File privateKeyFile;
    }

    @Getter
    class GrpcServerModuleDefaultConfiguration implements GrpcServerModuleConfiguration {
        private final String path = DEFAULT_MODULE_PATH;
        private final boolean executeServiceInTransportThread = false;
        private final Executor overridingExecutor = newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        private final GrpcServerSecurityConfiguration securityConfiguration = null;
        @Getter(lazy = true)
        private final List<ServerInterceptor> interceptors = linkedListOf(new GrpcServerLoggingInterceptor());
        private final int maxInboundMessageSize = DEFAULT_MAX_INBOUND_MESSAGE_SIZE;
        private final int handshakeTimeout = DEFAULT_HANDSHAKE_TIMEOUT;
        private final int port = findAvailableTcpPort();
    }
}
