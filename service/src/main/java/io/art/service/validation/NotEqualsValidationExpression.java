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

package io.art.service.validation;

import io.art.service.constants.ServiceExceptionsMessages;

import static java.text.MessageFormat.format;
import static io.art.service.constants.ValidationExpressionType.NOT_EQUALS;

class NotEqualsValidationExpression extends ValidationExpression<Object> {
    private Object other;

    NotEqualsValidationExpression(Object other) {
        super(NOT_EQUALS);
        this.other = other;
    }

    NotEqualsValidationExpression(Object other, String pattern) {
        super(NOT_EQUALS);
        this.other = other;
        this.pattern = pattern;
    }

    @Override
    public boolean evaluate(String fieldName, Object value) {
        return super.evaluate(fieldName, value) && !value.equals(other);
    }

    @Override
    public String getValidationErrorMessage() {
        return format(ServiceExceptionsMessages.EQUALS_VALIDATION_ERROR, fieldName, value, other);
    }
}
