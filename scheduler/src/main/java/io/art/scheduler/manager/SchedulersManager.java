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
import lombok.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import static io.art.core.constants.DateTimeConstants.*;
import static io.art.logging.LoggingModule.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.LoggingMessages.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.PeriodicTaskMode.*;
import static io.art.scheduler.factory.TaskFactory.*;
import static io.art.scheduler.module.SchedulerModule.*;
import static java.text.MessageFormat.*;
import static java.time.LocalDateTime.*;
import static lombok.AccessLevel.*;
import java.time.*;
import java.util.concurrent.*;

@UtilityClass
public class SchedulersManager {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(SchedulersManager.class);

    public static <T> Future<? extends T> schedule(Callable<? extends T> task) {
        return deferredExecutor().submit(task, now());
    }

    public static <T> Future<? extends T> schedule(Callable<? extends T> task, LocalDateTime startTime) {
        getLogger().info(format(DEFERRED_TASK_SUBMITTED, startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT)));
        return deferredExecutor().submit(task, startTime);
    }

    public static Future<?> schedule(Runnable task) {
        return deferredExecutor().execute(task, now());
    }

    public static Future<?> schedule(Runnable task, LocalDateTime startTime) {
        getLogger().info(format(DEFERRED_TASK_SUBMITTED, startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT)));
        return deferredExecutor().execute(task, startTime);
    }


    public static <T> Future<? extends T> scheduleFixedRate(ExceptionCallable<? extends T> task, Duration period) {
        return scheduleFixedRate(task(task), period);
    }

    public static <T> Future<? extends T> scheduleFixedRate(ExceptionCallable<? extends T> task, LocalDateTime startTime, Duration period) {
        return scheduleFixedRate(task(task), startTime, period);
    }


    public static Future<?> scheduleFixedRate(ExceptionRunnable task, Duration duration) {
        return scheduleFixedRate(task(task), duration);
    }

    public static Future<?> scheduleFixedRate(ExceptionRunnable task, LocalDateTime triggerTime, Duration duration) {
        return scheduleFixedRate(task(task), triggerTime, duration);
    }


    public static <T> Future<? extends T> scheduleFixedRate(CallableTask<? extends T> task, Duration period) {
        return scheduleFixedRate(task, now(), period);
    }

    public static <T> Future<? extends T> scheduleFixedRate(CallableTask<? extends T> task, LocalDateTime startTime, Duration period) {
        PeriodicCallableTask<T> periodicTask = PeriodicCallableTask.<T>builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(FIXED)
                .build();
        getLogger().info(format(PERIODIC_TASK_SUBMITTED, task.getId(), startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT), period));
        return periodicExecutor().submit(periodicTask);
    }


    public static Future<?> scheduleFixedRate(RunnableTask task, Duration period) {
        return scheduleFixedRate(task, now(), period);
    }

    public static Future<?> scheduleFixedRate(RunnableTask task, LocalDateTime startTime, Duration period) {
        PeriodicRunnableTask periodicTask = PeriodicRunnableTask.builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(FIXED)
                .build();
        getLogger().info(format(PERIODIC_TASK_SUBMITTED, task.getId(), startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT), period));
        return periodicExecutor().execute(periodicTask);
    }


    public static Future<?> scheduleDelayed(ExceptionRunnable task, Duration delay) {
        return scheduleDelayed(task(task), delay);
    }

    public static Future<?> scheduleDelayed(ExceptionRunnable task, LocalDateTime triggerTime, Duration delay) {
        return scheduleDelayed(task(task), triggerTime, delay);
    }


    public static <T> Future<? extends T> scheduleDelayed(ExceptionCallable<? extends T> task, Duration delay) {
        return scheduleDelayed(task(task), delay);
    }

    public static <T> Future<? extends T> scheduleDelayed(ExceptionCallable<? extends T> task, LocalDateTime startTime, Duration delay) {
        return scheduleDelayed(task(task), startTime, delay);
    }


    public static Future<?> scheduleDelayed(RunnableTask task, Duration period) {
        return scheduleDelayed(task, now(), period);
    }

    public static Future<?> scheduleDelayed(RunnableTask task, LocalDateTime startTime, Duration period) {
        PeriodicRunnableTask periodicTask = PeriodicRunnableTask.builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(DELAYED)
                .build();
        getLogger().info(format(PERIODIC_TASK_SUBMITTED, task.getId(), startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT), period));
        return periodicExecutor().execute(periodicTask);
    }


    public static <T> Future<? extends T> scheduleDelayed(CallableTask<? extends T> task, Duration period) {
        return scheduleDelayed(task, now(), period);
    }

    public static <T> Future<? extends T> scheduleDelayed(CallableTask<? extends T> task, LocalDateTime startTime, Duration period) {
        PeriodicCallableTask<T> periodicTask = PeriodicCallableTask.<T>builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(DELAYED)
                .build();
        getLogger().info(format(PERIODIC_TASK_SUBMITTED, task.getId(), startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT), period));
        return periodicExecutor().submit(periodicTask);
    }


    public static boolean hasTask(String taskId) {
        return periodicExecutor().hasTask(taskId).get();
    }

    public static boolean cancelTask(String taskId) {
        getLogger().info(format(PERIODIC_TASK_CANCELED, taskId));
        return periodicExecutor().cancelTask(taskId);
    }
}
