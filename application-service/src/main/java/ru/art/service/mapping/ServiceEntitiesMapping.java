package ru.art.service.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.service.exception.ServiceExecutionException;
import static ru.art.entity.mapper.ValueMapper.mapper;
import static ru.art.service.mapping.ServiceEntitiesMapping.ServiceExecutionExceptionMapping.ServiceExecutionExceptionFields.*;
import static ru.art.service.model.ServiceMethodCommand.parseServiceCommand;

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
