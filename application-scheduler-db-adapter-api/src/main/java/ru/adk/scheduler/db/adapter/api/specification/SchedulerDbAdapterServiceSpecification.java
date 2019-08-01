package ru.adk.scheduler.db.adapter.api.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.adk.remote.scheduler.api.model.DeferredTask;
import ru.adk.remote.scheduler.api.model.InfinityProcessRequest;
import ru.adk.remote.scheduler.api.model.PeriodicTask;
import ru.adk.scheduler.db.adapter.api.model.UpdateTaskExecutionTimeRequest;
import ru.adk.scheduler.db.adapter.api.model.UpdateTaskStatusRequest;
import ru.adk.scheduler.db.adapter.api.service.SchedulerDbAdapterService;
import ru.adk.service.Specification;
import ru.adk.service.exception.UnknownServiceMethodException;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Methods.*;

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
