package ru.art.example.api.communication.grpc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.art.example.api.handler.ExampleServiceHandlers;
import ru.art.grpc.client.communicator.GrpcCommunicator;
import ru.art.grpc.client.communicator.GrpcCommunicator.GrpcAsynchronousCommunicator;
import ru.art.grpc.client.specification.GrpcCommunicationSpecification;
import ru.art.service.exception.UnknownServiceMethodException;
import static ru.art.core.caster.Caster.cast;
import static ru.art.example.api.constants.ExampleApiConstants.EXAMPLE_GRPC_COMMUNICATION_SERVICE_ID;
import static ru.art.example.api.constants.ExampleApiConstants.EXAMPLE_SERVICE_ID;
import static ru.art.example.api.constants.ExampleApiConstants.Methods.*;
import static ru.art.example.api.mapping.ExampleRequestResponseMapper.ExampleRequestMapper.fromExampleRequest;
import static ru.art.example.api.mapping.ExampleRequestResponseMapper.ExampleResponseMapper.toExampleResponse;
import static ru.art.example.api.mapping.ExampleStateModelMapper.toExampleStateModel;
import static ru.art.grpc.client.communicator.GrpcCommunicator.grpcCommunicator;

/**
 * Protobuf Proxy specification is made for preparing protobuf clients for calling external module
 * We set all needed mappers here, serviceId and methodId of external module http service
 */

@Getter
@RequiredArgsConstructor
public class ExampleServiceGrpcCommunicationSpecification implements GrpcCommunicationSpecification {
    private final String serviceId = EXAMPLE_GRPC_COMMUNICATION_SERVICE_ID;

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator requestResponseHandlingExample = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(REQUEST_RESPONSE_HANDLING_EXAMPLE)
            .requestMapper(fromExampleRequest)
            .responseMapper(toExampleResponse);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcAsynchronousCommunicator requestResponseHandlingExampleAsync = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(REQUEST_RESPONSE_HANDLING_EXAMPLE)
            .requestMapper(fromExampleRequest)
            .responseMapper(toExampleResponse)
            .asynchronous()
            .completionHandler(ExampleServiceHandlers::handleRequestResponseHandlingExampleCompletion)
            .exceptionHandler(ExampleServiceHandlers::handleRequestResponseHandlingExampleError);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator usingConfigurationValuesExample = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(USING_CONFIGURATION_VALUES_EXAMPLE);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator soapClientExample = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(SOAP_CLIENT_EXAMPLE);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator httpClientExample = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(HTTP_CLIENT_EXAMPLE);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator protobufClientExample = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(GRPC_CLIENT_EXAMPLE);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator sqlExample = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(SQL_EXAMPLE);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator rocksDbExample = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(ROCKS_DB_EXAMPLE);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator loggingExample = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(LOGGING_EXAMPLE);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator jsonReadWriteExample = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(JSON_READ_WRITE_EXAMPLE);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator protobufReadWriteExample = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(PROTOBUF_READ_WRITE_EXAMPLE);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator asyncTaskExecutingExample = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(ASYNC_TASK_EXECUTING_EXAMPLE);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator getExampleModuleState = grpcCommunicator(communicationTarget(EXAMPLE_SERVICE_ID))
            .methodId(GET_EXAMPLE_MODULE_STATE)
            .responseMapper(toExampleStateModel);

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case REQUEST_RESPONSE_HANDLING_EXAMPLE:
                return cast(requestResponseHandlingExample().execute(request));
            case REQUEST_RESPONSE_HANDLING_EXAMPLE_ASYNC:
                requestResponseHandlingExampleAsync().executeAsynchronous(request);
                return null;
            case USING_CONFIGURATION_VALUES_EXAMPLE:
                usingConfigurationValuesExample().execute();
                return null;
            case SOAP_CLIENT_EXAMPLE:
                soapClientExample().execute();
                return null;
            case HTTP_CLIENT_EXAMPLE:
                httpClientExample().execute();
                return null;
            case GRPC_CLIENT_EXAMPLE:
                protobufClientExample().execute();
                return null;
            case SQL_EXAMPLE:
                sqlExample().execute();
                return null;
            case ROCKS_DB_EXAMPLE:
                rocksDbExample().execute();
                return null;
            case LOGGING_EXAMPLE:
                loggingExample().execute();
                return null;
            case JSON_READ_WRITE_EXAMPLE:
                jsonReadWriteExample().execute();
                return null;
            case PROTOBUF_READ_WRITE_EXAMPLE:
                protobufReadWriteExample().execute();
                return null;
            case ASYNC_TASK_EXECUTING_EXAMPLE:
                asyncTaskExecutingExample().execute();
            case GET_EXAMPLE_MODULE_STATE:
                return cast(getExampleModuleState().execute());
        }
        throw new UnknownServiceMethodException(getServiceId(), methodId);
    }
}
