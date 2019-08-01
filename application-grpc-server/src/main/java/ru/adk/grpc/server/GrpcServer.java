package ru.adk.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.Logger;
import ru.adk.grpc.server.configuration.GrpcServerModuleConfiguration.GrpcServerSecurityConfiguration;
import ru.adk.grpc.server.exception.GrpcServerException;
import ru.adk.grpc.server.servlet.GrpcServletContainer;
import ru.adk.grpc.server.specification.GrpcServiceSpecification;
import static io.grpc.ServerBuilder.forPort;
import static java.lang.System.currentTimeMillis;
import static java.text.MessageFormat.format;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.SECONDS;
import static ru.adk.core.context.Context.contextConfiguration;
import static ru.adk.core.extension.ThreadExtensions.thread;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.grpc.server.constants.GrpcServerExceptionMessages.*;
import static ru.adk.grpc.server.constants.GrpcServerLoggingMessages.*;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVER_THREAD;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.adk.grpc.server.module.GrpcServerModule.grpcServerModule;
import static ru.adk.grpc.server.module.GrpcServerModule.grpcServerModuleState;
import static ru.adk.logging.LoggingModule.loggingModule;
import static ru.adk.service.ServiceModule.serviceModule;
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new GrpcServerException(GRPC_SERVER_AWAITING_FAILED, e);
        }
    }

    public void restart() {
        long millis = currentTimeMillis();
        try {
            server.shutdownNow();
            grpcServer();
            logger.info(format(GRPC_RESTARTED_MESSAGE, currentTimeMillis() - millis));
        } catch (Exception e) {
            logger.error(GRPC_SERVER_RESTART_FAILED);
        }
    }
}
