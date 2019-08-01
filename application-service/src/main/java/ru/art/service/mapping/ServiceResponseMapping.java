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
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.mapper.ValueMapper.mapper;
import static ru.art.service.constants.ServiceExceptionsMessages.*;

public interface ServiceResponseMapping {
    String SERVICE_METHOD_COMMAND = "serviceMethodCommand";
    String SERVICE_EXECUTION_EXCEPTION = "serviceExecutionException";
    String SERVICE_ID = "serviceId";
    String METHOD_ID = "methodId";
    String RESPONSE_DATA = "responseData";
    String ERROR_CODE = "errorCode";
    String ERROR_MESSAGE = "errorMessage";

    @SuppressWarnings("Duplicates")
    static <V> ValueToModelMapper.EntityToModelMapper<ServiceResponse<V>> toServiceResponse(final ValueToModelMapper<V, Value> responseDataMapper) {
        return value -> {
            ServiceResponse.ServiceResponseBuilder<V> serviceResponseBuilder = ServiceResponse.builder();
            V responseData = isNull(responseDataMapper) ? null : value.getValue(RESPONSE_DATA, responseDataMapper);
            if (nonNull(responseData)) serviceResponseBuilder.responseData(responseData);
            Entity serviceExceptionEntity = value.getEntity(SERVICE_EXECUTION_EXCEPTION);
            if (isNull(serviceExceptionEntity)) {
                return serviceResponseBuilder.build();
            }
            Entity serviceMethodCommandEntity = serviceExceptionEntity.getEntity(SERVICE_METHOD_COMMAND);
            if (isNull(serviceMethodCommandEntity)) throw new ServiceMappingException(SERVICE_COMMAND_IS_NULL);
            String serviceId = serviceMethodCommandEntity.getString(SERVICE_ID);
            if (isNull(serviceId)) throw new ServiceMappingException(SERVICE_ID_IS_NULL);
            String methodId = serviceMethodCommandEntity.getString(METHOD_ID);
            if (isNull(methodId)) throw new ServiceMappingException(METHOD_ID_IS_NULL);
            ServiceMethodCommand serviceMethodCommand = new ServiceMethodCommand(serviceId, methodId);
            String errorCode = serviceExceptionEntity.getString(ERROR_CODE);
            if (isEmpty(errorCode)) throw new ServiceMappingException(ERROR_CODE_IS_EMPTY);
            ServiceExecutionException serviceException = new ServiceExecutionException(serviceMethodCommand, errorCode, serviceExceptionEntity.getString(ERROR_MESSAGE));
            return serviceResponseBuilder.serviceException(serviceException).build();
        };
    }

    static <V> ValueFromModelMapper.EntityFromModelMapper<ServiceResponse<V>> fromServiceResponse(final ValueFromModelMapper<V, ? extends Value> responseDataMapper) {
        return response -> {
            Entity.EntityBuilder entityBuilder = entityBuilder();
            ServiceExecutionException serviceException = response.getServiceException();
            if (nonNull(responseDataMapper)) {
                entityBuilder.valueField(RESPONSE_DATA, responseDataMapper.map(response.getResponseData()));
            }
            if (isNull(serviceException)) {
                return entityBuilder.build();
            }
            ServiceMethodCommand serviceMethodCommand = response.getCommand();
            if (isNull(serviceMethodCommand)) throw new ServiceMappingException(SERVICE_COMMAND_IS_NULL);
            String serviceId = serviceMethodCommand.getServiceId();
            if (isNull(serviceId)) throw new ServiceMappingException(SERVICE_ID_IS_NULL);
            String methodId = serviceMethodCommand.getMethodId();
            if (isNull(methodId)) throw new ServiceMappingException(METHOD_ID_IS_NULL);
            String errorCode = serviceException.getErrorCode();
            if (isEmpty(errorCode)) throw new ServiceMappingException(ERROR_CODE_IS_EMPTY);
            String errorMessage = serviceException.getErrorMessage();
            return entityBuilder
                    .stringField(SERVICE_METHOD_COMMAND, response.getCommand().toString())
                    .stringField(ERROR_CODE, errorCode)
                    .stringField(ERROR_MESSAGE, errorMessage)
                    .entityField(SERVICE_EXECUTION_EXCEPTION, entityBuilder()
                            .stringField(SERVICE_ID, serviceId)
                            .stringField(METHOD_ID, methodId)
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