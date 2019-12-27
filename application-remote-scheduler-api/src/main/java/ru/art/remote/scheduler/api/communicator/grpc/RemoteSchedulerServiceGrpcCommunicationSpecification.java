package ru.art.remote.scheduler.api.communicator.grpc;

import lombok.Getter;
import ru.art.grpc.client.specification.GrpcCommunicationSpecification;

import static ru.art.remote.scheduler.api.constants.RemoteSchedulerApiConstants.REMOTE_SCHEDULER_SERVICE_ID;

@Getter
public class RemoteSchedulerServiceGrpcCommunicationSpecification implements GrpcCommunicationSpecification {
    private final String serviceId = REMOTE_SCHEDULER_SERVICE_ID;
    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        return null;
    }
}