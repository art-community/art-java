package ru.adk.service;

import ru.adk.service.constants.ServiceErrorCodes;
import ru.adk.service.exception.UnknownServiceMethodException;
import ru.adk.service.factory.ServiceResponseFactory;
import ru.adk.service.model.ServiceMethodCommand;
import ru.adk.service.model.ServiceResponse;

public class UnknownServiceMethodExceptionWrapper extends ServiceExecutionExceptionWrapper {
    @Override
    public <RequestType, ResponseType> ServiceResponse<ResponseType> wrapServiceExecution(ServiceMethodCommand command, RequestType request) throws Exception {
        try {
            return previousWrapper.wrapServiceExecution(command, request);
        } catch (UnknownServiceMethodException e) {
            return ServiceResponseFactory.errorResponse(command, ServiceErrorCodes.UNKNOWN_METHOD_ERROR, e);
        }
    }
}
