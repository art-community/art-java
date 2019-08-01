package ru.adk.rsocket.extractor;

import ru.adk.entity.Entity;
import ru.adk.rsocket.exception.RsocketServerException;
import ru.adk.service.model.ServiceMethodCommand;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.rsocket.constants.RsocketModuleConstants.ExceptionMessages.METHOD_ID_IS_EMPTY;
import static ru.adk.rsocket.constants.RsocketModuleConstants.ExceptionMessages.SERVICE_ID_IS_EMPTY;
import static ru.adk.rsocket.constants.RsocketModuleConstants.METHOD_ID;
import static ru.adk.rsocket.constants.RsocketModuleConstants.SERVICE_ID;

public interface EntityServiceCommandExtractor {
    static ServiceMethodCommand extractServiceMethodCommand(Entity serviceRequestEntity) {
        String serviceId;
        if (isEmpty(serviceId = serviceRequestEntity.getString(SERVICE_ID))) {
            throw new RsocketServerException(SERVICE_ID_IS_EMPTY);
        }
        String methodId;
        if (isEmpty(methodId = serviceRequestEntity.getString(METHOD_ID))) {
            throw new RsocketServerException(METHOD_ID_IS_EMPTY);
        }
        return new ServiceMethodCommand(serviceId, methodId);
    }
}
