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

package io.art.scheduler.manager;

import io.art.scheduler.model.*;
import lombok.experimental.*;
import static io.art.scheduler.module.SchedulerModule.*;
import java.time.*;
import java.util.Optional;
import java.util.concurrent.*;

@UtilityClass
public class SchedulersManager {
    public static <EventResultType> Future<? extends EventResultType> asynchronous(Callable<? extends EventResultType> eventTask, LocalDateTime triggerTime) {
        return deferredExecutor().submit(eventTask, triggerTime);
    }

    public static <EventResultType> Future<? extends EventResultType> asynchronous(Callable<? extends EventResultType> eventTask) {
        return deferredExecutor().submit(eventTask);
    }

    public static Future<?> asynchronous(Runnable task, LocalDateTime triggerTime) {
        return deferredExecutor().execute(task, triggerTime);
    }

    public static Future<?> asynchronous(Runnable task) {
        return deferredExecutor().execute(task);
    }

    public static <EventResultType> Future<? extends EventResultType> asynchronousPeriod(CallableTask<? extends EventResultType> eventTask,
                                                                                         LocalDateTime startTime, Duration duration) {
        return periodicExecutor().submitPeriodic(eventTask, startTime, duration);
    }

    public static <EventResultType> Future<? extends EventResultType> asynchronousPeriod(CallableTask<? extends EventResultType> eventTask,
                                                                                  Duration duration) {
        return periodicExecutor().submitPeriodic(eventTask, duration);
    }

    public static Future<?> asynchronousPeriod(RunnableTask task, LocalDateTime triggerTime, Duration duration) {
        return periodicExecutor().executePeriodic(task, triggerTime, duration);
    }

    public static Future<?> asynchronousPeriod(RunnableTask task, Duration duration) {
        return periodicExecutor().executePeriodic(task, duration);
    }

    public static boolean cancelPeriodicTask(String taskId) {
        return periodicExecutor().cancelPeriodicTask(taskId);
    }

    public static Optional<Future<?>> removePeriodicTask(String taskId) {
        return periodicExecutor().removePeriodicTask(taskId);
    }

    public static void clearDeferred() {
        deferredExecutor().clear();
    }

    public static void clearPeriodic() {
        periodicExecutor().clear();
    }
}
