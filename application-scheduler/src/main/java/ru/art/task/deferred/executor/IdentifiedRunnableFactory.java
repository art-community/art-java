package ru.art.task.deferred.executor;

import static java.util.UUID.randomUUID;
import static ru.art.task.deferred.executor.SchedulerModuleConstants.COMMON_TASK;

public interface IdentifiedRunnableFactory {
    static IdentifiedRunnable uniqueTask(Runnable runnable) {
        return new IdentifiedRunnable(randomUUID().toString(), runnable);
    }

    static IdentifiedRunnable commonTask(Runnable runnable) {
        return new IdentifiedRunnable(COMMON_TASK, runnable);
    }
}
