package ru.art.service.interceptor;

import ru.art.service.exception.*;
import ru.art.service.model.*;
import ru.art.service.validation.*;
import static java.util.Objects.*;
import static ru.art.service.ServiceModule.*;
import static ru.art.service.constants.RequestValidationPolicy.*;
import static ru.art.service.constants.ServiceExceptionsMessages.*;
import static ru.art.service.model.ServiceInterceptionResult.*;

public class ServiceValidationInterception implements ServiceRequestInterception {
    @Override
    public ServiceInterceptionResult intercept(ServiceRequest<?> request) {
        if (request.getValidationPolicy() == NOT_NULL) {
            if (isNull(request.getRequestData())) throw new ValidationException(REQUEST_DATA_IS_NULL);
            return nextInterceptor(request);
        }
        if (request.getValidationPolicy() == NON_VALIDATABLE) return nextInterceptor(request);
        if (isNull(request.getRequestData())) {
            return stopHandling(request, ServiceResponse.builder()
                    .command(request.getServiceMethodCommand())
                    .serviceException(new ServiceExecutionException(request.getServiceMethodCommand(), REQUEST_DATA_IS_NULL_CODE, new ValidationException(REQUEST_DATA_IS_NULL)))
                    .build());
        }
        try {
            ((Validatable) request.getRequestData()).onValidating(serviceModule().getValidator());
        } catch (Throwable e) {
            return stopHandling(request, ServiceResponse.builder()
                    .command(request.getServiceMethodCommand())
                    .serviceException(new ServiceExecutionException(request.getServiceMethodCommand(), VALIDATION_EXCEPTION_CODE, e))
                    .build());
        }
        return nextInterceptor(request);
    }
}
