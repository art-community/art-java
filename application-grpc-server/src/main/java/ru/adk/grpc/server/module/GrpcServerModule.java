package ru.adk.grpc.server.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.grpc.server.configuration.GrpcServerModuleConfiguration;
import ru.adk.grpc.server.state.GrpcServerModuleState;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.context.Context.context;
import static ru.adk.grpc.server.configuration.GrpcServerModuleConfiguration.GrpcServerModuleDefaultConfiguration;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVER_MODULE_ID;

public class GrpcServerModule implements Module<GrpcServerModuleConfiguration, GrpcServerModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static GrpcServerModuleConfiguration grpcServerModule = context().getModule(GRPC_SERVER_MODULE_ID, new GrpcServerModule());
    @Getter(lazy = true, value = PRIVATE)
    private final static GrpcServerModuleState grpcServerModuleState = context().getModuleState(GRPC_SERVER_MODULE_ID, new GrpcServerModule());
    @Getter
    private final GrpcServerModuleState state = new GrpcServerModuleState();
    @Getter
    private final String id = GRPC_SERVER_MODULE_ID;
    @Getter
    private final GrpcServerModuleConfiguration defaultConfiguration = new GrpcServerModuleDefaultConfiguration();

    public static GrpcServerModuleConfiguration grpcServerModule() {
        return getGrpcServerModule();
    }

    public static GrpcServerModuleState grpcServerModuleState() {
        return getGrpcServerModuleState();
    }
}
