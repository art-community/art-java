package ru.adk.service.interceptor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.adk.service.constants.ServiceExceptionsMessages;
import ru.adk.service.exception.ServiceInternalException;
import static java.util.Objects.isNull;

public interface ServiceExecutionInterceptor {
    static ServiceRequestInterceptor interceptRequest(ServiceRequestInterception interception) {
        if (isNull(interception)) throw new ServiceInternalException(ServiceExceptionsMessages.INTERCEPTION_IS_NULL);
        return new ServiceRequestInterceptor(interception);
    }

    static ServiceResponseInterceptor interceptResponse(ServiceResponseInterception interception) {
        if (isNull(interception)) throw new ServiceInternalException(ServiceExceptionsMessages.INTERCEPTION_IS_NULL);
        return new ServiceResponseInterceptor(interception);
    }

    @Getter
    @AllArgsConstructor
    class ServiceRequestInterceptor {
        private final ServiceRequestInterception interception;
    }

    @Getter
    @AllArgsConstructor
    class ServiceResponseInterceptor {
        private final ServiceResponseInterception interception;
    }
}
