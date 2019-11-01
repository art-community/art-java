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

package ru.art.remote.scheduler.controller;

import ru.art.remote.scheduler.api.model.*;
import ru.art.service.*;
import ru.art.task.deferred.executor.*;
import static java.time.Duration.*;
import static java.time.LocalDateTime.*;
import static java.util.Collections.*;
import static ru.art.remote.scheduler.action.TaskExecutionActions.*;
import static ru.art.remote.scheduler.api.constants.RemoteSchedulerApiConstants.Methods.GET_ALL_DEFERRED_TASKS;
import static ru.art.remote.scheduler.api.constants.RemoteSchedulerApiConstants.Methods.GET_ALL_PERIODIC_TASKS;
import static ru.art.remote.scheduler.api.constants.RemoteSchedulerApiConstants.*;
import static ru.art.remote.scheduler.constants.RemoteSchedulerModuleConstants.*;
import static ru.art.remote.scheduler.module.RemoteSchedulerModule.*;
import static ru.art.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Methods.GET_ALL_INFINITY_PROCESSES;
import static ru.art.task.deferred.executor.SchedulerModule.*;
import static ru.art.task.deferred.executor.SchedulerModuleActions.*;
import static ru.art.task.deferred.executor.TaskFactory.*;
import java.time.*;
import java.util.*;

public interface PoolController {
    static void fillAllPools() {
        fillDeferredPool();
        fillPeriodicPool();
        fillInfinityProcessPool();
    }

    static void startPoolRefreshingTask() {
        PeriodicExecutor periodicRefreshExecutor = new PeriodicExecutor(DeferredExecutorImplementation.builder()
                .withExceptionHandler(new DeferredExecutorExceptionHandler())
                .build());

        RunnableTask refreshDeferredTask = runnableTask(REFRESH_DEFERRED_TASK_KEY, taskId -> fillDeferredPool());
        RunnableTask refreshPeriodicTask = runnableTask(REFRESH_PERIODIC_TASK_KEY, PoolController::fillPeriodicPool);

        periodicRefreshExecutor.executePeriodic(refreshDeferredTask, now().plusMinutes(remoteSchedulerModule().getRefreshDeferredPeriodMinutes()), ofMinutes(remoteSchedulerModule().getRefreshDeferredPeriodMinutes()));
        periodicRefreshExecutor.executePeriodic(refreshPeriodicTask, now().plusMinutes(remoteSchedulerModule().getRefreshPeriodicPeriodMinutes()), ofMinutes(remoteSchedulerModule().getRefreshPeriodicPeriodMinutes()));
    }

    static void fillDeferredPool() {
        schedulerModule().getDeferredExecutor().clear();
        Set<DeferredTask> deferredTasks = ServiceController.<Set<DeferredTask>>executeServiceMethod(REMOTE_SCHEDULER_SERVICE_ID, GET_ALL_DEFERRED_TASKS).orElse(emptySet());
        for (DeferredTask task : deferredTasks) {
            LocalDateTime triggerTime = task.getExecutionDateTime();
            if (triggerTime.isBefore(now())) {
                asynchronous(() -> executeDeferredTask(task));
                continue;
            }
            SchedulerModuleActions.asynchronous(() -> executeDeferredTask(task), triggerTime);
        }
    }

    static void fillPeriodicPool() {
        schedulerModule().getPeriodicExecutor().clear();
        Set<PeriodicTask> periodicTasks = ServiceController.<Set<PeriodicTask>>executeServiceMethod(REMOTE_SCHEDULER_SERVICE_ID, GET_ALL_PERIODIC_TASKS).orElse(emptySet());
        for (PeriodicTask task : periodicTasks) {
            RunnableTask runnableTask = runnableTask(task.getId(), () -> submitPeriodicTask(task));
            LocalDateTime executionDateTime = task.getExecutionDateTime();
            if (executionDateTime.isBefore(now())) {
                asynchronousPeriod(runnableTask, ofSeconds(task.getExecutionPeriodSeconds()));
                continue;
            }
            SchedulerModuleActions.asynchronousPeriod(runnableTask, executionDateTime, ofSeconds(task.getExecutionPeriodSeconds()));
        }
    }

    static void fillInfinityProcessPool() {
        remoteSchedulerModuleState().getPeriodicInfinityExecutor().clear();
        Set<InfinityProcess> infinityProcesses = ServiceController.<Set<InfinityProcess>>executeServiceMethod(REMOTE_SCHEDULER_SERVICE_ID, GET_ALL_INFINITY_PROCESSES).orElse(emptySet());
        for (InfinityProcess process : infinityProcesses) {
            RunnableTask runnableTask = runnableTask(process.getId(), () -> submitInfinityProcess(process));
            remoteSchedulerModuleState().getPeriodicInfinityExecutor().executePeriodic(runnableTask,
                    LocalDateTime.now().plus(ofSeconds(process.getExecutionDelay())),
                    ofSeconds(process.getExecutionPeriodSeconds()));
        }
    }
}
