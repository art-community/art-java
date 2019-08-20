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

package ru.art.service.mapping;

import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.service.exception.ServiceExecutionException;
import ru.art.service.exception.ServiceMappingException;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceResponse;
import ru.art.service.model.ServiceResponse.ServiceResponseBuilder;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.mapper.ValueMapper.mapper;
import static ru.art.service.constants.ServiceExceptionsMessages.*;

@SuppressWarnings("Duplicates")
public interface ServiceResponseMapping {
    String SERVICE_METHOD_COMMAND = "serviceMethodCommand";
    String SERVICE_EXECUTION_EXCEPTION = "serviceExecutionException";
    String SERVICE_ID = "serviceId";
    String METHOD_ID = "methodId";
    String RESPONSE_DATA = "responseData";
    String ERROR_CODE = "errorCode";
    String ERROR_MESSAGE = "errorMessage";

    static <V> ValueToModelMapper.EntityToModelMapper<ServiceResponse<V>> toServiceResponse(final ValueToModelMapper<V, Value> responseDataMapper) {
        return value -> {
            ServiceResponseBuilder<V> serviceResponseBuilder = ServiceResponse.builder();
            Entity commandEntity = value.getEntity(SERVICE_METHOD_COMMAND);
            ServiceMethodCommand command = null;
            if (nonNull(commandEntity)) {
                String serviceId = commandEntity.getString(SERVICE_ID);
                String methodId = commandEntity.getString(METHOD_ID);
                serviceResponseBuilder.command(command = new ServiceMethodCommand(serviceId, methodId));
            }
            V responseData = isNull(responseDataMapper) ? null : value.getValue(RESPONSE_DATA, responseDataMapper);
            if (nonNull(responseData)) {
                serviceResponseBuilder.responseData(responseData);
            }
            Entity serviceExceptionEntity = value.getEntity(SERVICE_EXECUTION_EXCEPTION);
            if (isNull(serviceExceptionEntity)) {
                return serviceResponseBuilder.build();
            }
            String errorCode = serviceExceptionEntity.getString(ERROR_CODE);
            if (isEmpty(errorCode)) throw new ServiceMappingException(ERROR_CODE_IS_EMPTY);
            ServiceExecutionException serviceException = isNull(command)
                    ? new ServiceExecutionException(errorCode, serviceExceptionEntity.getString(ERROR_MESSAGE))
                    : new ServiceExecutionException(command, errorCode, serviceExceptionEntity.getString(ERROR_MESSAGE));
            return serviceResponseBuilder.serviceException(serviceException).build();
        };
    }

    static <V> ValueFromModelMapper.EntityFromModelMapper<ServiceResponse<V>> fromServiceResponse(final ValueFromModelMapper<V, ? extends Value> responseDataMapper) {
        return response -> {
            ServiceMethodCommand command;
            Entity.EntityBuilder entityBuilder = isNull(command = response.getCommand())
                    ? entityBuilder()
                    : entityBuilder()
                    .entityField(SERVICE_METHOD_COMMAND, entityBuilder()
                            .stringField(SERVICE_ID, command.getServiceId())
                            .stringField(METHOD_ID, command.getMethodId())
                            .build());
            ServiceExecutionException serviceException = response.getServiceException();
            if (nonNull(responseDataMapper)) {
                entityBuilder.valueField(RESPONSE_DATA, responseDataMapper.map(response.getResponseData()));
            }
            if (isNull(serviceException)) {
                return entityBuilder.build();
            }
            String errorCode = serviceException.getErrorCode();
            if (isEmpty(errorCode)) throw new ServiceMappingException(ERROR_CODE_IS_EMPTY);
            String errorMessage = serviceException.getErrorMessage();
            return entityBuilder.entityField(SERVICE_EXECUTION_EXCEPTION, entityBuilder()
                    .stringField(ERROR_CODE, errorCode)
                    .stringField(ERROR_MESSAGE, errorMessage)
                    .build())
                    .build();
        };
    }

    static <V> ValueToModelMapper.EntityToModelMapper<ServiceResponse<V>> toServiceResponse() {
        return toServiceResponse(null);
    }

    static <V> ValueFromModelMapper.EntityFromModelMapper<ServiceResponse<V>> fromServiceResponse() {
        return fromServiceResponse(null);
    }

    static <V> ValueMapper<ServiceResponse<V>, Entity> serviceResponseMapper(ValueToModelMapper<V, Value> responseToModelMapper, ValueFromModelMapper<V, ? extends Value> responseFromModelMapper) {
        return mapper(fromServiceResponse(responseFromModelMapper), toServiceResponse(responseToModelMapper));
    }
}