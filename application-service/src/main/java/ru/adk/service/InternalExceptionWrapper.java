package ru.adk.service;

import ru.adk.service.constants.ServiceErrorCodes;
import ru.adk.service.exception.ServiceInternalException;
import ru.adk.service.model.ServiceMethodCommand;
import ru.adk.service.model.ServiceResponse;
import static ru.adk.service.factory.ServiceResponseFactory.errorResponse;

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
