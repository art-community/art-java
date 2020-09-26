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

package io.art.server.validation;

import lombok.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.StringConstants.*;
import static java.text.MessageFormat.*;
import java.util.*;

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

    public String formatValidationErrorMessage(List<?> patternParameters) {
        return isEmpty(pattern) ? EMPTY_STRING : format(pattern, patternParameters.toArray());
    }
}
