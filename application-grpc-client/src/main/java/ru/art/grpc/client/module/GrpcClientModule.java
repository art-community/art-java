package ru.art.grpc.client.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.grpc.client.configuration.GrpcClientModuleConfiguration;
import ru.art.grpc.client.constants.GrpcClientModuleConstants;
import static ru.art.core.context.Context.context;
import static ru.art.grpc.client.configuration.GrpcClientModuleConfiguration.GrpcClientModuleDefaultConfiguration;
import static ru.art.grpc.client.constants.GrpcClientModuleConstants.GRPC_CLIENT_MODULE_ID;

public class GrpcClientModule implements Module<GrpcClientModuleConfiguration, ModuleState> {
    @Getter
    private final String id = GrpcClientModuleConstants.GRPC_CLIENT_MODULE_ID;
    @Getter
    private final GrpcClientModuleConfiguration defaultConfiguration = new GrpcClientModuleDefaultConfiguration();

    public static GrpcClientModuleConfiguration grpcClientModule() {
        return context().getModule(GRPC_CLIENT_MODULE_ID, new GrpcClientModule());
    }
}
