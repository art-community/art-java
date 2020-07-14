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

package io.art.task.deferred.executor;

import static java.util.UUID.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.task.deferred.executor.SchedulerModuleConstants.*;
import java.util.concurrent.*;
import java.util.function.*;

public interface TaskFactory {
    static RunnableTask uniqueRunnableTask(Runnable runnable) {
        return new RunnableTask(randomUUID().toString(), taskId -> runnable.run());
    }

    static RunnableTask commonRunnableTask(Runnable runnable) {
        return new RunnableTask(COMMON_TASK, taskId -> runnable.run());
    }

    static RunnableTask runnableTask(String id, Runnable runnable) {
        return new RunnableTask(id, taskId -> runnable.run());
    }

    static <T> CallableTask<T> uniqueCallableTask(Callable<T> callable) {
        return new CallableTask<>(randomUUID().toString(), taskId -> wrapException(callable));
    }

    static <T> RunnableTask commonCallableTask(Callable<T> callable) {
        return new RunnableTask(COMMON_TASK, taskId -> wrapException(callable));
    }

    static <T> RunnableTask callableTask(String id, Callable<T> callable) {
        return new RunnableTask(id, taskId -> wrapException(callable));
    }

    static RunnableTask uniqueRunnableTask(Consumer<String> consumer) {
        return new RunnableTask(randomUUID().toString(), consumer);
    }

    static RunnableTask commonRunnableTask(Consumer<String> consumer) {
        return new RunnableTask(COMMON_TASK, consumer);
    }

    static RunnableTask runnableTask(String id, Consumer<String> consumer) {
        return new RunnableTask(id, consumer);
    }

    static <T> CallableTask<T> uniqueCallableTask(Function<String, T> function) {
        return new CallableTask<>(randomUUID().toString(), function);
    }

    static <T> CallableTask commonCallableTask(Function<String, T> function) {
        return new CallableTask<>(COMMON_TASK, function);
    }

    static <T> CallableTask callableTask(String id, Function<String, T> function) {
        return new CallableTask<>(id, function);
    }
}
