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

package ru.art.task.deferred.executor;


import lombok.RequiredArgsConstructor;
import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.factory.CollectionsFactory.concurrentHashMap;
import static ru.art.logging.LoggingModule.loggingModule;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class PeriodicExecutor {
    private final Map<String, Future<?>> executingTasks = concurrentHashMap();
    private final DeferredExecutor deferredExecutor;

    public <EventResultType> Future<? extends EventResultType> submitPeriodic(IdentifiedCallable<EventResultType> task, LocalDateTime startTime, Duration duration) {
        Future<?> future = executingTasks.get(task.getId());
        if (nonNull(future)) {
            return cast(future);
        }
        return submit(task, startTime, duration);
    }

    public <EventResultType> Future<? extends EventResultType> submitPeriodic(IdentifiedCallable<EventResultType> task, Duration duration) {
        Future<?> future = executingTasks.get(task.getId());
        if (nonNull(future)) {
            return cast(future);
        }
        return submit(task, duration);
    }

    public Future<?> executePeriodic(IdentifiedRunnable task, LocalDateTime startTime, Duration duration) {
        Future<?> future = executingTasks.get(task.getId());
        if (nonNull(future)) {
            return cast(future);
        }
        return execute(task, startTime, duration);
    }

    public Future<?> executePeriodic(IdentifiedRunnable task, Duration duration) {
        Future<?> future = executingTasks.get(task.getId());
        if (nonNull(future)) {
            return cast(future);
        }
        return execute(task, duration);
    }

    public boolean cancelPeriodicTask(String taskId) {
        return removePeriodicTask(taskId).cancel(true);
    }

    public Future<?> removePeriodicTask(String taskId) {
        return executingTasks.remove(taskId);
    }

    public void clear() {
        executingTasks.clear();
        deferredExecutor.clear();
    }

    private <EventResultType> Future<? extends EventResultType> submit(IdentifiedCallable<EventResultType> task, LocalDateTime startTime, Duration duration) {
        NotifiedCallable<EventResultType> eventTask = new NotifiedCallable<>(task.getCallable(), (notification) -> submitAgain(task, duration));
        Future<? extends EventResultType> future = deferredExecutor.submit(eventTask, startTime);
        executingTasks.put(task.getId(), future);
        return future;
    }

    private <EventResultType> Future<? extends EventResultType> submit(IdentifiedCallable<EventResultType> task, Duration duration) {
        NotifiedCallable<EventResultType> eventTask = new NotifiedCallable<>(task.getCallable(), (notification) -> submitAgain(task, duration));
        Future<? extends EventResultType> future = deferredExecutor.submit(eventTask);
        executingTasks.put(task.getId(), future);
        return future;
    }

    private Future<?> execute(IdentifiedRunnable task, LocalDateTime startTime, Duration duration) {
        Future<?> future = deferredExecutor.execute(new NotifiedRunnable(task.getRunnable(), () -> executeAgain(task, duration)), startTime);
        executingTasks.put(task.getId(), future);
        return future;
    }

    private Future<?> execute(IdentifiedRunnable task, Duration duration) {
        Future<?> future = deferredExecutor.execute(new NotifiedRunnable(task.getRunnable(), () -> executeAgain(task, duration)));
        executingTasks.put(task.getId(), future);
        return future;
    }


    private void submitAgain(IdentifiedCallable<?> eventTask, Duration duration) {
        if (executingTasks.containsKey(eventTask.getId())) {
            submit(eventTask, now().plus(duration), duration);
        }
    }

    private void executeAgain(IdentifiedRunnable task, Duration duration) {
        if (executingTasks.containsKey(task.getId())) {
            execute(task, now().plus(duration), duration);
        }
    }
}