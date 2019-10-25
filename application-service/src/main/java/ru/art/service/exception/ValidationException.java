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

package ru.art.service.exception;

import lombok.*;
import ru.art.service.constants.*;
import ru.art.service.validation.*;
import static ru.art.core.caster.Caster.*;

public class ValidationException extends RuntimeException {
    @Getter
    private String fieldName;
    @Getter
    private ValidationExpressionType type;
    @Getter
    private ValidationExpression<?> expression;
    private Validatable validatableModel;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Validatable validatableModel) {
        super(message);
        this.validatableModel = validatableModel;
    }

    public ValidationException(Validatable validatableModel, ValidationExpression<?> expression) {
        super(expression.getValidationErrorMessage());
        this.fieldName = expression.getFieldName();
        this.type = expression.getType();
        this.expression = expression;
        this.validatableModel = validatableModel;
    }

    public <T extends Validatable> T getValidatableModel() {
        return cast(validatableModel);
    }
}
