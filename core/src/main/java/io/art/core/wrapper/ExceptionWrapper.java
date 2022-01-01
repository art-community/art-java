/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.core.wrapper;

import io.art.core.callable.*;
import io.art.core.factory.*;
import io.art.core.runnable.*;
import lombok.*;
import lombok.experimental.*;
import java.util.concurrent.*;
import java.util.function.*;

@UtilityClass
public class ExceptionWrapper {

    public static void ignoreException(ExceptionRunnable action) {
        try {
            action.run();
        } catch (Throwable throwable) {
            //ignore
        }
    }

    public static void ignoreException(ExceptionRunnable action, Consumer<Throwable> onException) {
        try {
            action.run();
        } catch (Throwable throwable) {
            onException.accept(throwable);
        }
    }

    public static <T> T ignoreException(ExceptionCallable<T> action, Function<Throwable, T> onException) {
        try {
            return action.call();
        } catch (Throwable throwable) {
            return onException.apply(throwable);
        }
    }

    public static <T> T ignoreException(ExceptionCallable<T> action, Consumer<Throwable> onException) {
        try {
            return action.call();
        } catch (Throwable throwable) {
            onException.accept(throwable);
            return null;
        }
    }

    @SneakyThrows
    public static void wrapExceptionRun(Runnable action) {
        action.run();
    }

    @SneakyThrows
    public static <T> T wrapExceptionCall(Callable<T> action) {
        return action.call();
    }

    public static void wrapExceptionRun(Runnable action, ExceptionFactory<? extends Throwable> exceptionFactory) {
        try {
            action.run();
        } catch (Throwable throwable) {
            throw exceptionFactory.create(throwable);
        }
    }

    public static <T, E extends RuntimeException> T wrapExceptionCall(Callable<T> action, ExceptionFactory<E> exceptionFactory) {
        try {
            return action.call();
        } catch (Throwable throwable) {
            throw exceptionFactory.create(throwable);
        }
    }
}
