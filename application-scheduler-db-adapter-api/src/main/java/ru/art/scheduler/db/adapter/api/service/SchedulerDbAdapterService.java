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

package ru.art.scheduler.db.adapter.api.service;

import ru.art.remote.scheduler.api.model.DeferredTask;
import ru.art.remote.scheduler.api.model.InfinityProcess;
import ru.art.remote.scheduler.api.model.InfinityProcessRequest;
import ru.art.remote.scheduler.api.model.PeriodicTask;
import ru.art.scheduler.db.adapter.api.model.UpdateTaskExecutionTimeRequest;
import ru.art.scheduler.db.adapter.api.model.UpdateTaskStatusRequest;
import java.util.Set;

public interface SchedulerDbAdapterService {
    String putDeferredTask(DeferredTask task);

    String putPeriodicTask(PeriodicTask task);

    String putInfinityProcess(InfinityProcessRequest process);

    DeferredTask getDeferredTask(String taskId);

    PeriodicTask getPeriodicTask(String taskId);

    void updateDeferredTaskStatus(UpdateTaskStatusRequest taskIdAndStatus);

    void updatePeriodicTaskStatus(UpdateTaskStatusRequest taskIdAndStatus);

    void updateTaskExecutionTime(UpdateTaskExecutionTimeRequest executionPeriodSeconds);

    int incrementExecutionCount(String taskId);

    Set<DeferredTask> getAllDeferredTasks();

    Set<PeriodicTask> getAllPeriodicTasks();

    Set<InfinityProcess> getAllInfinityProcesses();
}
