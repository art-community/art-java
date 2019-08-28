/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.example.api.communication.grpc;

import lombok.*;
import lombok.experimental.*;
import ru.art.example.api.handler.*;
import ru.art.grpc.client.communicator.*;
import ru.art.grpc.client.communicator.GrpcCommunicator.*;
import ru.art.grpc.client.specification.*;
import ru.art.service.exception.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.example.api.constants.ExampleApiConstants.*;
import static ru.art.example.api.constants.ExampleApiConstants.Methods.*;
import static ru.art.example.api.mapping.ExampleRequestResponseMapper.ExampleRequestMapper.*;
import static ru.art.example.api.mapping.ExampleRequestResponseMapper.ExampleResponseMapper.*;
import static ru.art.example.api.mapping.ExampleStateModelMapper.*;
import static ru.art.grpc.client.communicator.GrpcCommunicator.*;

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
