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

package ru.art.remote.scheduler.service;

import ru.art.remote.scheduler.api.constants.RemoteSchedulerApiConstants.*;
import ru.art.remote.scheduler.api.exception.*;
import ru.art.remote.scheduler.api.model.*;
import ru.art.scheduler.db.adapter.api.model.*;
import ru.art.service.*;
import ru.art.task.deferred.executor.*;
import java.util.*;

import static java.time.Duration.*;
import static java.time.LocalDateTime.*;
import static java.util.Collections.*;
import static ru.art.core.extension.OptionalExtensions.*;
import static ru.art.remote.scheduler.action.TaskExecutionActions.*;
import static ru.art.remote.scheduler.api.constants.RemoteSchedulerApiConstants.TaskStatus.*;
import static ru.art.remote.scheduler.constants.RemoteSchedulerModuleConstants.ExceptionMessages.*;
import static ru.art.remote.scheduler.constants.RemoteSchedulerModuleConstants.ZERO;
import static ru.art.remote.scheduler.module.RemoteSchedulerModule.*;
import static ru.art.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Methods.*;
import static ru.art.service.ServiceController.*;


public interface RemoteSchedulerService {
    static String addDeferredTask(DeferredTaskRequest deferredTaskRequest) {
        DeferredTask task = DeferredTask.builder()
                .status(NEW)
                .executableRequest(deferredTaskRequest.getExecutableRequest())
                .creationDateTime(now())
                .executionDateTime(deferredTaskRequest.getExecutionDateTime())
                .executableMethodId(deferredTaskRequest.getExecutableMethodId())
                .executableServiceId(deferredTaskRequest.getExecutableServiceId())
                .executableServletPath(deferredTaskRequest.getExecutableServletPath())
                .build();
        Optional<String> id = executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), PUT_DEFERRED_TASK, task);

        if (!id.isPresent()) {
            throw new IdNotGeneratedException(ID_IS_EMPTY);
        }

        task.setId(id.get());

        SchedulerModuleActions.asynchronous(() -> executeDeferredTask(task), task.getExecutionDateTime());
        return task.getId();
    }

    static String addPeriodicTask(PeriodicTaskRequest periodicTaskRequest) {
        PeriodicTask task = PeriodicTask.builder()
                .status(NEW)
                .executableRequest(periodicTaskRequest.getExecutableRequest())
                .creationDateTime(now())
                .executionDateTime(periodicTaskRequest.getExecutionDateTime())
                .executableMethodId(periodicTaskRequest.getExecutableMethodId())
                .executableServiceId(periodicTaskRequest.getExecutableServiceId())
                .executableServletPath(periodicTaskRequest.getExecutableServletPath())
                .maxExecutionCount(periodicTaskRequest.getMaxExecutionCount())
                .executionPeriodSeconds(periodicTaskRequest.getExecutionPeriodSeconds())
                .executionCount(ZERO)
                .finishAfterCompletion(periodicTaskRequest.isFinishAfterCompletion())
                .build();
        Optional<String> id = executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), PUT_PERIODIC_TASK, task);

        if (!id.isPresent()) {
            throw new IdNotGeneratedException(ID_IS_EMPTY);
        }

        task.setId(id.get());

        IdentifiedRunnable runnableTask = new IdentifiedRunnable(task.getId(), () -> submitPeriodicTask(task));
        SchedulerModuleActions.asynchronousPeriod(runnableTask, periodicTaskRequest.getExecutionDateTime(), ofSeconds(task.getExecutionPeriodSeconds()));
        return task.getId();
    }

    static String addInfinityProcess(InfinityProcessRequest request) {
        Optional<String> id = executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), PUT_INFINITY_PROCESS, request);
        if (!id.isPresent()) {
            throw new IdNotGeneratedException(ID_IS_EMPTY);
        }

        InfinityProcess infinityProcess = new InfinityProcess(id.get(), request);

        IdentifiedRunnable runnableTask = new IdentifiedRunnable(infinityProcess.getId(), () -> submitInfinityProcess(infinityProcess));
        remoteSchedulerModuleState().getPeriodicInfinityExecutor().executePeriodic(runnableTask,
                now().plus(ofSeconds(infinityProcess.getExecutionDelay())),
                ofSeconds(infinityProcess.getExecutionPeriodSeconds()));

        return infinityProcess.getId();
    }

    static DeferredTask getDeferredTaskById(String taskId) {
        return unwrap(executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), GET_DEFERRED_TASK, taskId));
    }

    static PeriodicTask getPeriodicTaskById(String taskId) {
        return unwrap(executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), GET_PERIODIC_TASK, taskId));
    }

    static Set<DeferredTask> getAllDeferredTasks() {
        return ServiceController.<Set<DeferredTask>>executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), GET_ALL_DEFERRED_TASKS)
                .orElse(emptySet());
    }

    static Set<PeriodicTask> getAllPeriodicTasks() {
        return ServiceController.<Set<PeriodicTask>>executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), GET_ALL_PERIODIC_TASKS)
                .orElse(emptySet());
    }

    static Set<InfinityProcess> getAllInfinityProcesses() {
        return ServiceController.<Set<InfinityProcess>>executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), GET_ALL_INFINITY_PROCESSES)
                .orElse(emptySet());
    }

    static void cancelPeriodicTask(String taskId) {
        SchedulerModuleActions.cancelPeriodicTask(taskId);
        executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), UPDATE_PERIODIC_TASK_STATUS, new UpdateTaskStatusRequest(taskId, TaskStatus.CANCELLED));
    }

}