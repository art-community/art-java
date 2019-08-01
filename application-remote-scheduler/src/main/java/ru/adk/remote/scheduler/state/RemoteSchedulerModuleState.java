package ru.adk.remote.scheduler.state;

import lombok.Getter;
import ru.adk.core.module.ModuleState;
import ru.adk.task.deferred.executor.DeferredExecutorExceptionHandler;
import ru.adk.task.deferred.executor.DeferredExecutorImpl;
import ru.adk.task.deferred.executor.PeriodicExecutor;

@Getter
public class RemoteSchedulerModuleState implements ModuleState {
    private final PeriodicExecutor periodicInfinityExecutor = new PeriodicExecutor(DeferredExecutorImpl.builder()
            .withExceptionHandler(new DeferredExecutorExceptionHandler())
            .build());
}
