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

import io.art.core.callable.*;
import io.art.core.runnable.*;
import io.art.scheduler.model.*;
import lombok.experimental.*;
import static io.art.scheduler.factory.TaskFactory.*;
import static io.art.scheduler.module.SchedulerModule.*;
import java.time.*;
import java.util.concurrent.*;

@UtilityClass
public class SchedulersManager {
    public static <T> Future<? extends T> schedule(Callable<? extends T> eventTask) {
        return deferredExecutor().submit(eventTask);
    }

    public static <T> Future<? extends T> schedule(Callable<? extends T> eventTask, LocalDateTime triggerTime) {
        return deferredExecutor().submit(eventTask, triggerTime);
    }

    public static Future<?> schedule(Runnable task) {
        return deferredExecutor().execute(task);
    }

    public static Future<?> schedule(Runnable task, LocalDateTime triggerTime) {
        return deferredExecutor().execute(task, triggerTime);
    }


    public static <T> Future<? extends T> scheduleFixedRate(ExceptionCallable<? extends T> eventTask, Duration period) {
        return scheduleFixedRate(task(eventTask), period);
    }

    public static <T> Future<? extends T> scheduleFixedRate(ExceptionCallable<? extends T> eventTask, LocalDateTime startTime, Duration period) {
        return scheduleFixedRate(task(eventTask), startTime, period);
    }

    public static Future<?> scheduleFixedRate(ExceptionRunnable task, Duration duration) {
        return scheduleFixedRate(task(task), duration);
    }

    public static Future<?> scheduleFixedRate(ExceptionRunnable task, LocalDateTime triggerTime, Duration duration) {
        return scheduleFixedRate(task(task), triggerTime, duration);
    }


    public static <T> Future<? extends T> scheduleFixedRate(CallableTask<? extends T> eventTask, Duration period) {
        return periodicExecutor().submitPeriodic(eventTask, period.toNanos());
    }

    public static <T> Future<? extends T> scheduleFixedRate(CallableTask<? extends T> eventTask, LocalDateTime startTime, Duration period) {
        return periodicExecutor().submitPeriodic(eventTask, startTime, period.toNanos());
    }

    public static Future<?> scheduleFixedRate(RunnableTask task, Duration duration) {
        return periodicExecutor().executePeriodic(task, duration.toNanos());
    }

    public static Future<?> scheduleFixedRate(RunnableTask task, LocalDateTime triggerTime, Duration duration) {
        return periodicExecutor().executePeriodic(task, triggerTime, duration.toNanos());
    }


    public static Future<?> scheduleDelayed(ExceptionRunnable task, Duration delay) {
        return scheduleDelayed(task(task), delay);
    }

    public static Future<?> scheduleDelayed(ExceptionRunnable task, LocalDateTime triggerTime, Duration delay) {
        return scheduleDelayed(task(task), triggerTime, delay);
    }

    public static <T> Future<? extends T> scheduleDelayed(ExceptionCallable<? extends T> eventTask, Duration delay) {
        return scheduleDelayed(task(eventTask), delay);
    }

    public static <T> Future<? extends T> scheduleDelayed(ExceptionCallable<? extends T> eventTask, LocalDateTime startTime, Duration delay) {
        return scheduleDelayed(task(eventTask), startTime, delay);
    }


    public static Future<?> scheduleDelayed(RunnableTask task, Duration delay) {
        return periodicExecutor().executePeriodic(task, -delay.toNanos());
    }

    public static Future<?> scheduleDelayed(RunnableTask task, LocalDateTime triggerTime, Duration delay) {
        return periodicExecutor().executePeriodic(task, triggerTime, -delay.toNanos());
    }

    public static <T> Future<? extends T> scheduleDelayed(CallableTask<? extends T> eventTask, Duration delay) {
        return periodicExecutor().submitPeriodic(eventTask, -delay.toNanos());
    }

    public static <T> Future<? extends T> scheduleDelayed(CallableTask<? extends T> eventTask, LocalDateTime startTime, Duration delay) {
        return periodicExecutor().submitPeriodic(eventTask, startTime, -delay.toNanos());
    }


    public static boolean hasTask(String taskId) {
        return periodicExecutor().hasPeriodicTask(taskId);
    }

    public static boolean cancelTask(String taskId) {
        return periodicExecutor().cancelPeriodicTask(taskId);
    }
}
