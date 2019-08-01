package ru.art.grpc.server.specification;

import ru.art.grpc.server.constants.GrpcServerModuleConstants;
import ru.art.grpc.server.model.GrpcService;
import ru.art.service.Specification;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import java.util.List;

public interface GrpcServiceSpecification extends Specification {
    GrpcService getGrpcService();

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(GrpcServerModuleConstants.GRPC_SERVICE_TYPE);
    }
}
