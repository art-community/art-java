package ru.art.service;

import ru.art.service.constants.ServiceErrorCodes;
import ru.art.service.factory.ServiceResponseFactory;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceResponse;

public class RuntimeExceptionWrapper extends ServiceExecutionExceptionWrapper {
    @Override
    public <RequestType, ResponseType> ServiceResponse<ResponseType> wrapServiceExecution(ServiceMethodCommand command, RequestType request) {
        try {
            return previousWrapper.wrapServiceExecution(command, request);
        } catch (Exception e) {
            return ServiceResponseFactory.errorResponse(command, ServiceErrorCodes.UNDECLARED_INTERNAL_ERROR, e);
        }
    }
}
