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

@Getter
public class ValidationException extends RuntimeException {
    private String fieldName;
    private ValidationExpressionType type;
    private ValidationExpression<?> expression;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public ValidationException(String fieldName, String message, ValidationExpressionType type) {
        super(message);
        this.fieldName = fieldName;
        this.type = type;
    }

    public ValidationException(ValidationExpression<?> expression) {
        super(expression.getValidationErrorMessage());
        this.fieldName = expression.getFieldName();
        this.type = expression.getType();
        this.expression = expression;
    }
}
