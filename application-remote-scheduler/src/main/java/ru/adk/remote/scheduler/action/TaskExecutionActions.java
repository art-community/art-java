package ru.adk.remote.scheduler.action;

import ru.adk.core.caster.Caster;
import ru.adk.entity.Value;
import ru.adk.remote.scheduler.api.model.DeferredTask;
import ru.adk.remote.scheduler.api.model.InfinityProcess;
import ru.adk.remote.scheduler.api.model.PeriodicTask;
import ru.adk.scheduler.db.adapter.api.model.UpdateTaskExecutionTimeRequest;
import ru.adk.scheduler.db.adapter.api.model.UpdateTaskStatusRequest;
import static java.text.MessageFormat.format;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.grpc.client.communicator.GrpcCommunicator.grpcCommunicator;
import static ru.adk.logging.LoggingModule.loggingModule;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.TaskStatus.*;
import static ru.adk.remote.scheduler.constants.RemoteSchedulerModuleConstants.LoggingMessages.*;
import static ru.adk.remote.scheduler.constants.RemoteSchedulerModuleConstants.ZERO;
import static ru.adk.remote.scheduler.module.RemoteSchedulerModule.remoteSchedulerModule;
import static ru.adk.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Methods.*;
import static ru.adk.service.ServiceController.executeServiceMethod;
import static ru.adk.task.deferred.executor.SchedulerModuleActions.cancelPeriodicTask;
import static ru.adk.task.deferred.executor.SchedulerModuleActions.removePeriodicTask;
import java.util.Optional;

public interface TaskExecutionActions {
    static void executeDeferredTask(DeferredTask task) {
        loggingModule().getLogger().info(format(TASK_STARTED_MESSAGE, task));
        try {
            Optional<Value> response = grpcCommunicator(remoteSchedulerModule().getBalancerHost(),
                    remoteSchedulerModule().getBalancerPort(),
                    task.getExecutableServletPath())
                    .serviceId(task.getExecutableServiceId())
                    .methodId(task.getExecutableMethodId())
                    .requestMapper(Caster::cast)
                    .execute(task.getExecutableRequest());
            handleDeferredTaskCompletion(task, response);
        } catch (Exception e) {
            handleDeferredTaskError(e, task);
        }
    }

    static void submitPeriodicTask(PeriodicTask task) {
        loggingModule().getLogger().info(format(TASK_STARTED_MESSAGE, task));
        try {
            Optional<Value> response = grpcCommunicator(remoteSchedulerModule().getBalancerHost(),
                    remoteSchedulerModule().getBalancerPort(),
                    task.getExecutableServletPath())
                    .serviceId(task.getExecutableServiceId())
                    .methodId(task.getExecutableMethodId())
                    .requestMapper(Caster::cast)
                    .execute(task.getExecutableRequest());
            handlePeriodicTaskCompletion(task, response);
        } catch (Exception e) {
            handlePeriodicTaskError(e, task);
        }
    }

    static void submitInfinityProcess(InfinityProcess process) {
        loggingModule().getLogger().info(format(PROCESS_STARTED_MESSAGE, process));
        try {
            Optional<Value> response = grpcCommunicator(remoteSchedulerModule().getBalancerHost(),
                    remoteSchedulerModule().getBalancerPort(),
                    process.getExecutableServletPath())
                    .serviceId(process.getExecutableServiceId())
                    .methodId(process.getExecutableMethodId())
                    .requestMapper(Caster::cast)
                    .execute(process.getExecutableRequest());
            handleInfinityProcessCompletion(process, response);
        } catch (Exception e) {
            handleInfinityProcessError(e, process);
        }
    }

    static void handlePeriodicTaskError(Exception error, PeriodicTask task) {
        loggingModule().getLogger().error(format(TASK_FAILED_MESSAGE, task), error);
        handlePeriodicTaskAction(task);
    }

    static void handleDeferredTaskError(Throwable error, DeferredTask task) {
        loggingModule().getLogger().error(format(TASK_FAILED_MESSAGE, task), error);
        executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), UPDATE_DEFERRED_TASK_STATUS, new UpdateTaskStatusRequest(task.getId(), FAILED));
    }

    static void handleInfinityProcessError(Throwable error, InfinityProcess process) {
        loggingModule().getLogger().error(format(PROCESS_FAILED_MESSAGE, process), error);
    }


    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static void handlePeriodicTaskCompletion(PeriodicTask task, Optional<Value> response) {
        loggingModule().getLogger().info(format(TASK_COMPLETED_MESSAGE, task, response.isPresent() ? response.get() : EMPTY_STRING));

        if (task.isFinishAfterCompletion()) {
            executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), INC_EXECUTION_COUNT, task.getId());
            executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), UPDATE_PERIODIC_TASK_STATUS, new UpdateTaskStatusRequest(task.getId(), FINISHED));
            cancelPeriodicTask(task.getId());
            return;
        }

        handlePeriodicTaskAction(task);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static void handleDeferredTaskCompletion(DeferredTask task, Optional<Value> response) {
        loggingModule().getLogger().info(format(TASK_COMPLETED_MESSAGE, task, response.isPresent() ? response.get() : EMPTY_STRING));
        executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), UPDATE_DEFERRED_TASK_STATUS, new UpdateTaskStatusRequest(task.getId(), COMPLETED));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static void handleInfinityProcessCompletion(InfinityProcess process, Optional<Value> response) {
        loggingModule().getLogger().info(format(PROCESS_COMPLETED_MESSAGE, process, response.isPresent() ? response.get() : EMPTY_STRING));
    }

    static void updateTaskStatusProcessingIfFirstAttempt(String taskId, int currentRetryCount) {
        if (currentRetryCount <= 1) {
            executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), UPDATE_PERIODIC_TASK_STATUS, new UpdateTaskStatusRequest(taskId, PROCESSING));
        }
    }

    static void handlePeriodicTaskAction(PeriodicTask task) {
        Optional<Integer> currentExecutionCount = executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), INC_EXECUTION_COUNT, task.getId());
        updateTaskStatusProcessingIfFirstAttempt(task.getId(), currentExecutionCount.orElse(ZERO));
        executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), UPDATE_TASK_EXECUTION_TIME, new UpdateTaskExecutionTimeRequest(task.getId(), task.getExecutionPeriodSeconds()));

        if (!currentExecutionCount.isPresent() || currentExecutionCount.get() < task.getMaxExecutionCount()) {
            return;
        }
        removePeriodicTask(task.getId());
        executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), UPDATE_PERIODIC_TASK_STATUS, new UpdateTaskStatusRequest(task.getId(), FINISHED));
    }
}
