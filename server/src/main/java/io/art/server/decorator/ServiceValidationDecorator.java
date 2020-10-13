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

import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.exception.*;
import io.art.server.validation.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.publisher.*;
import static io.art.logging.LoggingModule.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionMessages.*;
import static io.art.server.constants.ServerModuleConstants.RequestValidationPolicy.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

public class ServiceValidationDecorator implements UnaryOperator<Flux<Object>> {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(ServiceLoggingDecorator.class);
    private final UnaryOperator<Flux<Object>> decorator;

    public ServiceValidationDecorator(RequestValidationPolicy policy) {
        decorator = input -> input.doOnNext(data -> validateReactiveData(policy, data));
    }

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        return decorator.apply(input);
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
