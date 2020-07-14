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

package io.art.service.validation;

import io.art.service.constants.ServiceExceptionsMessages;

import java.util.List;

import static java.text.MessageFormat.format;
import static io.art.service.constants.ValidationExpressionType.CONTAINS;

class ContainsValidationExpression extends ValidationExpression<Object> {
    private List<?> objectList;

    ContainsValidationExpression(List<?> objectList) {
        super(CONTAINS);
        this.objectList = objectList;
    }

    ContainsValidationExpression(List<?> objectList, String pattern) {
        super(CONTAINS);
        this.objectList = objectList;
        this.pattern = pattern;
    }

    @Override
    public boolean evaluate(String fieldName, Object value) {
        return super.evaluate(fieldName, value) && objectList.contains(value);
    }

    @Override
    public String getValidationErrorMessage() {
        return format(ServiceExceptionsMessages.NOT_CONTAINS_VALIDATION_ERROR, fieldName, value, objectList.toString());
    }
}
