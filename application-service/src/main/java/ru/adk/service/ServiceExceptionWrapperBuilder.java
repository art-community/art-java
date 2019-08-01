package ru.adk.service;

import static java.util.Objects.isNull;
import static ru.adk.service.constants.ServiceExceptionsMessages.THROWABLE_EXCEPTION_WRAPPER_IS_NULL;
import static ru.adk.service.constants.ServiceExceptionsMessages.WRAPPER_IS_NULL;

public class ServiceExceptionWrapperBuilder {
    private ServiceExecutionExceptionWrapper exceptionWrapper;

    public static ServiceExceptionWrapperBuilder exceptionWrapperBuilder() {
        return new ServiceExceptionWrapperBuilder();
    }

    public ServiceExceptionWrapperBuilder addExceptionWrapper(ServiceExecutionExceptionWrapper wrapper) {
        if (isNull(wrapper)) {
            throw new NullPointerException(WRAPPER_IS_NULL);
        }
        if (isNull(exceptionWrapper)) {
            exceptionWrapper = wrapper;
            return this;
        }
        ServiceExecutionExceptionWrapper currentWrapper = exceptionWrapper;
        ServiceExecutionExceptionWrapper lastWrapper = exceptionWrapper.getPreviousWrapper();
        while (lastWrapper != null) {
            currentWrapper = lastWrapper;
            lastWrapper = currentWrapper.getPreviousWrapper();
        }
        currentWrapper.setPreviousWrapper(wrapper);
        return this;
    }

    public ServiceExceptionWrapperBuilder setThrowableExceptionWrapper(ServiceExecutionExceptionWrapper throwableExceptionWrapper) {
        if (isNull(throwableExceptionWrapper)) {
            throw new NullPointerException(THROWABLE_EXCEPTION_WRAPPER_IS_NULL);
        }
        if (isNull(exceptionWrapper)) {
            exceptionWrapper = throwableExceptionWrapper;
            return this;
        }
        throwableExceptionWrapper.setPreviousWrapper(exceptionWrapper);
        exceptionWrapper = throwableExceptionWrapper;
        return this;
    }

    public ServiceExecutionExceptionWrapper build() {
        addExceptionWrapper(new ServiceExecutionExceptionWrapper());
        return exceptionWrapper;
    }
}