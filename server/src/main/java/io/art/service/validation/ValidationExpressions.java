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

import lombok.experimental.UtilityClass;

import static io.art.core.caster.Caster.*;
import java.util.*;

@UtilityClass
public class ValidationExpressions {
    public static <T> ValidationExpression<T> notNull() {
        return cast(new NotNullValidationExpression());
    }

    public static <T> ValidationExpression<T> notNull(String pattern) {
        return cast(new NotNullValidationExpression(pattern));
    }

    public static <T> ValidationExpression<T> notEmptyString() {
        return cast(new NotEmptyStringValidationExpression());
    }

    public static <T> ValidationExpression<T> notEmptyString(String pattern) {
        return cast(new NotEmptyStringValidationExpression(pattern));
    }

    public static <T> ValidationExpression<T> notEmptyCollection() {
        return cast(new NotEmptyCollectionValidationExpression());
    }

    public static <T> ValidationExpression<T> notEmptyCollection(String pattern) {
        return cast(new NotEmptyCollectionValidationExpression(pattern));
    }

    public static <T> ValidationExpression<T> notEmptyMap() {
        return cast(new NotEmptyMapValidationExpression());
    }

    public static <T> ValidationExpression<T> notEmptyMap(String pattern) {
        return cast(new NotEmptyMapValidationExpression(pattern));
    }

    public static <T> ValidationExpression<T> notEqualsOther(Object other) {
        return cast(new NotEqualsValidationExpression(other));
    }

    public static <T> ValidationExpression<T> notEqualsOther(Object other, String pattern) {
        return cast(new NotEqualsValidationExpression(other, pattern));
    }

    public static <T> ValidationExpression<T> equalsOther(Object other) {
        return cast(new EqualsValidationExpression(other));
    }

    public static <T> ValidationExpression<T> equalsOther(Object other, String pattern) {
        return cast(new EqualsValidationExpression(other, pattern));
    }

    public static <T> ValidationExpression<T> containsOther(List<?> list) {
        return cast(new ContainsValidationExpression(list));
    }

    public static <T> ValidationExpression<T> containsOther(List<?> list, String pattern) {
        return cast(new ContainsValidationExpression(list, pattern));
    }

    public static <T> ValidationExpression<T> betweenInt(Integer lower, Integer greater) {
        return cast(new BetweenIntValidationExpression(lower, greater));
    }

    public static <T> ValidationExpression<T> betweenInt(Integer lower, Integer greater, String pattern) {
        return cast(new BetweenIntValidationExpression(lower, greater, pattern));
    }

    public static <T> ValidationExpression<T> moreThanInt(Integer value) {
        return cast(new BetweenIntValidationExpression(Integer.MAX_VALUE, value));
    }

    public static <T> ValidationExpression<T> moreThanInt(Integer value, String pattern) {
        return cast(new BetweenIntValidationExpression(Integer.MAX_VALUE, value, pattern));
    }

    public static <T> ValidationExpression<T> lessThanInt(Integer value) {
        return cast(new BetweenIntValidationExpression(value, Integer.MIN_VALUE));
    }

    public static <T> ValidationExpression<T> lessThanInt(Integer value, String pattern) {
        return cast(new BetweenIntValidationExpression(value, Integer.MIN_VALUE, pattern));
    }

    public static <T> ValidationExpression<T> betweenLong(Long lower, Long greater) {
        return cast(new BetweenLongValidationExpression(lower, greater));
    }

    public static <T> ValidationExpression<T> betweenLong(Long lower, Long greater, String pattern) {
        return cast(new BetweenLongValidationExpression(lower, greater, pattern));
    }

    public static <T> ValidationExpression<T> moreThanLong(Long value) {
        return cast(new BetweenLongValidationExpression(Long.MAX_VALUE, value));
    }

    public static <T> ValidationExpression<T> moreThanLong(Long value, String pattern) {
        return cast(new BetweenLongValidationExpression(Long.MAX_VALUE, value, pattern));
    }

    public static <T> ValidationExpression<T> lessThanLong(Long value) {
        return cast(new BetweenLongValidationExpression(value, Long.MIN_VALUE));
    }

    public static <T> ValidationExpression<T> lessThanLong(Long value, String pattern) {
        return cast(new BetweenLongValidationExpression(value, Long.MIN_VALUE, pattern));
    }

    public static <T> ValidationExpression<T> betweenDouble(Double lower, Double greater) {
        return cast(new BetweenDoubleValidationExpression(lower, greater));
    }

    public static <T> ValidationExpression<T> betweenDouble(Double lower, Double greater, String pattern) {
        return cast(new BetweenDoubleValidationExpression(lower, greater, pattern));
    }

    public static <T> ValidationExpression<T> moreThanDouble(Double value) {
        return cast(new BetweenDoubleValidationExpression(Double.MAX_VALUE, value));
    }

    public static <T> ValidationExpression<T> moreThanDouble(Double value, String pattern) {
        return cast(new BetweenDoubleValidationExpression(Double.MAX_VALUE, value, pattern));
    }

    public static <T> ValidationExpression<T> lessThanDouble(Double value) {
        return cast(new BetweenDoubleValidationExpression(value, Double.MIN_VALUE));
    }

    public static <T> ValidationExpression<T> lessThanDouble(Double value, String pattern) {
        return cast(new BetweenDoubleValidationExpression(value, Double.MIN_VALUE, pattern));
    }
}
