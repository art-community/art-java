package ru.art.remote.scheduler.api.communicator.grpc;

import lombok.Getter;
import ru.art.grpc.client.specification.GrpcCommunicationSpecification;

@Getter
public class RemoteSchedulerServiceGrpcCommunicationSpecification implements GrpcCommunicationSpecification {
    @Override
    public String getServiceId() {
        return null;
    }

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        return null;
    }
}