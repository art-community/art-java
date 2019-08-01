package ru.art.remote.scheduler.action;

import ru.art.core.caster.Caster;
import ru.art.entity.Value;
import ru.art.remote.scheduler.api.model.DeferredTask;
import ru.art.remote.scheduler.api.model.InfinityProcess;
import ru.art.remote.scheduler.api.model.PeriodicTask;
import ru.art.scheduler.db.adapter.api.model.UpdateTaskExecutionTimeRequest;
import ru.art.scheduler.db.adapter.api.model.UpdateTaskStatusRequest;
import ru.art.service.model.ServiceResponse;
import static java.text.MessageFormat.format;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.grpc.client.communicator.GrpcCommunicator.grpcCommunicator;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.remote.scheduler.api.constants.RemoteSchedulerApiConstants.TaskStatus.*;
import static ru.art.remote.scheduler.constants.RemoteSchedulerModuleConstants.LoggingMessages.*;
import static ru.art.remote.scheduler.constants.RemoteSchedulerModuleConstants.ZERO;
import static ru.art.remote.scheduler.module.RemoteSchedulerModule.remoteSchedulerModule;
import static ru.art.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Methods.*;
import static ru.art.service.ServiceController.executeServiceMethod;
import static ru.art.task.deferred.executor.SchedulerModuleActions.cancelPeriodicTask;
import static ru.art.task.deferred.executor.SchedulerModuleActions.removePeriodicTask;
import java.util.Optional;

public interface TaskExecutionActions {
    static void executeDeferredTask(DeferredTask task) {
        loggingModule().getLogger().info(format(TASK_STARTED_MESSAGE, task));
        try {
            ServiceResponse<Value> response = grpcCommunicator(remoteSchedulerModule().getBalancerHost(),
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
            ServiceResponse<Value> response = grpcCommunicator(remoteSchedulerModule().getBalancerHost(),
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
            ServiceResponse<Value> response = grpcCommunicator(remoteSchedulerModule().getBalancerHost(),
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

    static void handlePeriodicTaskCompletion(PeriodicTask task, ServiceResponse<Value> response) {
        loggingModule().getLogger().info(format(TASK_COMPLETED_MESSAGE, task, getOrElse(response.getResponseData(), EMPTY_STRING)));

        if (task.isFinishAfterCompletion()) {
            executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), INC_EXECUTION_COUNT, task.getId());
            executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), UPDATE_PERIODIC_TASK_STATUS, new UpdateTaskStatusRequest(task.getId(), FINISHED));
            cancelPeriodicTask(task.getId());
            return;
        }

        handlePeriodicTaskAction(task);
    }

    static void handleDeferredTaskCompletion(DeferredTask task, ServiceResponse<Value> response) {
        loggingModule().getLogger().info(format(TASK_COMPLETED_MESSAGE, task, getOrElse(response.getResponseData(), EMPTY_STRING)));
        executeServiceMethod(remoteSchedulerModule().getDbAdapterServiceId(), UPDATE_DEFERRED_TASK_STATUS, new UpdateTaskStatusRequest(task.getId(), COMPLETED));
    }

    static void handleInfinityProcessCompletion(InfinityProcess process, ServiceResponse<Value> response) {
        loggingModule().getLogger().info(format(PROCESS_COMPLETED_MESSAGE, process, getOrElse(response.getResponseData(), EMPTY_STRING)));
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
