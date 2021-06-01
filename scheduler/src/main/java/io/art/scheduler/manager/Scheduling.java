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

package io.art.scheduler.manager;

import io.art.core.callable.*;
import io.art.core.runnable.*;
import io.art.scheduler.model.*;
import lombok.experimental.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.PeriodicTaskMode.*;
import static io.art.scheduler.factory.TaskFactory.*;
import static io.art.scheduler.module.SchedulerModule.*;
import static java.time.LocalDateTime.*;
import java.time.*;
import java.util.concurrent.*;

@UtilityClass
public class Scheduling {
    public static <T> Future<? extends T> schedule(Callable<? extends T> task) {
        return deferredExecutor().submit(task, now());
    }

    public static <T> Future<? extends T> schedule(LocalDateTime startTime, Callable<? extends T> task) {
        return deferredExecutor().submit(task, startTime);
    }


    public static Future<?> schedule(Runnable task) {
        return deferredExecutor().execute(task, now());
    }

    public static Future<?> schedule(LocalDateTime startTime, Runnable task) {
        return deferredExecutor().execute(task, startTime);
    }


    public static <T> void scheduleFixedRate(Duration period, ExceptionCallable<? extends T> task) {
        scheduleFixedRate(period, task(task));
    }

    public static <T> void scheduleFixedRate(LocalDateTime startTime, Duration period, ExceptionCallable<? extends T> task) {
        scheduleFixedRate(startTime, period, task(task));
    }


    public static void scheduleFixedRate(Duration duration, ExceptionRunnable task) {
        scheduleFixedRate(duration, task(task));
    }

    public static void scheduleFixedRate(LocalDateTime triggerTime, Duration duration, ExceptionRunnable task) {
        scheduleFixedRate(triggerTime, duration, task(task));
    }


    public static <T> void scheduleFixedRate(Duration period, CallableTask<? extends T> task) {
        scheduleFixedRate(now(), period, task);
    }

    public static <T> void scheduleFixedRate(LocalDateTime startTime, Duration period, CallableTask<? extends T> task) {
        PeriodicCallableTask<T> periodicTask = PeriodicCallableTask.<T>builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(FIXED)
                .build();
        periodicExecutor().submit(periodicTask);
    }


    public static void scheduleFixedRate(Duration period, RunnableTask task) {
        scheduleFixedRate(now(), period, task);
    }

    public static void scheduleFixedRate(LocalDateTime startTime, Duration period, RunnableTask task) {
        PeriodicRunnableTask periodicTask = PeriodicRunnableTask.builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(FIXED)
                .build();
        periodicExecutor().execute(periodicTask);
    }


    public static void scheduleDelayed(Duration delay, ExceptionRunnable task) {
        scheduleDelayed(delay, task(task));
    }

    public static void scheduleDelayed(LocalDateTime triggerTime, Duration delay, ExceptionRunnable task) {
        scheduleDelayed(triggerTime, delay, task(task));
    }


    public static <T> void scheduleDelayed(Duration delay, ExceptionCallable<? extends T> task) {
        scheduleDelayed(delay, task(task));
    }

    public static <T> void scheduleDelayed(LocalDateTime startTime, Duration delay, ExceptionCallable<? extends T> task) {
        scheduleDelayed(startTime, delay, task(task));
    }


    public static void scheduleDelayed(Duration period, RunnableTask task) {
        scheduleDelayed(now(), period, task);
    }

    public static void scheduleDelayed(LocalDateTime startTime, Duration period, RunnableTask task) {
        PeriodicRunnableTask periodicTask = PeriodicRunnableTask.builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(DELAYED)
                .build();
        periodicExecutor().execute(periodicTask);
    }


    public static <T> void scheduleDelayed(Duration period, CallableTask<? extends T> task) {
        scheduleDelayed(now(), period, task);
    }

    public static <T> void scheduleDelayed(LocalDateTime startTime, Duration period, CallableTask<? extends T> task) {
        PeriodicCallableTask<T> periodicTask = PeriodicCallableTask.<T>builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(DELAYED)
                .build();
        periodicExecutor().submit(periodicTask);
    }


    public static boolean hasTask(String taskId) {
        return periodicExecutor().hasTask(taskId);
    }

    public static boolean cancelTask(String taskId) {
        return periodicExecutor().cancelTask(taskId);
    }
}
