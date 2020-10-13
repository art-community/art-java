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

import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import java.util.*;
import java.util.function.*;

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

    public static <T> ValidationExpression<T> notEmptyString(Function<NotEmptyStringValidationExpression, String> factory) {
        return cast(new NotEmptyStringValidationExpression(factory));
    }

    public static <T> ValidationExpression<T> notEmptyCollection() {
        return cast(new NotEmptyCollectionValidationExpression());
    }

    public static <T> ValidationExpression<T> notEmptyCollection(Function<NotEmptyCollectionValidationExpression, String> factory) {
        return cast(new NotEmptyCollectionValidationExpression(factory));
    }

    public static <T> ValidationExpression<T> notEmptyMap() {
        return cast(new NotEmptyMapValidationExpression());
    }

    public static <T> ValidationExpression<T> notEmptyMap(Function<NotEmptyMapValidationExpression, String> factory) {
        return cast(new NotEmptyMapValidationExpression(factory));
    }

    public static <T> ValidationExpression<T> notEqualsOther(Object other) {
        return cast(new NotEqualsValidationExpression(other));
    }

    public static <T> ValidationExpression<T> notEqualsOther(Object other, Function<NotEqualsValidationExpression, String> factory) {
        return cast(new NotEqualsValidationExpression(other, factory));
    }

    public static <T> ValidationExpression<T> equalsOther(Object other) {
        return cast(new EqualsValidationExpression(other));
    }

    public static <T> ValidationExpression<T> equalsOther(Object other, Function<EqualsValidationExpression, String> factory) {
        return cast(new EqualsValidationExpression(other, factory));
    }

    public static <T> ValidationExpression<T> containsOther(List<?> list) {
        return cast(new ContainsValidationExpression(list));
    }

    public static <T> ValidationExpression<T> containsOther(List<?> list, Function<ContainsValidationExpression, String> factory) {
        return cast(new ContainsValidationExpression(list, factory));
    }

    public static <T> ValidationExpression<T> betweenInt(Integer lower, Integer greater) {
        return cast(new BetweenIntValidationExpression(lower, greater));
    }

    public static <T> ValidationExpression<T> betweenInt(Integer lower, Integer greater, Function<BetweenIntValidationExpression, String> factory) {
        return cast(new BetweenIntValidationExpression(lower, greater, factory));
    }

    public static <T> ValidationExpression<T> moreThanInt(Integer value) {
        return cast(new BetweenIntValidationExpression(Integer.MAX_VALUE, value));
    }

    public static <T> ValidationExpression<T> moreThanInt(Integer value, Function<BetweenIntValidationExpression, String> factory) {
        return cast(new BetweenIntValidationExpression(Integer.MAX_VALUE, value, factory));
    }

    public static <T> ValidationExpression<T> lessThanInt(Integer value) {
        return cast(new BetweenIntValidationExpression(value, Integer.MIN_VALUE));
    }

    public static <T> ValidationExpression<T> lessThanInt(Integer value, Function<BetweenIntValidationExpression, String> factory) {
        return cast(new BetweenIntValidationExpression(value, Integer.MIN_VALUE, factory));
    }

    public static <T> ValidationExpression<T> betweenLong(Long lower, Long greater) {
        return cast(new BetweenLongValidationExpression(lower, greater));
    }

    public static <T> ValidationExpression<T> betweenLong(Long lower, Long greater, Function<BetweenLongValidationExpression, String> factory) {
        return cast(new BetweenLongValidationExpression(lower, greater, factory));
    }

    public static <T> ValidationExpression<T> moreThanLong(Long value) {
        return cast(new BetweenLongValidationExpression(Long.MAX_VALUE, value));
    }

    public static <T> ValidationExpression<T> moreThanLong(Long value, Function<BetweenLongValidationExpression, String> factory) {
        return cast(new BetweenLongValidationExpression(Long.MAX_VALUE, value, factory));
    }

    public static <T> ValidationExpression<T> lessThanLong(Long value) {
        return cast(new BetweenLongValidationExpression(value, Long.MIN_VALUE));
    }

    public static <T> ValidationExpression<T> lessThanLong(Long value, Function<BetweenLongValidationExpression, String> factory) {
        return cast(new BetweenLongValidationExpression(value, Long.MIN_VALUE, factory));
    }

    public static <T> ValidationExpression<T> betweenDouble(Double lower, Double greater) {
        return cast(new BetweenDoubleValidationExpression(lower, greater));
    }

    public static <T> ValidationExpression<T> betweenDouble(Double lower, Double greater, Function<BetweenDoubleValidationExpression, String> factory) {
        return cast(new BetweenDoubleValidationExpression(lower, greater, factory));
    }

    public static <T> ValidationExpression<T> moreThanDouble(Double value) {
        return cast(new BetweenDoubleValidationExpression(Double.MAX_VALUE, value));
    }

    public static <T> ValidationExpression<T> moreThanDouble(Double value, Function<BetweenDoubleValidationExpression, String> factory) {
        return cast(new BetweenDoubleValidationExpression(Double.MAX_VALUE, value, factory));
    }

    public static <T> ValidationExpression<T> lessThanDouble(Double value) {
        return cast(new BetweenDoubleValidationExpression(value, Double.MIN_VALUE));
    }

    public static <T> ValidationExpression<T> lessThanDouble(Double value, Function<BetweenDoubleValidationExpression, String> factory) {
        return cast(new BetweenDoubleValidationExpression(value, Double.MIN_VALUE, factory));
    }
}
