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

import io.art.core.model.*;
import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.exception.*;
import io.art.server.specification.*;
import io.art.server.validation.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.model.InterceptionResult.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.*;
import static io.art.server.constants.ServerModuleConstants.RequestValidationPolicy.*;
import static java.util.Objects.*;

public class ServiceValidationInterceptor implements ServiceMethodInterceptor<Object, Object> {
    @Override
    public InterceptionResult interceptRequest(Object request, ServiceMethodSpecification specification) {
        switch (specification.getRequestMode()) {
            case BLOCKING:
                validateBlockingRequest(request, specification);
                break;
            case MONO:
                request = orElse(request, Mono.empty());
                return next(validateReactiveMono(Mono.from(cast(request)), specification));
            case FLUX:
                request = orElse(request, Flux.empty());
                return next(validateReactiveFlux(Flux.from(cast(request)), specification));
        }
        return next(request);
    }

    @Override
    public InterceptionResult interceptResponse(Object response, ServiceMethodSpecification specification) {
        return next(response);
    }

    @Override
    public ExceptionInterceptionResult interceptException(Throwable throwable, ServiceMethodSpecification specification) {
        return ExceptionInterceptionResult.next(throwable);
    }

    private static void validateBlockingRequest(Object request, ServiceMethodSpecification specification) {
        RequestValidationPolicy policy = specification.getValidationPolicy();
        if (policy == NOT_NULL) {
            if (isNull(request)) throw new ValidationException(REQUEST_IS_NULL);
        }
        if (policy == NON_VALIDATABLE) {
            return;
        }
        if (isNull(request)) {
            throw new ValidationException(REQUEST_IS_NULL);
        }
        Validatable requestData = (Validatable) request;
        requestData.onValidating(new Validator(requestData));
    }

    private static Mono<Object> validateReactiveMono(Mono<Object> request, ServiceMethodSpecification specification) {
        RequestValidationPolicy policy = specification.getValidationPolicy();
        return request.doOnNext(data -> validateReactiveData(policy, data));
    }

    private static Flux<Object> validateReactiveFlux(Flux<Object> request, ServiceMethodSpecification specification) {
        RequestValidationPolicy policy = specification.getValidationPolicy();
        return request.doOnNext(data -> validateReactiveData(policy, data));
    }

    private static void validateReactiveData(RequestValidationPolicy policy, Object data) {
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
