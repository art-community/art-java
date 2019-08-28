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

package ru.art.core.wrapper;

import lombok.*;
import lombok.experimental.*;
import ru.art.core.callable.*;
import ru.art.core.exception.*;
import ru.art.core.factory.*;
import ru.art.core.runnable.*;
import static java.util.Objects.*;
import static ru.art.core.constants.ExceptionMessages.*;
import java.util.concurrent.*;
import java.util.function.*;

@UtilityClass
public class ExceptionWrapper {
    @SneakyThrows
    public static void wrapException(Runnable action) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        action.run();
    }

    @SneakyThrows
    public static <T> T wrapException(Callable<T> action) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        return action.call();
    }

    public static void ignoreException(ExceptionRunnable action) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        try {
            action.run();
        } catch (Throwable e) {
            //ignore
        }
    }

    public static void ignoreException(ExceptionRunnable action, Consumer<Throwable> onException) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        try {
            action.run();
        } catch (Throwable e) {
            onException.accept(e);
        }
    }

    public static <T> T ignoreException(ExceptionCallable<T> action, Function<Throwable, T> onException) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        try {
            return action.call();
        } catch (Throwable e) {
            return onException.apply(e);
        }
    }

    public static void wrapException(Runnable action, ExceptionFactory<?> exceptionFactory) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        if (isNull(exceptionFactory)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_FACTORY_IS_NULL);
        try {
            action.run();
        } catch (Throwable e) {
            throw exceptionFactory.create(e);
        }
    }

    public static <T> T wrapException(Callable<T> action, ExceptionFactory<?> exceptionFactory) {
        try {
            return action.call();
        } catch (Throwable e) {
            throw exceptionFactory.create(e);
        }
    }
}
