/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.core.exception;

import io.art.core.validation.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

public class ValidationException extends RuntimeException {
    @Getter
    private final ValidationExpression<?> expression;
    private Validatable model;

    public ValidationException(ValidationExpression<?> expression) {
        super(expression.getErrorMessage());
        this.expression = expression;
    }

    public ValidationException(Validatable model, ValidationExpression<?> expression) {
        super(expression.getErrorMessage());
        this.expression = expression;
        this.model = model;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + newLineTabulation(2) + stream(getStackTrace())
                .map(StackTraceElement::toString)
                .collect(joining(newLineTabulation(2)));
    }

    public <T extends Validatable> T model() {
        return cast(model);
    }
}
