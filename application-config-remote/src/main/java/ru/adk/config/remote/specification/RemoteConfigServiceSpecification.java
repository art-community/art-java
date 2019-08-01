package ru.adk.config.remote.specification;

import lombok.Getter;
import lombok.ToString;
import ru.adk.grpc.server.model.GrpcService;
import ru.adk.grpc.server.specification.GrpcServiceSpecification;
import ru.adk.service.exception.UnknownServiceMethodException;
import static ru.adk.config.remote.api.constants.RemoteConfigApiConstants.Methods.APPLY_CONFIGURATION_METHOD_ID;
import static ru.adk.config.remote.api.constants.RemoteConfigApiConstants.REMOTE_CONFIG_SERVICE_ID;
import static ru.adk.config.remote.service.RemoteConfigService.applyConfiguration;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.adk.grpc.server.model.GrpcService.GrpcMethod.grpcMethod;
import static ru.adk.grpc.server.model.GrpcService.grpcService;
import java.util.List;

@ToString
@Getter
public class RemoteConfigServiceSpecification implements GrpcServiceSpecification {
    private final String serviceId = REMOTE_CONFIG_SERVICE_ID;
    private final GrpcService grpcService = grpcService().method(APPLY_CONFIGURATION_METHOD_ID, grpcMethod()).serve();
    private final List<String> serviceTypes = fixedArrayOf(GRPC_SERVICE_TYPE);

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        if (APPLY_CONFIGURATION_METHOD_ID.equals(methodId)) {
            applyConfiguration();
            return null;
        }
        throw new UnknownServiceMethodException(serviceId, methodId);
    }
}
