package ru.adk.remote.scheduler.service;

import ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.TaskStatus;
import ru.adk.remote.scheduler.api.exception.IdNotGeneratedException;
import ru.adk.remote.scheduler.api.model.*;
import ru.adk.scheduler.db.adapter.api.model.UpdateTaskStatusRequest;
import ru.adk.service.ServiceController;
import ru.adk.task.deferred.executor.IdentifiedRunnable;
import ru.adk.task.deferred.executor.SchedulerModuleActions;
import static java.time.Duration.ofSeconds;
import static java.time.LocalDateTime.now;
import static java.util.Collections.emptySet;
import static ru.adk.core.extension.OptionalExtensions.unwrap;
import static ru.adk.remote.scheduler.action.TaskExecutionActions.*;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.TaskStatus.NEW;
import static ru.adk.remote.scheduler.constants.RemoteSchedulerModuleConstants.ExceptionMessages.ID_IS_EMPTY;
import static ru.adk.remote.scheduler.constants.RemoteSchedulerModuleConstants.ZERO;
import static ru.adk.remote.scheduler.module.RemoteSchedulerModule.remoteSchedulerModule;
import static ru.adk.remote.scheduler.module.RemoteSchedulerModule.remoteSchedulerModuleState;
import static ru.adk.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Methods.*;
import static ru.adk.service.ServiceController.executeServiceMethod;
import static ru.adk.task.deferred.executor.SchedulerModuleActions.asynchronous;
import static ru.adk.task.deferred.executor.SchedulerModuleActions.asynchronousPeriod;
import java.util.Optional;
import java.util.Set;


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