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

    static <T> ValidationExpression<T> notNull(String pattern) {
        return cast(new NotNullValidationExpression(pattern));
    }

    static <T> ValidationExpression<T> notEmptyString() {
        return cast(new NotEmptyStringValidationExpression());
    }

    static <T> ValidationExpression<T> notEmptyString(String pattern) {
        return cast(new NotEmptyStringValidationExpression(pattern));
    }

    static <T> ValidationExpression<T> notEmptyCollection() {
        return cast(new NotEmptyCollectionValidationExpression());
    }

    static <T> ValidationExpression<T> notEmptyCollection(String pattern) {
        return cast(new NotEmptyCollectionValidationExpression(pattern));
    }

    static <T> ValidationExpression<T> notEmptyMap() {
        return cast(new NotEmptyMapValidationExpression());
    }

    static <T> ValidationExpression<T> notEmptyMap(String pattern) {
        return cast(new NotEmptyMapValidationExpression(pattern));
    }

    static <T> ValidationExpression<T> notEqualsOther(Object other) {
        return cast(new NotEqualsValidationExpression(other));
    }

    static <T> ValidationExpression<T> notEqualsOther(Object other, String pattern) {
        return cast(new NotEqualsValidationExpression(other, pattern));
    }

    static <T> ValidationExpression<T> equalsOther(Object other) {
        return cast(new EqualsValidationExpression(other));
    }

    static <T> ValidationExpression<T> equalsOther(Object other, String pattern) {
        return cast(new EqualsValidationExpression(other, pattern));
    }

    static <T> ValidationExpression<T> containsOther(List<?> list) {
        return cast(new ContainsValidationExpression(list));
    }

    static <T> ValidationExpression<T> containsOther(List<?> list, String pattern) {
        return cast(new ContainsValidationExpression(list, pattern));
    }

    static <T> ValidationExpression<T> betweenInt(Integer lower, Integer greater) {
        return cast(new BetweenIntValidationExpression(lower, greater));
    }

    static <T> ValidationExpression<T> betweenInt(Integer lower, Integer greater, String pattern) {
        return cast(new BetweenIntValidationExpression(lower, greater, pattern));
    }

    static <T> ValidationExpression<T> moreThanInt(Integer value) {
        return cast(new BetweenIntValidationExpression(Integer.MAX_VALUE, value));
    }

    static <T> ValidationExpression<T> moreThanInt(Integer value, String pattern) {
        return cast(new BetweenIntValidationExpression(Integer.MAX_VALUE, value, pattern));
    }

    static <T> ValidationExpression<T> lessThanInt(Integer value) {
        return cast(new BetweenIntValidationExpression(value, Integer.MIN_VALUE));
    }

    static <T> ValidationExpression<T> lessThanInt(Integer value, String pattern) {
        return cast(new BetweenIntValidationExpression(value, Integer.MIN_VALUE, pattern));
    }

    static <T> ValidationExpression<T> betweenLong(Long lower, Long greater) {
        return cast(new BetweenLongValidationExpression(lower, greater));
    }

    static <T> ValidationExpression<T> betweenLong(Long lower, Long greater, String pattern) {
        return cast(new BetweenLongValidationExpression(lower, greater, pattern));
    }

    static <T> ValidationExpression<T> moreThanLong(Long value) {
        return cast(new BetweenLongValidationExpression(Long.MAX_VALUE, value));
    }

    static <T> ValidationExpression<T> moreThanLong(Long value, String pattern) {
        return cast(new BetweenLongValidationExpression(Long.MAX_VALUE, value, pattern));
    }

    static <T> ValidationExpression<T> lessThanLong(Long value) {
        return cast(new BetweenLongValidationExpression(value, Long.MIN_VALUE));
    }

    static <T> ValidationExpression<T> lessThanLong(Long value, String pattern) {
        return cast(new BetweenLongValidationExpression(value, Long.MIN_VALUE, pattern));
    }

    static <T> ValidationExpression<T> betweenDouble(Double lower, Double greater) {
        return cast(new BetweenDoubleValidationExpression(lower, greater));
    }

    static <T> ValidationExpression<T> betweenDouble(Double lower, Double greater, String pattern) {
        return cast(new BetweenDoubleValidationExpression(lower, greater, pattern));
    }

    static <T> ValidationExpression<T> moreThanDouble(Double value) {
        return cast(new BetweenDoubleValidationExpression(Double.MAX_VALUE, value));
    }

    static <T> ValidationExpression<T> moreThanDouble(Double value, String pattern) {
        return cast(new BetweenDoubleValidationExpression(Double.MAX_VALUE, value, pattern));
    }

    static <T> ValidationExpression<T> lessThanDouble(Double value) {
        return cast(new BetweenDoubleValidationExpression(value, Double.MIN_VALUE));
    }

    static <T> ValidationExpression<T> lessThanDouble(Double value, String pattern) {
        return cast(new BetweenDoubleValidationExpression(value, Double.MIN_VALUE, pattern));
    }
}
