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

package ru.art.core.extension;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import java.util.concurrent.Callable;
import java.util.function.Function;

public interface ExceptionExtensions {
    static String emptyIfException(Callable<String> operation) {
        if (isNull(operation)) return EMPTY_STRING;
        try {
            return operation.call();
        } catch (Throwable e) {
            return EMPTY_STRING;
        }
    }

    static <T> T nullIfException(Callable<T> operation) {
        if (isNull(operation)) return null;
        try {
            return operation.call();
        } catch (Throwable e) {
            return null;
        }
    }

    static <T> T ifException(Callable<T> operation, T value) {
        requireNonNull(operation);
        try {
            return operation.call();
        } catch (Throwable e) {
            return value;
        }
    }

    static <T> T ifExceptionOrEmpty(Callable<T> operation, T value) {
        requireNonNull(operation);
        try {
            T result = operation.call();
            if (isEmpty(result)) return value;
            return result;
        } catch (Throwable e) {
            return value;
        }
    }

    static <T> T exceptionIfNull(T value, RuntimeException e) {
        if (isNull(value)) throw e;
        return value;
    }

    static <T> T exceptionIfEmpty(T value, RuntimeException e) {
        if (isEmpty(value)) throw e;
        return value;
    }

    static <T> T wrapException(Callable<T> action, Function<Throwable, RuntimeException> exceptionFactory) {
        try {
            return action.call();
        } catch (Throwable e) {
            throw exceptionFactory.apply(e);
        }
    }
}
