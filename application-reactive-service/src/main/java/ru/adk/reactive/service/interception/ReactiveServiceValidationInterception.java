package ru.adk.reactive.service.interception;

import reactor.core.publisher.Flux;
import ru.adk.reactive.service.specification.ReactiveServiceSpecification;
import ru.adk.service.Specification;
import ru.adk.service.exception.ValidationException;
import ru.adk.service.interceptor.ServiceValidationInterception;
import ru.adk.service.model.ServiceInterceptionResult;
import ru.adk.service.model.ServiceRequest;
import ru.adk.service.validation.Validatable;
import static java.util.Objects.isNull;
import static reactor.core.publisher.Flux.from;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.REACTIVE_SERVICE_TYPE;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode;
import static ru.adk.reactive.service.model.ReactiveService.ReactiveMethod;
import static ru.adk.service.ServiceModule.serviceModule;
import static ru.adk.service.constants.RequestValidationPolicy.NON_VALIDATABLE;
import static ru.adk.service.constants.RequestValidationPolicy.NOT_NULL;
import static ru.adk.service.constants.ServiceExceptionsMessages.REQUEST_DATA_IS_NULL;
import static ru.adk.service.model.ServiceInterceptionResult.nextInterceptor;

public class ReactiveServiceValidationInterception extends ServiceValidationInterception {
    @Override
    @SuppressWarnings("Duplicates")
    public ServiceInterceptionResult intercept(ServiceRequest<?> request) {
        String serviceId = request.getServiceMethodCommand().getServiceId();
        String methodId = request.getServiceMethodCommand().getMethodId();
        Specification serviceSpecification = serviceModule().getServiceRegistry().getService(serviceId);

        if (!serviceSpecification.getServiceTypes().contains(REACTIVE_SERVICE_TYPE)) {
            return super.intercept(request);
        }

        if (request.getValidationPolicy() == NOT_NULL) {
            if (isNull(request.getRequestData()))
                throw new ValidationException(REQUEST_DATA_IS_NULL);
            return nextInterceptor(request);
        }
        if (request.getValidationPolicy() == NON_VALIDATABLE) return nextInterceptor(request);
        if (isNull(request.getRequestData()))
            throw new ValidationException(REQUEST_DATA_IS_NULL);

        ReactiveServiceSpecification reactiveServiceSpecification = (ReactiveServiceSpecification) serviceSpecification;
        ReactiveMethod reactiveMethod;
        if (isNull(reactiveMethod = reactiveServiceSpecification.getReactiveService().getMethods().get(methodId))) {
            return super.intercept(request);
        }

        ReactiveMethodProcessingMode requestProcessingMode = reactiveMethod.requestProcessingMode();
        switch (requestProcessingMode) {
            case STRAIGHT:
                return super.intercept(request);
            case REACTIVE:
                Flux<?> requestDataStream = from(cast(request.getRequestData()))
                        .doOnNext(payload -> ((Validatable) payload).onValidating(serviceModule().getValidator()));
                return nextInterceptor(request, cast(new ServiceRequest<Flux<?>>(request.getServiceMethodCommand(), request.getValidationPolicy(), requestDataStream)));
        }
        return nextInterceptor(request);
    }
}
