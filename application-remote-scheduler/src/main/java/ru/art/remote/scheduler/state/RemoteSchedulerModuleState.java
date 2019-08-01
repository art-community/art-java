package ru.art.remote.scheduler.state;

import lombok.Getter;
import ru.art.core.module.ModuleState;
import ru.art.task.deferred.executor.DeferredExecutorExceptionHandler;
import ru.art.task.deferred.executor.DeferredExecutorImpl;
import ru.art.task.deferred.executor.PeriodicExecutor;

@Getter
public class RemoteSchedulerModuleState implements ModuleState {
    private final PeriodicExecutor periodicInfinityExecutor = new PeriodicExecutor(DeferredExecutorImpl.builder()
            .withExceptionHandler(new DeferredExecutorExceptionHandler())
            .build());
}
