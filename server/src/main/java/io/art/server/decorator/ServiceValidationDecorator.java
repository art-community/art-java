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

package io.art.server.decorator;

import io.art.core.lazy.*;
import io.art.core.model.*;
import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.exception.*;
import io.art.server.specification.*;
import io.art.server.validation.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.publisher.*;
import static io.art.core.lazy.ManagedValue.*;
import static io.art.logging.LoggingModule.*;
import static io.art.server.constants.ServerModuleConstants.RequestValidationPolicy.*;
import static io.art.server.constants.ServerModuleConstants.ValidationErrorPatterns.*;
import static io.art.server.module.ServerModule.*;
import static io.art.server.validation.Validators.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.*;
import java.util.function.*;

public class ServiceValidationDecorator implements UnaryOperator<Flux<Object>> {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(ServiceLoggingDecorator.class);
    private final UnaryOperator<Flux<Object>> decorator;
    private final Supplier<Boolean> enabled;
    private final Supplier<Boolean> deactivated;
    private final ManagedValue<Boolean> hasInput;

    public ServiceValidationDecorator(RequestValidationPolicy policy, ServiceMethodIdentifier serviceMethodId) {
        decorator = decorate(policy);
        hasInput = managed(() -> hasInput(serviceMethodId));
        enabled = () -> serverModule().configuration().isValidating(serviceMethodId);
        deactivated = () -> serverModule().configuration().isDeactivated(serviceMethodId);
    }

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        if (!enabled.get() || deactivated.get() || !hasInput.get()) {
            return input;
        }
        return decorator.apply(input);
    }

    private UnaryOperator<Flux<Object>> decorate(RequestValidationPolicy policy) {
        switch (policy) {
            case VALIDATABLE:
            case NOT_NULL:
                return input -> input
                        .switchIfEmpty(error(new ValidationException(notNull(expression -> REQUEST_IS_NULL))))
                        .doOnNext(data -> validateReactiveData(policy, data));
            case NON_VALIDATABLE:
                return UnaryOperator.identity();
        }
        return UnaryOperator.identity();
    }

    private boolean hasInput(ServiceMethodIdentifier serviceMethodId) {
        return serverModule()
                .configuration()
                .getRegistry()
                .findMethodById(serviceMethodId)
                .map(ServiceMethodSpecification::getInputMapper)
                .isPresent();
    }

    private void validateReactiveData(RequestValidationPolicy policy, Object data) {
        if (policy == NON_VALIDATABLE) {
            return;
        }
        if (policy == NOT_NULL) {
            if (isNull(data)) throw new ValidationException(notNull(expression -> REQUEST_IS_NULL));
            return;
        }
        if (isNull(data)) {
            throw new ValidationException(notNull(expression -> REQUEST_IS_NULL));
        }
        Validatable requestData = (Validatable) data;
        requestData.onValidating(new Validator(requestData));
    }
}
