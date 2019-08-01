package ru.adk.network.manager.client;

import ru.adk.state.api.model.ModuleConnectionRequest;
import ru.adk.state.api.model.ModuleEndpoint;
import static java.lang.System.getProperty;
import static ru.adk.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.adk.core.constants.ContextConstants.LOCAL_PROFILE;
import static ru.adk.core.constants.SystemProperties.PROFILE_PROPERTY;
import static ru.adk.grpc.server.module.GrpcServerModule.grpcServerModule;
import static ru.adk.network.manager.module.NetworkManagerModule.networkManagerModule;
import static ru.adk.service.ServiceController.executeServiceMethod;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.Methods.*;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.NETWORK_SERVICE_ID;

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