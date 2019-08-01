package ru.art.task.deferred.executor;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface SchedulerModuleExceptions {
    String EXCEPTION_OCCURRED_DURING = "Exception occurred during {0}: ''{1}''";

    @Getter
    @AllArgsConstructor
    enum ExceptionEvent {
        TASK_EXECUTION("task execution"),
        TASK_OBSERVING("task observing"),
        POOL_SHUTDOWN("pool shutdown");

        private final String message;
    }
}
