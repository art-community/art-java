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

package ru.art.service.validation;

import lombok.Getter;

import static java.text.MessageFormat.format;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;

@Getter
public abstract class ValidationExpression<T> {
    protected String fieldName;
    protected T value;
    protected final String type;
    protected String pattern;

    protected ValidationExpression(String type) {
        this.type = type;
    }

    public boolean evaluate(String fieldName, T value) {
        this.fieldName = fieldName;
        this.value = value;
        return value != null;
    }

    public abstract String getValidationErrorMessage();

    public String getValidationErrorMessageWithPattern(Object... params) {
        return isEmpty(pattern) ? EMPTY_STRING : format(pattern, params);
    }
}
