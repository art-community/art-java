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

package io.art.scheduler.executor.periodic;


import io.art.scheduler.executor.deferred.*;
import io.art.scheduler.model.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static java.time.Duration.*;
import static java.time.LocalDateTime.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

@RequiredArgsConstructor
public class PeriodicExecutor {
    private final Map<String, Future<?>> executingTasks = concurrentMap();
    private final DeferredExecutor deferredExecutor;

    public <T> Future<? extends T> submitPeriodic(CallableTask<T> task, LocalDateTime startTime, long period) {
        Future<?> future = executingTasks.get(task.getId());
        if (nonNull(future)) {
            return cast(future);
        }
        return submit(task, startTime, period);
    }

    public <T> Future<? extends T> submitPeriodic(CallableTask<T> task, long period) {
        Future<?> future = executingTasks.get(task.getId());
        if (nonNull(future)) {
            return cast(future);
        }
        return submit(task, period);
    }

    public Future<?> executePeriodic(RunnableTask task, LocalDateTime startTime, long period) {
        Future<?> future = executingTasks.get(task.getId());
        if (nonNull(future)) {
            return cast(future);
        }
        return execute(task, startTime, period);
    }

    public Future<?> executePeriodic(RunnableTask task, long period) {
        Future<?> future = executingTasks.get(task.getId());
        if (nonNull(future)) {
            return cast(future);
        }
        return execute(task, period);
    }

    public boolean cancelPeriodicTask(String taskId) {
        return ofNullable(executingTasks.remove(taskId)).map(future -> future.cancel(false)).orElse(false);
    }

    public boolean hasPeriodicTask(String taskId) {
        return executingTasks.containsKey(taskId);
    }

    public void shutdown() {
        deferredExecutor.shutdown();
    }

    private <T> Future<? extends T> submit(CallableTask<T> task, LocalDateTime startTime, long periodNanos) {
        final Callable<T> action = () -> task.getCallable().apply(task.getId());
        final Consumer<LocalDateTime> repeat = now -> submitAgain(task, now, periodNanos);
        RepeatableCallable<T> eventTask = new RepeatableCallable<>(action, repeat);
        Future<? extends T> future = deferredExecutor.submit(eventTask, startTime);
        executingTasks.put(task.getId(), future);
        return future;
    }

    private <T> Future<? extends T> submit(CallableTask<T> task, long periodNanos) {
        final Callable<T> action = () -> task.getCallable().apply(task.getId());
        final Consumer<LocalDateTime> repeat = now -> submitAgain(task, now, periodNanos);
        RepeatableCallable<T> eventTask = new RepeatableCallable<>(action, repeat);
        Future<? extends T> future = deferredExecutor.submit(eventTask);
        executingTasks.put(task.getId(), future);
        return future;
    }

    private Future<?> execute(RunnableTask task, LocalDateTime startTime, long periodNanos) {
        final Runnable action = () -> task.getRunnable().accept(task.getId());
        final Consumer<LocalDateTime> repeat = now -> executeAgain(task, now, periodNanos);
        Future<?> future = deferredExecutor.execute(new RepeatableRunnable(action, repeat), startTime);
        executingTasks.put(task.getId(), future);
        return future;
    }

    private Future<?> execute(RunnableTask task, long periodNanos) {
        final Runnable action = () -> task.getRunnable().accept(task.getId());
        final Consumer<LocalDateTime> repeat = now -> executeAgain(task, now, periodNanos);
        Future<?> future = deferredExecutor.execute(new RepeatableRunnable(action, repeat));
        executingTasks.put(task.getId(), future);
        return future;
    }


    private void submitAgain(CallableTask<?> eventTask, LocalDateTime startTime, long periodNanos) {
        if (executingTasks.containsKey(eventTask.getId())) {
            if (periodNanos > 0L) {
                submit(eventTask, now().plus(ofNanos(periodNanos)), periodNanos);
                return;
            }
            submit(eventTask, startTime.plus(ofNanos(-periodNanos)), -periodNanos);
        }
    }

    private void executeAgain(RunnableTask task, LocalDateTime startTime, long periodNanos) {
        if (executingTasks.containsKey(task.getId())) {
            if (periodNanos > 0L) {
                execute(task, now().plus(ofNanos(periodNanos)), periodNanos);
                return;
            }
            execute(task, startTime.plus(ofNanos(-periodNanos)), -periodNanos);
        }
    }
}
