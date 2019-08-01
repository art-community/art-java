package ru.art.service;

import ru.art.service.constants.ServiceErrorCodes;
import ru.art.service.exception.UnknownServiceMethodException;
import ru.art.service.factory.ServiceResponseFactory;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceResponse;

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
