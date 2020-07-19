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

package io.art.server.service.exception;

import lombok.Getter;
import io.art.server.service.validation.Validatable;
import io.art.server.service.validation.ValidationExpression;

import static io.art.core.caster.Caster.cast;
import java.util.*;

public class ValidationException extends RuntimeException {
    @Getter
    private String fieldName;
    @Getter
    private String type;
    @Getter
    private ValidationExpression<?> expression;
    private Validatable validatableModel;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Validatable validatable) {
        super(message);
        this.validatableModel = validatable;
    }

    public ValidationException(Validatable validatableModel, ValidationExpression<?> expression) {
        super(expression.getValidationErrorMessage());
        this.fieldName = expression.getFieldName();
        this.type = expression.getType();
        this.expression = expression;
        this.validatableModel = validatableModel;
    }

    public ValidationException(Validatable validatableModel, ValidationExpression<?> expression, List<?> patternParameters) {
        super(expression.formatValidationErrorMessage(patternParameters));
        this.fieldName = expression.getFieldName();
        this.type = expression.getType();
        this.expression = expression;
        this.validatableModel = validatableModel;
    }

    public <T extends Validatable> T getValidatableModel() {
        return cast(validatableModel);
    }
}
