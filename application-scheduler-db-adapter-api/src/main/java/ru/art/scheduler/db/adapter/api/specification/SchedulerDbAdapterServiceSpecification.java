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

package ru.art.scheduler.db.adapter.api.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.art.remote.scheduler.api.model.DeferredTask;
import ru.art.remote.scheduler.api.model.InfinityProcessRequest;
import ru.art.remote.scheduler.api.model.PeriodicTask;
import ru.art.scheduler.db.adapter.api.model.UpdateTaskExecutionTimeRequest;
import ru.art.scheduler.db.adapter.api.model.UpdateTaskStatusRequest;
import ru.art.scheduler.db.adapter.api.service.SchedulerDbAdapterService;
import ru.art.service.Specification;
import ru.art.service.exception.UnknownServiceMethodException;
import static ru.art.core.caster.Caster.cast;
import static ru.art.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Methods.*;

@Getter
@AllArgsConstructor
public class SchedulerDbAdapterServiceSpecification implements Specification {
    private final String serviceId;
    private final SchedulerDbAdapterService schedulerDbAdapterService;

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case PUT_DEFERRED_TASK:
                return cast(schedulerDbAdapterService.putDeferredTask((DeferredTask) request));
            case PUT_PERIODIC_TASK:
                return cast(schedulerDbAdapterService.putPeriodicTask((PeriodicTask) request));
            case PUT_INFINITY_PROCESS:
                return cast(schedulerDbAdapterService.putInfinityProcess((InfinityProcessRequest) request));
            case GET_DEFERRED_TASK:
                return cast(schedulerDbAdapterService.getDeferredTask((String) request));
            case GET_PERIODIC_TASK:
                return cast(schedulerDbAdapterService.getPeriodicTask((String) request));
            case UPDATE_DEFERRED_TASK_STATUS:
                schedulerDbAdapterService.updateDeferredTaskStatus((UpdateTaskStatusRequest) request);
                return null;
            case UPDATE_PERIODIC_TASK_STATUS:
                schedulerDbAdapterService.updatePeriodicTaskStatus((UpdateTaskStatusRequest) request);
                return null;
            case UPDATE_TASK_EXECUTION_TIME:
                schedulerDbAdapterService.updateTaskExecutionTime((UpdateTaskExecutionTimeRequest) request);
                return null;
            case INC_EXECUTION_COUNT:
                return cast(schedulerDbAdapterService.incrementExecutionCount((String) request));
            case GET_ALL_DEFERRED_TASKS:
                return cast(schedulerDbAdapterService.getAllDeferredTasks());
            case GET_ALL_PERIODIC_TASKS:
                return cast(schedulerDbAdapterService.getAllPeriodicTasks());
            case GET_ALL_INFINITY_PROCESSES:
                return cast(schedulerDbAdapterService.getAllInfinityProcesses());
            default:
                throw new UnknownServiceMethodException(getServiceId(), methodId);
        }
    }
}
