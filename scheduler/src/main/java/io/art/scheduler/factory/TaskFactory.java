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

package io.art.scheduler.factory;

import io.art.core.callable.*;
import io.art.core.runnable.*;
import io.art.scheduler.executor.deferred.*;
import io.art.scheduler.model.*;
import lombok.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.LoggingModule.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.*;
import static java.util.UUID.*;
import java.util.function.*;

@UtilityClass
public class TaskFactory {
    @Getter(lazy = true)
    private final static Logger logger = logger(DeferredExecutor.class);

    public static RunnableTask uniqueRunnableTask(ExceptionRunnable runnable) {
        return new RunnableTask(randomUUID().toString(), taskId -> ignoreException(runnable, TaskFactory::logError));
    }

    public static RunnableTask commonRunnableTask(ExceptionRunnable runnable) {
        return new RunnableTask(COMMON_TASK, taskId -> ignoreException(runnable, TaskFactory::logError));
    }

    public static RunnableTask runnableTask(String id, ExceptionRunnable runnable) {
        return new RunnableTask(id, taskId -> ignoreException(runnable, TaskFactory::logError));
    }

    public static <T> CallableTask<T> uniqueCallableTask(ExceptionCallable<T> callable) {
        return new CallableTask<>(randomUUID().toString(), taskId -> ignoreException(callable, TaskFactory::logError));
    }

    public static <T> RunnableTask commonCallableTask(ExceptionCallable<T> callable) {
        return new RunnableTask(COMMON_TASK, taskId -> ignoreException(callable, TaskFactory::logError));
    }

    public static <T> RunnableTask callableTask(String id, ExceptionCallable<T> callable) {
        return new RunnableTask(id, taskId -> ignoreException(callable, TaskFactory::logError));
    }

    public static RunnableTask uniqueRunnableTask(Consumer<String> consumer) {
        return new RunnableTask(randomUUID().toString(), consumer);
    }

    public static RunnableTask commonRunnableTask(Consumer<String> consumer) {
        return new RunnableTask(COMMON_TASK, consumer);
    }

    public static RunnableTask runnableTask(String id, Consumer<String> consumer) {
        return new RunnableTask(id, consumer);
    }

    public static <T> CallableTask<T> uniqueCallableTask(Function<String, T> function) {
        return new CallableTask<>(randomUUID().toString(), function);
    }

    public static <T> CallableTask<T> commonCallableTask(Function<String, T> function) {
        return new CallableTask<>(COMMON_TASK, function);
    }

    public static <T> CallableTask<T> callableTask(String id, Function<String, T> function) {
        return new CallableTask<>(id, function);
    }

    private static void logError(Throwable error) {
        getLogger().error(error.getMessage(), error);
    }
}
