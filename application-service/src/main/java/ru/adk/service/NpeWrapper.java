package ru.adk.service;

import ru.adk.service.constants.ServiceErrorCodes;
import ru.adk.service.factory.ServiceResponseFactory;
import ru.adk.service.model.ServiceMethodCommand;
import ru.adk.service.model.ServiceResponse;

public class NpeWrapper extends ServiceExecutionExceptionWrapper {

    @Override
    public <RequestType, ResponseType> ServiceResponse<ResponseType> wrapServiceExecution(ServiceMethodCommand command, RequestType request) throws Exception {
        try {
            return previousWrapper.wrapServiceExecution(command, request);
        } catch (NullPointerException e) {
            return ServiceResponseFactory.errorResponse(command, ServiceErrorCodes.NPE, e);
        }
    }
}
