package ru.adk.config.remote.api.specification;

import lombok.Getter;
import ru.adk.grpc.client.communicator.GrpcCommunicator.GrpcAsynchronousCommunicator;
import ru.adk.grpc.client.specification.GrpcCommunicationSpecification;
import ru.adk.service.exception.UnknownServiceMethodException;
import static ru.adk.config.remote.api.constants.RemoteConfigApiConstants.Methods.APPLY_CONFIGURATION_METHOD_ID;
import static ru.adk.config.remote.api.constants.RemoteConfigApiConstants.REMOTE_CONFIG_SERVICE_ID;
import static ru.adk.grpc.client.communicator.GrpcCommunicator.grpcCommunicator;

@Getter
public class RemoteConfigCommunicationSpecification implements GrpcCommunicationSpecification {
    private final String host;
    private final Integer port;
    private final String path;
    private final String serviceId;

    public RemoteConfigCommunicationSpecification(String host, Integer port, String path) {
        this.host = host;
        this.port = port;
        this.path = path;
        serviceId = serviceId(host, port, path);
    }

    @Getter(lazy = true)
    private final GrpcAsynchronousCommunicator applyConfigurationProxy = grpcCommunicator(host, port, path)
            .serviceId(REMOTE_CONFIG_SERVICE_ID)
            .methodId(APPLY_CONFIGURATION_METHOD_ID)
            .asynchronous();

    public void applyConfiguration() {
        executeMethod(APPLY_CONFIGURATION_METHOD_ID, null);
    }

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        if (APPLY_CONFIGURATION_METHOD_ID.equals(methodId)) {
            getApplyConfigurationProxy().executeAsynchronous();
            return null;
        }
        throw new UnknownServiceMethodException(getServiceId(), methodId);
    }
}