/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.core.wrapper;

import lombok.SneakyThrows;
import ru.art.core.exception.InternalRuntimeException;
import ru.art.core.factory.ExceptionFactory;
import static java.util.Objects.isNull;
import static ru.art.core.constants.ExceptionMessages.EXCEPTION_WRAPPER_ACTION_IS_NULL;
import static ru.art.core.constants.ExceptionMessages.EXCEPTION_WRAPPER_FACTORY_IS_NULL;
import java.util.concurrent.Callable;

public interface ExceptionWrapper {
    @SneakyThrows
    static void wrap(Runnable action) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        action.run();
    }

    @SneakyThrows
    static <T> T wrap(Callable<T> action) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        return action.call();
    }

    static void wrap(Runnable action, ExceptionFactory<?> exceptionFactory) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        if (isNull(exceptionFactory)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_FACTORY_IS_NULL);
        try {
            action.run();
        } catch (Exception e) {
            throw exceptionFactory.create(e);
        }
    }

    static <T> T wrap(Callable<T> action, ExceptionFactory<?> exceptionFactory) {
        try {
            return action.call();
        } catch (Exception e) {
            throw exceptionFactory.create(e);
        }
    }
}
