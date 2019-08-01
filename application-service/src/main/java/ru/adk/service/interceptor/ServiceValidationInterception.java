package ru.adk.service.interceptor;

import ru.adk.service.exception.ValidationException;
import ru.adk.service.model.ServiceInterceptionResult;
import ru.adk.service.model.ServiceRequest;
import ru.adk.service.validation.Validatable;
import static java.util.Objects.isNull;
import static ru.adk.service.ServiceModule.serviceModule;
import static ru.adk.service.constants.RequestValidationPolicy.NON_VALIDATABLE;
import static ru.adk.service.constants.RequestValidationPolicy.NOT_NULL;
import static ru.adk.service.constants.ServiceExceptionsMessages.REQUEST_DATA_IS_NULL;
import static ru.adk.service.model.ServiceInterceptionResult.nextInterceptor;

public class ServiceValidationInterception implements ServiceRequestInterception {
    @Override
    @SuppressWarnings("Duplicates")
    public ServiceInterceptionResult intercept(ServiceRequest<?> request) {
        if (request.getValidationPolicy() == NOT_NULL) {
            if (isNull(request.getRequestData())) {
                throw new ValidationException(REQUEST_DATA_IS_NULL);
            }
            return nextInterceptor(request);
        }
        if (request.getValidationPolicy() == NON_VALIDATABLE) return nextInterceptor(request);
        if (isNull(request.getRequestData())) {
            throw new ValidationException(REQUEST_DATA_IS_NULL);
        }
        ((Validatable) request.getRequestData()).onValidating(serviceModule().getValidator());
        return nextInterceptor(request);
    }
}
