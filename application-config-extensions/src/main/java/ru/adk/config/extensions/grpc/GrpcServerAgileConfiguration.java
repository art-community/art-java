package ru.adk.config.extensions.grpc;

import lombok.Getter;
import ru.adk.grpc.server.configuration.GrpcServerModuleConfiguration.GrpcServerModuleDefaultConfiguration;
import static java.util.Objects.isNull;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static ru.adk.config.extensions.ConfigExtensions.configInt;
import static ru.adk.config.extensions.ConfigExtensions.configString;
import static ru.adk.config.extensions.common.CommonConfigKeys.*;
import static ru.adk.config.extensions.grpc.GrpcConfigKeys.GRPC_SERVER_CONFIG_SECTION_ID;
import static ru.adk.config.extensions.grpc.GrpcConfigKeys.HANDSHAKE_TIMEOUT;
import static ru.adk.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.adk.core.context.Context.context;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVER_MODULE_ID;
import static ru.adk.grpc.server.module.GrpcServerModule.grpcServerModuleState;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Getter
public class GrpcServerAgileConfiguration extends GrpcServerModuleDefaultConfiguration {
    private int port;
    private String path;
    private int handshakeTimeout;
    private Executor overridingExecutor;

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
        if (restart && context().hasModule(GRPC_SERVER_MODULE_ID)) {
            grpcServerModuleState().getServer().restart();
        }
    }
}
