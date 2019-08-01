package ru.adk.service.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.service.exception.ServiceExecutionException;
import static ru.adk.entity.mapper.ValueMapper.mapper;
import static ru.adk.service.mapping.ServiceEntitiesMapping.ServiceExecutionExceptionMapping.ServiceExecutionExceptionFields.*;
import static ru.adk.service.model.ServiceMethodCommand.parseServiceCommand;

public interface ServiceEntitiesMapping {

    interface ServiceExecutionExceptionMapping {
        ValueToModelMapper<ServiceExecutionException, Entity> toServiceExecutionException = (entity) -> new ServiceExecutionException(
                parseServiceCommand(entity.getString(SERVICE_COMMAND)),
                entity.getString(ERROR_CODE),
                entity.getString(ERROR_MESSAGE)
        );
        ValueFromModelMapper<ServiceExecutionException, Entity> fromServiceExecutionException = (exception) -> Entity.entityBuilder()
                .stringField(ERROR_CODE, exception.getErrorCode())
                .stringField(ERROR_MESSAGE, exception.getMessage())
                .build();
        ValueMapper<ServiceExecutionException, Entity> serviceExecutionExceptionMapper = mapper(fromServiceExecutionException, toServiceExecutionException);

        interface ServiceExecutionExceptionFields {
            String SERVICE_COMMAND = "serviceCommand";
            String ERROR_CODE = "errorCode";
            String ERROR_MESSAGE = "errorMessage";
        }
    }
}
