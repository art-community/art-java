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

package ru.art.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.Logger;
import ru.art.grpc.server.configuration.GrpcServerModuleConfiguration.GrpcServerSecurityConfiguration;
import ru.art.grpc.server.exception.GrpcServerException;
import ru.art.grpc.server.servlet.GrpcServletContainer;
import ru.art.grpc.server.specification.GrpcServiceSpecification;
import static io.grpc.ServerBuilder.forPort;
import static java.lang.System.currentTimeMillis;
import static java.text.MessageFormat.format;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.SECONDS;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.extension.ThreadExtensions.thread;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.grpc.server.constants.GrpcServerExceptionMessages.*;
import static ru.art.grpc.server.constants.GrpcServerLoggingMessages.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVER_THREAD;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.art.grpc.server.module.GrpcServerModule.grpcServerModule;
import static ru.art.grpc.server.module.GrpcServerModule.grpcServerModuleState;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.service.ServiceModule.serviceModule;
import java.util.Map;
import java.util.concurrent.Executor;

@AllArgsConstructor
public class GrpcServer {
    private static final Logger logger = loggingModule().getLogger(GrpcServer.class);
    private final Server server;

    public static GrpcServer grpcServer() {
        long millis = currentTimeMillis();
        ServerBuilder<?> serverBuilder = forPort(grpcServerModule().getPort());
        serverBuilder.maxInboundMessageSize(grpcServerModule().getMaxInboundMessageSize());
        if (grpcServerModule().isExecuteServiceInTransportThread()) {
            serverBuilder.directExecutor();
        }
        Executor overridingExecutor;
        if (nonNull(overridingExecutor = grpcServerModule().getOverridingExecutor())) {
            serverBuilder.executor(overridingExecutor);
        }
        GrpcServerSecurityConfiguration serverSecurityConfiguration;
        if (nonNull(serverSecurityConfiguration = grpcServerModule().getSecurityConfiguration())) {
            serverBuilder.useTransportSecurity(serverSecurityConfiguration.getCertificateFile(), serverSecurityConfiguration.getPrivateKeyFile());
        }
        grpcServerModule().getInterceptors().forEach(serverBuilder::intercept);
        Map<String, GrpcServiceSpecification> protobufServices = mapOf();
        serviceModule().getServiceRegistry().getServices().values().stream()
                .filter(serviceSpecification -> serviceSpecification.getServiceTypes().contains(GRPC_SERVICE_TYPE))
                .map(serviceSpecification -> (GrpcServiceSpecification) serviceSpecification)
                .peek(GrpcServer::logService)
                .forEach(service -> protobufServices.put(service.getServiceId(), service));
        serverBuilder.addService(new GrpcServletContainer(grpcServerModule().getPath(), protobufServices).getServlet());
        serverBuilder.handshakeTimeout(grpcServerModule().getHandshakeTimeout(), SECONDS);
        GrpcServer protobufServer = new GrpcServer(serverBuilder.build());
        try {
            protobufServer.server.start();
            logger.info(format(GRPC_STARTED_MESSAGE, currentTimeMillis() - millis));
            grpcServerModuleState().setServer(protobufServer);
        } catch (Throwable e) {
            throw new GrpcServerException(GRPC_SERVER_INITIALIZATION_FAILED, e);
        }
        return protobufServer;
    }

    public static GrpcServer grpcServerInSeparatedThread() {
        GrpcServer grpcServer = grpcServer();
        thread(GRPC_SERVER_THREAD, grpcServer ::await);
        return grpcServer;
    }

    private static String buildServiceLoadedMessage(GrpcServiceSpecification service) {
        return format(GRPC_LOADED_SERVICE_MESSAGE, contextConfiguration().getIpAddress(), grpcServerModule().getPort(), grpcServerModule().getPath(), service.getServiceId(), service.getGrpcService().getMethods().keySet().toString());
    }

    private static void logService(GrpcServiceSpecification specification) {
        logger.info(buildServiceLoadedMessage(specification));
    }

    public void await() {
        try {
            server.awaitTermination();
        } catch (Throwable e) {
            throw new GrpcServerException(GRPC_SERVER_AWAITING_FAILED, e);
        }
    }

    public void restart() {
        long millis = currentTimeMillis();
        try {
            server.shutdownNow();
            grpcServer();
            logger.info(format(GRPC_RESTARTED_MESSAGE, currentTimeMillis() - millis));
        } catch (Throwable e) {
            logger.error(GRPC_SERVER_RESTART_FAILED);
        }
    }
}
