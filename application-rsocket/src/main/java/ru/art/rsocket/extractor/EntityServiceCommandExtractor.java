package ru.art.rsocket.extractor;

import ru.art.entity.Entity;
import ru.art.rsocket.exception.RsocketServerException;
import ru.art.service.model.ServiceMethodCommand;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.METHOD_ID_IS_EMPTY;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.SERVICE_ID_IS_EMPTY;
import static ru.art.rsocket.constants.RsocketModuleConstants.METHOD_ID;
import static ru.art.rsocket.constants.RsocketModuleConstants.SERVICE_ID;

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
