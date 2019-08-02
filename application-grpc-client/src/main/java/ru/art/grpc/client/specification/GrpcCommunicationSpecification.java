package ru.art.grpc.client.specification;

import ru.art.grpc.client.model.GrpcCommunicationTargetConfiguration;
import ru.art.service.Specification;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.grpc.client.constants.GrpcClientModuleConstants.GRPC_COMMUNICATION_SERVICE_TYPE;
import static ru.art.grpc.client.module.GrpcClientModule.grpcClientModule;
import java.util.List;

public interface GrpcCommunicationSpecification extends Specification {
    default GrpcCommunicationTargetConfiguration communicationTarget(String serviceId) {
        return grpcClientModule().getCommunicationTargetConfiguration(serviceId);
    }

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(GRPC_COMMUNICATION_SERVICE_TYPE);
    }
}