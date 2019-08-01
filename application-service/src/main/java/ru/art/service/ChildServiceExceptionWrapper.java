package ru.art.service;

import ru.art.service.exception.ChildServiceException;
import ru.art.service.exception.ServiceExecutionException;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceResponse;
import static ru.art.service.constants.ServiceErrorCodes.CHILD_SERVICE_ERROR;
import static ru.art.service.factory.ServiceResponseFactory.errorResponse;

class ChildServiceExceptionWrapper extends ServiceExecutionExceptionWrapper {
    @Override
    public <RequestType, ResponseType> ServiceResponse<ResponseType> wrapServiceExecution(ServiceMethodCommand command, RequestType request) throws Exception {
        try {
            return previousWrapper.wrapServiceExecution(command, request);
        } catch (ChildServiceException e) {
            return errorResponse(command, CHILD_SERVICE_ERROR, (ServiceExecutionException) e.getCause());
        }
    }
}
