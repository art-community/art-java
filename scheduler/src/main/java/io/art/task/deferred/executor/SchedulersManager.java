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

import lombok.experimental.*;
import static io.art.task.deferred.executor.SchedulerModule.*;
import java.time.*;
import java.util.Optional;
import java.util.concurrent.*;

@UtilityClass
public class SchedulersManager {
    public static <EventResultType> Future<? extends EventResultType> asynchronous(Callable<? extends EventResultType> eventTask, LocalDateTime triggerTime) {
        return schedulerModule().getDeferredExecutor().submit(eventTask, triggerTime);
    }

    public static <EventResultType> Future<? extends EventResultType> asynchronous(Callable<? extends EventResultType> eventTask) {
        return schedulerModule().getDeferredExecutor().submit(eventTask);
    }

    public static Future<?> asynchronous(Runnable task, LocalDateTime triggerTime) {
        return schedulerModule().getDeferredExecutor().execute(task, triggerTime);
    }

    public static Future<?> asynchronous(Runnable task) {
        return schedulerModule().getDeferredExecutor().execute(task);
    }

    public static <EventResultType> Future<? extends EventResultType> asynchronousPeriod(CallableTask<? extends EventResultType> eventTask,
                                                                                  LocalDateTime startTime, Duration duration) {
        return schedulerModule().getPeriodicExecutor().submitPeriodic(eventTask, startTime, duration);
    }

    public static <EventResultType> Future<? extends EventResultType> asynchronousPeriod(CallableTask<? extends EventResultType> eventTask,
                                                                                  Duration duration) {
        return schedulerModule().getPeriodicExecutor().submitPeriodic(eventTask, duration);
    }

    public static Future<?> asynchronousPeriod(RunnableTask task, LocalDateTime triggerTime, Duration duration) {
        return schedulerModule().getPeriodicExecutor().executePeriodic(task, triggerTime, duration);
    }

    public static Future<?> asynchronousPeriod(RunnableTask task, Duration duration) {
        return schedulerModule().getPeriodicExecutor().executePeriodic(task, duration);
    }

    public static boolean cancelPeriodicTask(String taskId) {
        return schedulerModule().getPeriodicExecutor().cancelPeriodicTask(taskId);
    }

    public static Optional<Future<?>> removePeriodicTask(String taskId) {
        return schedulerModule().getPeriodicExecutor().removePeriodicTask(taskId);
    }

    public static void clearDeferred() {
        schedulerModule().getDeferredExecutor().clear();
    }

    public static void clearPeriodic() {
        schedulerModule().getPeriodicExecutor().clear();
    }
}
