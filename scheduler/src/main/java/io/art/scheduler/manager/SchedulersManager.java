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

    public static <T> ForkJoinTask<? extends T> schedule(Callable<? extends T> task) {
        return deferredExecutor().submit(task, now());
    }

    public static <T> ForkJoinTask<? extends T> schedule(Callable<? extends T> task, LocalDateTime startTime) {
        getLogger().info(format(DEFERRED_TASK_SUBMITTED, startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT)));
        return deferredExecutor().submit(task, startTime);
    }

    public static ForkJoinTask<?> schedule(Runnable task) {
        return deferredExecutor().execute(task, now());
    }

    public static ForkJoinTask<?> schedule(Runnable task, LocalDateTime startTime) {
        getLogger().info(format(DEFERRED_TASK_SUBMITTED, startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT)));
        return deferredExecutor().execute(task, startTime);
    }


    public static <T> void scheduleFixedRate(ExceptionCallable<? extends T> task, Duration period) {
        scheduleFixedRate(task(task), period);
    }

    public static <T> void scheduleFixedRate(ExceptionCallable<? extends T> task, LocalDateTime startTime, Duration period) {
        scheduleFixedRate(task(task), startTime, period);
    }


    public static void scheduleFixedRate(ExceptionRunnable task, Duration duration) {
        scheduleFixedRate(task(task), duration);
    }

    public static void scheduleFixedRate(ExceptionRunnable task, LocalDateTime triggerTime, Duration duration) {
        scheduleFixedRate(task(task), triggerTime, duration);
    }


    public static <T> void scheduleFixedRate(CallableTask<? extends T> task, Duration period) {
        scheduleFixedRate(task, now(), period);
    }

    public static <T> void scheduleFixedRate(CallableTask<? extends T> task, LocalDateTime startTime, Duration period) {
        PeriodicCallableTask<T> periodicTask = PeriodicCallableTask.<T>builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(FIXED)
                .build();
        getLogger().info(format(PERIODIC_TASK_SUBMITTED, task.getId(), startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT), period));
        periodicExecutor().submit(periodicTask);
    }


    public static void scheduleFixedRate(RunnableTask task, Duration period) {
        scheduleFixedRate(task, now(), period);
    }

    public static void scheduleFixedRate(RunnableTask task, LocalDateTime startTime, Duration period) {
        PeriodicRunnableTask periodicTask = PeriodicRunnableTask.builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(FIXED)
                .build();
        getLogger().info(format(PERIODIC_TASK_SUBMITTED, task.getId(), startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT), period));
        periodicExecutor().execute(periodicTask);
    }


    public static void scheduleDelayed(ExceptionRunnable task, Duration delay) {
        scheduleDelayed(task(task), delay);
    }

    public static void scheduleDelayed(ExceptionRunnable task, LocalDateTime triggerTime, Duration delay) {
        scheduleDelayed(task(task), triggerTime, delay);
    }


    public static <T> void scheduleDelayed(ExceptionCallable<? extends T> task, Duration delay) {
        scheduleDelayed(task(task), delay);
    }

    public static <T> void scheduleDelayed(ExceptionCallable<? extends T> task, LocalDateTime startTime, Duration delay) {
        scheduleDelayed(task(task), startTime, delay);
    }


    public static void scheduleDelayed(RunnableTask task, Duration period) {
        scheduleDelayed(task, now(), period);
    }

    public static void scheduleDelayed(RunnableTask task, LocalDateTime startTime, Duration period) {
        PeriodicRunnableTask periodicTask = PeriodicRunnableTask.builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(DELAYED)
                .build();
        getLogger().info(format(PERIODIC_TASK_SUBMITTED, task.getId(), startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT), period));
        periodicExecutor().execute(periodicTask);
    }


    public static <T> void scheduleDelayed(CallableTask<? extends T> task, Duration period) {
        scheduleDelayed(task, now(), period);
    }

    public static <T> void scheduleDelayed(CallableTask<? extends T> task, LocalDateTime startTime, Duration period) {
        PeriodicCallableTask<T> periodicTask = PeriodicCallableTask.<T>builder()
                .delegate(task)
                .startTime(startTime)
                .period(period)
                .mode(DELAYED)
                .build();
        getLogger().info(format(PERIODIC_TASK_SUBMITTED, task.getId(), startTime.format(YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT), period));
        periodicExecutor().submit(periodicTask);
    }


    public static boolean hasTask(String taskId) {
        return periodicExecutor().hasTask(taskId);
    }

    public static boolean cancelTask(String taskId) {
        getLogger().info(format(PERIODIC_TASK_CANCELED, taskId));
        return periodicExecutor().cancelTask(taskId);
    }
}
