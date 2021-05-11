/*
 * ART
 *
 * Copyright 2019-2021 ART
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
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.factory.MapFactory.*;
import static java.util.Objects.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

@RequiredArgsConstructor
public class PeriodicExecutor {
    private final Map<String, ForkJoinTask<?>> executingTasks = concurrentMap();
    private final Map<String, ForkJoinTask<?>> cancelledTasks = concurrentMap();
    private final DeferredExecutor deferredExecutor;

    public <T> void submit(PeriodicCallableTask<? extends T> task) {
        ForkJoinTask<? extends T> deferred = cast(executingTasks.get(task.getDelegate().getId()));
        if (nonNull(deferred)) return;
        submitTask(task);
    }

    public void execute(PeriodicRunnableTask task) {
        ForkJoinTask<?> deferred = cast(executingTasks.get(task.getDelegate().getId()));
        if (nonNull(deferred)) return;
        executeTask(task);
    }

    public boolean hasTask(String id) {
        return executingTasks.containsKey(id);
    }

    public boolean cancelTask(String taskId) {
        ForkJoinTask<?> current = executingTasks.remove(taskId);
        if (isNull(current)) return true;
        apply(cancelledTasks.put(taskId, current), task -> task.cancel(false));
        return current.cancel(false);
    }

    public void shutdown() {
        deferredExecutor.shutdown();
    }

    private void executeTask(PeriodicRunnableTask task) {
        String id = task.getDelegate().getId();
        Consumer<LocalDateTime> repeat = time -> repeat(task, time);
        Supplier<Boolean> validate = () -> validate(id);
        RepeatableRunnable runnable = new RepeatableRunnable(validate, repeat, task);
        executingTasks.put(id, deferredExecutor.execute(runnable, task.getStartTime()));
    }

    private <T> void submitTask(PeriodicCallableTask<? extends T> task) {
        String id = task.getDelegate().getId();
        Consumer<LocalDateTime> repeat = time -> repeat(task, time);
        Supplier<Boolean> validate = () -> validate(id);
        RepeatableCallable<? extends T> callable = new RepeatableCallable<>(validate, repeat, task);
        executingTasks.put(id, deferredExecutor.submit(callable, task.getStartTime()));
    }

    private boolean validate(String id) {
        ForkJoinTask<?> task;
        return isNull(task = cancelledTasks.remove(id)) || !task.isCancelled();
    }

    private void repeat(PeriodicRunnableTask task, LocalDateTime time) {
        if (!validate(task.getDelegate().getId())) return;
        executeTask(task.toBuilder().startTime(time.plus(task.getPeriod())).build());
    }

    private void repeat(PeriodicCallableTask<?> task, LocalDateTime time) {
        if (!validate(task.getDelegate().getId())) return;
        submitTask(task.toBuilder().startTime(time.plus(task.getPeriod())).build());
    }
}
