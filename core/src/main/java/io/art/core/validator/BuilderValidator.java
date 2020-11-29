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

package io.art.core.validator;

import lombok.*;
import io.art.core.checker.*;
import io.art.core.exception.*;
import static io.art.core.factory.SetFactory.setOf;
import static java.text.MessageFormat.*;
import static io.art.core.constants.BuilderValidatorErrors.*;
import static io.art.core.constants.ExceptionMessages.*;
import static io.art.core.constants.StringConstants.*;
import java.util.*;
import java.util.function.*;

@AllArgsConstructor
public class BuilderValidator {
    private final String builderName;
    private final Set<BuilderFieldValidationError> validationErrors = setOf();

    @SuppressWarnings("WeakerAccess")
    public <T> T checkField(T value, BuilderFieldValidationError error, Predicate<T> predicate) {
        if (!predicate.test(value)) {
            validationErrors.add(error);
        }
        return value;
    }

    public <T> T notEmptyField(T value, String field) {
        return checkField(value, new BuilderFieldValidationError(field, format(FIELD_MUST_NOT_BE_EMPTY, field)), EmptinessChecker::isNotEmpty);
    }

    public <T> T notNullField(T value, String field) {
        return checkField(value, new BuilderFieldValidationError(field, format(FIELD_MUST_NOT_BE_NULL, field)), Objects::nonNull);
    }

    public void validate() {
        if (!validationErrors.isEmpty()) {
            throw new BuilderValidationException(buildErrorMessage());
        }
    }

    private String buildErrorMessage() {
        StringBuilder stringBuilder = new StringBuilder(format(BUILDER_VALIDATOR_HAS_NEXT_ERRORS, builderName));
        validationErrors.forEach(error -> stringBuilder.append(NEW_LINE).append(error));
        return stringBuilder.toString();
    }

    @AllArgsConstructor
    public static class BuilderFieldValidationError {
        private final String field;
        private final String error;

        public static BuilderFieldValidationError fieldError(String field, String error) {
            return new BuilderFieldValidationError(field, error);
        }

        @Override
        public String toString() {
            return field + COLON + error;
        }
    }
}
