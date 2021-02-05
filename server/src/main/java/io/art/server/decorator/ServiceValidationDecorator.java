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

import io.art.core.property.*;
import io.art.core.model.*;
import io.art.server.exception.*;
import io.art.server.specification.*;
import io.art.server.validation.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.publisher.*;
import static io.art.core.property.DisposableProperty.*;
import static io.art.logging.LoggingModule.*;
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
    private final static ValidationException NULL_EXCEPTION = new ValidationException(notNull(expression -> REQUEST_IS_NULL));
    private final Supplier<Boolean> enabled;
    private final Supplier<Boolean> deactivated;
    private final DisposableProperty<Boolean> hasInput;

    public ServiceValidationDecorator(ServiceMethodIdentifier serviceMethodId) {
        enabled = () -> serverModule().configuration().isValidating(serviceMethodId);
        deactivated = () -> serverModule().configuration().isDeactivated(serviceMethodId);
        hasInput = disposable(() -> hasInput(serviceMethodId));
    }

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        if (!enabled.get() || deactivated.get() || !hasInput.get()) {
            return input;
        }
        return input.switchIfEmpty(error(NULL_EXCEPTION.fillInStackTrace())).doOnNext(this::validateReactiveData);
    }

    private boolean hasInput(ServiceMethodIdentifier serviceMethodId) {
        return serverModule()
                .configuration()
                .getRegistry()
                .findMethodById(serviceMethodId)
                .map(ServiceMethodSpecification::getInputMapper)
                .isPresent();
    }

    private void validateReactiveData(Object data) {
        if (isNull(data)) throw (ValidationException) NULL_EXCEPTION.fillInStackTrace();
        Validatable requestData = (Validatable) data;
        requestData.onValidating(new Validator(requestData));
    }
}
