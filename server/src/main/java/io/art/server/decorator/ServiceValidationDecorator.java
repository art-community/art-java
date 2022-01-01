/*
 * ART
 *
 * Copyright 2019-2022 ART
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

import io.art.core.exception.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.core.validation.*;
import io.art.server.configuration.*;
import reactor.core.publisher.*;
import static io.art.core.constants.ValidationConstants.ValidationErrorPatterns.*;
import static io.art.core.property.Property.*;
import static io.art.core.validation.Validators.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;
import java.util.function.*;

public class ServiceValidationDecorator implements UnaryOperator<Flux<Object>> {
    private final static ValidationException NULL_EXCEPTION = new ValidationException(notNull(expression -> INPUT_IS_NULL));
    private final Property<Boolean> enabled;
    private final Property<Boolean> deactivated;

    public ServiceValidationDecorator(ServiceMethodIdentifier id, ServerConfiguration configuration) {
        enabled = property(() -> configuration.isValidating(id)).listenConsumer(() -> configuration.getConsumer().validationConsumer());
        deactivated = property(() -> configuration.isDeactivated(id)).listenConsumer(() -> configuration.getConsumer().deactivationConsumer());
    }

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        if (disabled()) return input;
        return input.switchIfEmpty(error(NULL_EXCEPTION.fillInStackTrace())).doOnNext(this::validateReactiveData);
    }

    private void validateReactiveData(Object data) {
        if (disabled()) return;
        if (isNull(data)) throw (ValidationException) NULL_EXCEPTION.fillInStackTrace();
        Validatable requestData = (Validatable) data;
        requestData.validate(new Validator(requestData));
    }

    private boolean disabled() {
        return !enabled.get() || deactivated.get();
    }
}
