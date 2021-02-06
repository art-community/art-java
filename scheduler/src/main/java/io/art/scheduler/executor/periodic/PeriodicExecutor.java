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

    public <T> Future<? extends T> submit(PeriodicCallableTask<? extends T> task) {
        Future<? extends T> future = cast(executingTasks.get(task.getDelegate().getId()));
        if (nonNull(future)) return cast(future);
        Supplier<Boolean> hasTask = hasTask(task.getDelegate().getId());
        Consumer<LocalDateTime> repeat = time -> submit(task.toBuilder().startTime(time).build());
        RepeatableCallable<? extends T> callable = new RepeatableCallable<>(hasTask, repeat, task);
        future = deferredExecutor.submit(callable, task.getStartTime());
        executingTasks.put(task.getDelegate().getId(), future);
        return future;
    }

    public Future<?> execute(PeriodicRunnableTask task) {
        Future<?> future = cast(executingTasks.get(task.getDelegate().getId()));
        if (nonNull(future)) return cast(future);
        Supplier<Boolean> hasTask = hasTask(task.getDelegate().getId());
        Consumer<LocalDateTime> repeat = time -> execute(task.toBuilder().startTime(time).build());
        RepeatableRunnable runnable = new RepeatableRunnable(hasTask, repeat, task);
        future = deferredExecutor.execute(runnable, task.getStartTime());
        executingTasks.put(task.getDelegate().getId(), future);
        return future;
    }

    public boolean cancelTask(String taskId) {
        return ofNullable(executingTasks.remove(taskId))
                .map(future -> future.cancel(false))
                .orElse(false);
    }

    public Supplier<Boolean> hasTask(String id) {
        return () -> executingTasks.containsKey(id);
    }

    public void shutdown() {
        deferredExecutor.shutdown();
    }
}
