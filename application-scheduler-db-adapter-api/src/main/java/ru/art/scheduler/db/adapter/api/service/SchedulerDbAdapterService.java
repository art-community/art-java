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
