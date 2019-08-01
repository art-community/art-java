package ru.adk.grpc.client.specification;

import ru.adk.grpc.client.model.GrpcCommunicationTargetConfiguration;
import ru.adk.service.Specification;
import static ru.adk.core.constants.StringConstants.COLON;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.grpc.client.constants.GrpcClientModuleConstants.GRPC_COMMUNICATION_SERVICE_TYPE;
import static ru.adk.grpc.client.module.GrpcClientModule.grpcClientModule;
import java.util.List;

public interface GrpcCommunicationSpecification extends Specification {
    default GrpcCommunicationTargetConfiguration communicationTarget(String serviceId) {
        return grpcClientModule().getCommunicationTargetConfiguration(serviceId);
    }

    default String serviceId(String host, int port, String path) {
        return host + COLON + port + path;
    }

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(GRPC_COMMUNICATION_SERVICE_TYPE);
    }
}