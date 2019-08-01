package ru.art.state.api.communication.grpc;

import lombok.Getter;
import lombok.experimental.Accessors;
import ru.art.grpc.client.communicator.GrpcCommunicator;
import ru.art.grpc.client.specification.GrpcCommunicationSpecification;
import ru.art.service.exception.UnknownServiceMethodException;
import static ru.art.core.caster.Caster.cast;
import static ru.art.grpc.client.communicator.GrpcCommunicator.grpcCommunicator;
import static ru.art.state.api.constants.StateApiConstants.LockServiceConstants.LOCK_SERVICE_ID;
import static ru.art.state.api.constants.StateApiConstants.LockServiceConstants.Methods.LOCK;
import static ru.art.state.api.constants.StateApiConstants.LockServiceConstants.Methods.UNLOCK;
import static ru.art.state.api.mapping.LockRequestMapper.fromLockRequest;

@Getter
public class LockServiceProxySpecification implements GrpcCommunicationSpecification {
    private final String path;
    private final String host;
    private final Integer port;
    private final String serviceId;

    public LockServiceProxySpecification(String path, String host, Integer port) {
        this.path = path;
        this.host = host;
        this.port = port;
        serviceId = serviceId(host, port, path);
    }

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator lock = grpcCommunicator(host, port, path)
            .serviceId(LOCK_SERVICE_ID)
            .methodId(LOCK)
            .requestMapper(fromLockRequest);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator unlock = grpcCommunicator(host, port, path)
            .serviceId(LOCK_SERVICE_ID)
            .methodId(UNLOCK)
            .requestMapper(fromLockRequest);

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case LOCK:
                lock().execute(cast(request));
                return null;
            case UNLOCK:
                unlock().execute(cast(request));
                return null;
        }
        throw new UnknownServiceMethodException(getServiceId(), methodId);
    }
}
