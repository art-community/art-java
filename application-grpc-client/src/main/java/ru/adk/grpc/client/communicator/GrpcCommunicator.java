package ru.adk.grpc.client.communicator;

import io.grpc.ClientInterceptor;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.grpc.client.handler.GrpcCommunicationCompletionHandler;
import ru.adk.grpc.client.handler.GrpcCommunicationExceptionHandler;
import ru.adk.grpc.client.model.GrpcCommunicationTargetConfiguration;
import ru.adk.service.model.ServiceResponse;
import java.util.concurrent.Executor;

public interface GrpcCommunicator {
    static GrpcCommunicator grpcCommunicator(GrpcCommunicationTargetConfiguration targetConfiguration) {
        return new GrpcCommunicatorImplementation(targetConfiguration);
    }

    static GrpcCommunicator grpcCommunicator(String url) {
        return new GrpcCommunicatorImplementation(url);
    }

    static GrpcCommunicator grpcCommunicator(String url, String path) {
        return new GrpcCommunicatorImplementation(url, path);
    }

    static GrpcCommunicator grpcCommunicator(String host, int port, String path) {
        return new GrpcCommunicatorImplementation(host, port, path);
    }

    GrpcCommunicator serviceId(String id);

    GrpcCommunicator methodId(String id);

    GrpcCommunicator requestMapper(ValueFromModelMapper mapper);

    GrpcCommunicator responseMapper(ValueToModelMapper mapper);

    GrpcCommunicator deadlineTimeout(long timeout);

    GrpcCommunicator addInterceptor(ClientInterceptor interceptor);

    GrpcCommunicator executor(Executor executor);

    GrpcCommunicator secured();

    GrpcAsynchronousCommunicator asynchronous();

    <ResponseType> ServiceResponse<ResponseType> execute();

    <RequestType, ResponseType> ServiceResponse<ResponseType> execute(RequestType request);

    interface GrpcAsynchronousCommunicator {
        <RequestType, ResponseType> GrpcAsynchronousCommunicator completionHandler(GrpcCommunicationCompletionHandler<RequestType, ResponseType> completionHandler);

        <RequestType> GrpcAsynchronousCommunicator exceptionHandler(GrpcCommunicationExceptionHandler<RequestType> errorHandler);

        void executeAsynchronous();

        <RequestType> void executeAsynchronous(RequestType request);
    }
}
