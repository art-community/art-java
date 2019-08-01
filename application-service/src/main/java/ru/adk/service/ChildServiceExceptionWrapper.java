package ru.adk.service;

import ru.adk.service.exception.ChildServiceException;
import ru.adk.service.exception.ServiceExecutionException;
import ru.adk.service.model.ServiceMethodCommand;
import ru.adk.service.model.ServiceResponse;
import static ru.adk.service.constants.ServiceErrorCodes.CHILD_SERVICE_ERROR;
import static ru.adk.service.factory.ServiceResponseFactory.errorResponse;

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
