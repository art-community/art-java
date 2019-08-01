package ru.adk.service.factory;

import ru.adk.service.exception.ServiceExecutionException;
import ru.adk.service.model.ServiceMethodCommand;
import ru.adk.service.model.ServiceResponse;

public interface ServiceResponseFactory {
    static <T> ServiceResponse<T> okResponse(ServiceMethodCommand command) {
        return ServiceResponse.<T>builder().command(command).build();
    }

    static <T> ServiceResponse<T> errorResponse(ServiceMethodCommand command, String errorCode, String errorMessage) {
        return ServiceResponse.<T>builder().command(command).serviceException(new ServiceExecutionException(command, errorCode, errorMessage)).build();
    }

    static <T> ServiceResponse<T> errorResponse(ServiceMethodCommand command, ServiceExecutionException exception) {
        return ServiceResponse.<T>builder().command(command).serviceException(exception).build();
    }

    static <T> ServiceResponse<T> errorResponse(ServiceMethodCommand command, String errorCode, Exception e) {
        return ServiceResponse.<T>builder().command(command).serviceException(new ServiceExecutionException(command, errorCode, e)).build();
    }

    static <T> ServiceResponse<T> okResponse(ServiceMethodCommand command, T responseData) {
        return ServiceResponse.<T>builder().command(command).responseData(responseData).build();
    }
}
