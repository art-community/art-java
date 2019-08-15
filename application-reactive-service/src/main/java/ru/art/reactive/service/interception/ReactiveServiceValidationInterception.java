/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.reactive.service.interception;

import reactor.core.publisher.Flux;
import ru.art.reactive.service.specification.ReactiveServiceSpecification;
import ru.art.service.Specification;
import ru.art.service.exception.ValidationException;
import ru.art.service.interceptor.ServiceValidationInterception;
import ru.art.service.model.ServiceInterceptionResult;
import ru.art.service.model.ServiceRequest;
import ru.art.service.validation.Validatable;
import static java.util.Objects.isNull;
import static reactor.core.publisher.Flux.from;
import static ru.art.core.caster.Caster.cast;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.REACTIVE_SERVICE_TYPE;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode;
import static ru.art.reactive.service.model.ReactiveService.ReactiveMethod;
import static ru.art.service.ServiceModule.serviceModule;
import static ru.art.service.constants.RequestValidationPolicy.NON_VALIDATABLE;
import static ru.art.service.constants.RequestValidationPolicy.NOT_NULL;
import static ru.art.service.constants.ServiceExceptionsMessages.REQUEST_DATA_IS_NULL;
import static ru.art.service.model.ServiceInterceptionResult.nextInterceptor;

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
