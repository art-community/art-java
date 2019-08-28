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

package ru.art.task.deferred.executor;

import static ru.art.task.deferred.executor.SchedulerModule.*;
import java.time.*;
import java.util.concurrent.*;

public interface SchedulerModuleActions {

    static <EventResultType> Future<? extends EventResultType> asynchronous(Callable<? extends EventResultType> eventTask, LocalDateTime triggerTime) {
        return schedulerModule().getDeferredExecutor().submit(eventTask, triggerTime);
    }

    static <EventResultType> Future<? extends EventResultType> asynchronous(Callable<? extends EventResultType> eventTask) {
        return schedulerModule().getDeferredExecutor().submit(eventTask);
    }

    static Future<?> asynchronous(Runnable task, LocalDateTime triggerTime) {
        return schedulerModule().getDeferredExecutor().execute(task, triggerTime);
    }

    static Future<?> asynchronous(Runnable task) {
        return schedulerModule().getDeferredExecutor().execute(task);
    }

    static <EventResultType> Future<? extends EventResultType> asynchronousPeriod(IdentifiedCallable<? extends EventResultType> eventTask,
                                                                                  LocalDateTime startTime, Duration duration) {
        return schedulerModule().getPeriodicExecutor().submitPeriodic(eventTask, startTime, duration);
    }

    static <EventResultType> Future<? extends EventResultType> asynchronousPeriod(IdentifiedCallable<? extends EventResultType> eventTask,
                                                                                  Duration duration) {
        return schedulerModule().getPeriodicExecutor().submitPeriodic(eventTask, duration);
    }

    static Future<?> asynchronousPeriod(IdentifiedRunnable task, LocalDateTime triggerTime, Duration duration) {
        return schedulerModule().getPeriodicExecutor().executePeriodic(task, triggerTime, duration);
    }

    static Future<?> asynchronousPeriod(IdentifiedRunnable task, Duration duration) {
        return schedulerModule().getPeriodicExecutor().executePeriodic(task, duration);
    }

    static boolean cancelPeriodicTask(String taskId) {
        return schedulerModule().getPeriodicExecutor().cancelPeriodicTask(taskId);
    }

    static Future<?> removePeriodicTask(String taskId) {
        return schedulerModule().getPeriodicExecutor().removePeriodicTask(taskId);
    }

    static void clearDeferred() {
        schedulerModule().getDeferredExecutor().clear();
    }

    static void clearPeriodic() {
        schedulerModule().getPeriodicExecutor().clear();
    }
}
