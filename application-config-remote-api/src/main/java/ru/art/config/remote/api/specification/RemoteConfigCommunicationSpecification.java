package ru.art.config.remote.api.specification;

import lombok.Getter;
import ru.art.grpc.client.communicator.GrpcCommunicator.GrpcAsynchronousCommunicator;
import ru.art.grpc.client.specification.GrpcCommunicationSpecification;
import ru.art.service.exception.UnknownServiceMethodException;
import static ru.art.config.remote.api.constants.RemoteConfigApiConstants.Methods.APPLY_CONFIGURATION_METHOD_ID;
import static ru.art.config.remote.api.constants.RemoteConfigApiConstants.REMOTE_CONFIG_SERVICE_ID;
import static ru.art.grpc.client.communicator.GrpcCommunicator.grpcCommunicator;

@Getter
public class RemoteConfigCommunicationSpecification implements GrpcCommunicationSpecification {
    private final String host;
    private final Integer port;
    private final String path;
    private final String serviceId = REMOTE_CONFIG_SERVICE_ID;

    public RemoteConfigCommunicationSpecification(String host, Integer port, String path) {
        this.host = host;
        this.port = port;
        this.path = path;
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