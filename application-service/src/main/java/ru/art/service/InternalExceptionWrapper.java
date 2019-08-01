package ru.art.service;

import ru.art.service.constants.ServiceErrorCodes;
import ru.art.service.exception.ServiceInternalException;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceResponse;
import static ru.art.service.factory.ServiceResponseFactory.errorResponse;

public class InternalExceptionWrapper extends ServiceExecutionExceptionWrapper {

    @Override
    public <RequestType, ResponseType> ServiceResponse<ResponseType> wrapServiceExecution(ServiceMethodCommand command, RequestType request) throws Exception {
        try {
            return previousWrapper.wrapServiceExecution(command, request);
        } catch (ServiceInternalException e) {
            return errorResponse(command, ServiceErrorCodes.INTERNAL_ERROR, e);
        }
    }
}
