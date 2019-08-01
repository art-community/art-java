package ru.art.service.factory;

import ru.art.service.constants.RequestValidationPolicy;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceRequest;
import static ru.art.service.constants.RequestValidationPolicy.NON_VALIDATABLE;
import static ru.art.service.constants.RequestValidationPolicy.NOT_NULL;

public interface ServiceRequestFactory {
    static <T> ServiceRequest<T> newServiceRequest(ServiceMethodCommand command) {
        return new ServiceRequest<>(command, NON_VALIDATABLE);
    }

    static <T> ServiceRequest<T> newServiceRequest(ServiceMethodCommand command, T primitiveData) {
        return new ServiceRequest<>(command, NON_VALIDATABLE, primitiveData);
    }

    static <T> ServiceRequest<T> newServiceRequest(ServiceMethodCommand command, RequestValidationPolicy validationPolicy) {
        return new ServiceRequest<>(command, validationPolicy);
    }

    static <T> ServiceRequest<T> newPrimitiveRequest(ServiceMethodCommand command, T primitiveData) {
        return newServiceRequest(command, primitiveData, NOT_NULL);
    }

    static <T> ServiceRequest<T> newServiceRequest(ServiceMethodCommand command, T requestData, RequestValidationPolicy validationPolicy) {
        return new ServiceRequest<>(command, validationPolicy, requestData);
    }
}
