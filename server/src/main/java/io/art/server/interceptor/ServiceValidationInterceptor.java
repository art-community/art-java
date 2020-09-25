/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.server.interceptor;

import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.exception.*;
import io.art.server.service.specification.*;
import io.art.server.service.validation.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.*;
import static io.art.server.constants.ServerModuleConstants.RequestValidationPolicy.*;
import static java.util.Objects.*;

public class ServiceValidationInterceptor implements ServiceExecutionInterceptor<Object, Object> {
    @Override
    public void intercept(ServiceInterceptionContext<Object, Object> context) {
        ServiceMethodSpecification specification = context.getImplementation().getMethodSpecification();
        switch (specification.getRequestProcessingMode()) {
            case BLOCKING:
                validateBlocking(context);
                return;
            case REACTIVE_MONO:
                validateReactiveMono(context);
                return;
            case REACTIVE_FLUX:
                validateReactiveFlux(context);
        }
    }

    private void validateBlocking(ServiceInterceptionContext<Object, Object> context) {
        RequestValidationPolicy policy = context.getImplementation().getMethodSpecification().getValidationPolicy();
        Object request = context.getRequest();
        if (policy == NOT_NULL) {
            if (isNull(request)) throw new ValidationException(REQUEST_IS_NULL);
        }
        if (policy == NON_VALIDATABLE) {
            context.process();
            return;
        }
        if (isNull(request)) {
            throw new ValidationException(REQUEST_IS_NULL);
        }
        Validatable requestData = (Validatable) request;
        requestData.onValidating(new Validator(requestData));
        context.process();
    }

    private void validateReactiveMono(ServiceInterceptionContext<Object, Object> context) {
        RequestValidationPolicy policy = context.getImplementation().getMethodSpecification().getValidationPolicy();
        context.processRequest(Mono.from(cast(context.getRequest())).doOnNext(data -> validateReactivePayload(policy, data)));
    }

    private void validateReactiveFlux(ServiceInterceptionContext<Object, Object> context) {
        RequestValidationPolicy policy = context.getImplementation().getMethodSpecification().getValidationPolicy();
        context.processRequest(Flux.from(cast(context.getRequest())).doOnNext(data -> validateReactivePayload(policy, data)));
    }

    private void validateReactivePayload(RequestValidationPolicy policy, Object data) {
        if (policy == NOT_NULL) {
            if (isNull(data)) throw new ValidationException(REQUEST_IS_NULL);
        }
        if (policy == NON_VALIDATABLE) {
            return;
        }
        if (isNull(data)) {
            throw new ValidationException(REQUEST_IS_NULL);
        }
        Validatable requestData = (Validatable) data;
        requestData.onValidating(new Validator(requestData));
    }
}
