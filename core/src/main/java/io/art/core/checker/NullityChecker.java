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

package io.art.core.checker;

import lombok.experimental.*;
import static java.util.Objects.*;
import java.util.function.*;

@UtilityClass
public class NullityChecker {
    public static <T> T orElse(T value, T orElse) {
        return isNull(value) ? orElse : value;
    }

    public static <T> T orThrow(T value, Supplier<? extends RuntimeException> exceptionFactory) {
        if (isNull(value)) {
            throw exceptionFactory.get();
        }
        return value;
    }

    public static <T> T orThrow(T value, RuntimeException exception) {
        if (isNull(value)) {
            throw exception;
        }
        return value;
    }

    public static <T> T orElse(T value, Supplier<T> orElse) {
        return isNull(value) ? orElse.get() : value;
    }

    public static <T> void apply(T value, Consumer<T> consumer) {
        if (nonNull(value)) {
            consumer.accept(value);
        }
    }

    public static <T, R> R let(T value, Function<T, R> action) {
        return nonNull(value) ? action.apply(value) : null;
    }

    public static <T, R> R orNull(T value, Predicate<T> condition, Function<T, R> action) {
        return condition.test(value) ? action.apply(value) : null;
    }

    public static <T, R> R let(T value, Function<T, R> action, Supplier<R> orElse) {
        return nonNull(value) ? action.apply(value) : orElse.get();
    }

    public static <T, R> R let(T value, Function<T, R> action, R orElse) {
        return nonNull(value) ? action.apply(value) : orElse;
    }
}
