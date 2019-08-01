package ru.adk.grpc.server.specification;

import ru.adk.grpc.server.constants.GrpcServerModuleConstants;
import ru.adk.grpc.server.model.GrpcService;
import ru.adk.service.Specification;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import java.util.List;

public interface GrpcServiceSpecification extends Specification {
    GrpcService getGrpcService();

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(GrpcServerModuleConstants.GRPC_SERVICE_TYPE);
    }
}
