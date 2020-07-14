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

import lombok.*;
import io.art.service.exception.*;

@AllArgsConstructor
public class Validator {
    private final Validatable model;

    @SafeVarargs
    public final <T> Validator validate(String fieldName, T value, ValidationExpression<T>... validationExpressions) {
        for (ValidationExpression<T> validationExpression : validationExpressions) {
            if (!validationExpression.evaluate(fieldName, value)) {
                throw new ValidationException(model, validationExpression);
            }
        }
        return this;
    }
//
//    @SafeVarargs
//    public final <T> Validator validate(String fieldName, T value, ValidationExpression<T> validationExpression, Object...params) {
//        if (!validationExpression.evaluate(fieldName, value)) {
//            throw new ValidationException(model, validationExpression, params);
//        }
//
//        return this;
//    }
}
