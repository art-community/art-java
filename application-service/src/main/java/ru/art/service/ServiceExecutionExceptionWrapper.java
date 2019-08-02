package ru.art.service;

import lombok.Getter;
import lombok.Setter;
import ru.art.service.constants.ServiceErrorCodes;
import ru.art.service.exception.ServiceExecutionException;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceRequest;
import ru.art.service.model.ServiceResponse;
import static java.util.Objects.isNull;
import static ru.art.service.ServiceModule.serviceModule;
import static ru.art.service.constants.ServiceExceptionsMessages.WRAPPER_IS_NULL;
import static ru.art.service.execution.ServiceExecutor.executeServiceWithConfiguration;
import static ru.art.service.factory.ServiceResponseFactory.errorResponse;
import static ru.art.service.factory.ServiceResponseFactory.okResponse;

public class ServiceExecutionExceptionWrapper {
    @Getter
    @Setter
    protected ServiceExecutionExceptionWrapper previousWrapper;

    <RequestType, ResponseType> ServiceResponse<ResponseType> executeServiceWrapped(ServiceMethodCommand command, ServiceRequest<RequestType> request) {
        try {
            return wrapServiceExecution(command, request.getRequestData());
        } catch (ServiceExecutionException e) {
            return errorResponse(command, e);
        } catch (Exception e) {
            return errorResponse(command, ServiceErrorCodes.UNCAUGHT_INTERNAL_ERROR, e);
        }
    }

    public ServiceExecutionExceptionWrapper addExceptionWrapper(ServiceExecutionExceptionWrapper wrapper) {
        if (isNull(wrapper)) {
            throw new NullPointerException(WRAPPER_IS_NULL);
        }
        if (isNull(previousWrapper)) {
            previousWrapper = wrapper;
            return this;
        }
        ServiceExecutionExceptionWrapper currentWrapper = previousWrapper;
        ServiceExecutionExceptionWrapper serviceExecutionExceptionWrapper = previousWrapper.getPreviousWrapper();

        while (serviceExecutionExceptionWrapper != null && !serviceExecutionExceptionWrapper.getClass().equals(ServiceExecutionExceptionWrapper.class)) {
            currentWrapper = serviceExecutionExceptionWrapper;
            serviceExecutionExceptionWrapper = currentWrapper.getPreviousWrapper();
        }

        if (isNull(serviceExecutionExceptionWrapper)) {
            return this;
        }

        wrapper.setPreviousWrapper(serviceExecutionExceptionWrapper);
        currentWrapper.setPreviousWrapper(wrapper);
        return this;
    }


    public <RequestType, ResponseType> ServiceResponse<ResponseType> wrapServiceExecution(ServiceMethodCommand command, RequestType request) throws Exception {
        Specification service = serviceModule().getServiceRegistry().getService(command.getServiceId());
        ResponseType responseData = executeServiceWithConfiguration(() -> service.executeMethod(command.getMethodId(), request), command, service.getExecutionConfiguration());
        return okResponse(command, responseData);
    }
}
