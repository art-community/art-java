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

package ru.art.grpc.client.communicator;

import ru.art.entity.Value;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.grpc.servlet.GrpcResponse;
import ru.art.service.exception.ServiceExecutionException;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceResponse;
import static java.util.Objects.nonNull;
import static ru.art.core.caster.Caster.cast;
import static ru.art.grpc.client.constants.GrpcClientModuleConstants.RESPONSE_OK;
import static ru.art.protobuf.descriptor.ProtobufEntityReader.readProtobuf;

public interface GrpcServiceResponseExtractor {
    static <ResponseType> ResponseType extractServiceResponse(GrpcCommunicationConfiguration configuration, GrpcResponse response) {
        String errorCode = response.getErrorCode();
        String errorMessage = response.getErrorMessage();
        Value responseDataValue = readProtobuf(response.getResponseData());
        ValueToModelMapper<?, ? extends Value> responseMapper = configuration.getResponseMapper();
        ServiceResponse.ServiceResponseBuilder<?> serviceResponseBuilder = ServiceResponse.builder().command(new ServiceMethodCommand(configuration.getServiceId(), configuration.getMethodId()));
        if (!RESPONSE_OK.equals(errorCode)) {
            serviceResponseBuilder.serviceException(new ServiceExecutionException(errorCode, errorMessage));
        }
        if (nonNull(responseDataValue) && nonNull(responseMapper)) {
            return cast(serviceResponseBuilder.responseData(cast(responseMapper.map(cast(responseDataValue)))).build());
        }
        return cast(serviceResponseBuilder.build());
    }
}
