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

import static ru.art.core.caster.Caster.*;
import java.util.*;

public interface ValidationExpressions {
    static <T> ValidationExpression<T> notNull() {
        return cast(new NotNullValidationExpression());
    }

    static <T> ValidationExpression<T> notEmptyString() {
        return cast(new NotEmptyStringValidationExpression());
    }

    static <T> ValidationExpression<T> notEmptyCollection() {
        return cast(new NotEmptyCollectionValidationExpression());
    }

    static <T> ValidationExpression<T> notEmptyMap() {
        return cast(new NotEmptyMapValidationExpression());
    }

    static <T> ValidationExpression<T> notEqualsOther(Object other) {
        return cast(new NotEqualsValidationExpression(other));
    }

    static <T> ValidationExpression<T> equalsOther(Object other) {
        return cast(new EqualsValidationExpression(other));
    }

    static <T> ValidationExpression<T> containsOther(List<Object> list) {
        return cast(new ContainsValidationExpression(list));
    }

    static <T> ValidationExpression<T> betweenInt(Integer lower, Integer greater) {
        return cast(BetweenIntValidationExpression.builder()
                .greaterValue(greater)
                .lowerValue(lower)
                .build());
    }

    static <T> ValidationExpression<T> betweenLong(Long lower, Long greater) {
        return cast(BetweenLongValidationExpression.builder()
                .greaterValue(greater)
                .lowerValue(lower)
                .build());
    }

    static <T> ValidationExpression<T> moreThanLong(Long lower) {
        return cast(BetweenLongValidationExpression.builder()
                .greaterValue(lower)
                .lowerValue(Long.MAX_VALUE)
                .build());
    }

    static <T> ValidationExpression<T> lessThanLong(Long greater) {
        return cast(BetweenLongValidationExpression.builder()
                .greaterValue(Long.MIN_VALUE)
                .lowerValue(greater)
                .build());
    }

    static <T> ValidationExpression<T> moreThanInt(Integer lower) {
        return cast(BetweenIntValidationExpression.builder()
                .greaterValue(lower)
                .lowerValue(Integer.MAX_VALUE)
                .build());
    }

    static <T> ValidationExpression<T> lessThanInt(Integer greater) {
        return cast(BetweenIntValidationExpression.builder()
                .greaterValue(Integer.MIN_VALUE)
                .lowerValue(greater)
                .build());
    }

    static <T> ValidationExpression<T> betweenDouble(Double lower, Double greater) {
        return cast(BetweenDoubleValidationExpression.builder()
                .greaterValue(greater)
                .lowerValue(lower)
                .build());
    }
}
