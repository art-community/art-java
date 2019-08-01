package ru.art.task.deferred.executor;

import static ru.art.task.deferred.executor.SchedulerModule.schedulerModule;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface SchedulerModuleActions {

    static <EventResultType> Future<? extends EventResultType> asynchronous(Callable<? extends EventResultType> eventTask, LocalDateTime triggerTime) {
        return schedulerModule().getDeferredExecutor().submit(eventTask, triggerTime);
    }

    static <EventResultType> Future<? extends EventResultType> asynchronous(Callable<? extends EventResultType> eventTask) {
        return schedulerModule().getDeferredExecutor().submit(eventTask);
    }

    static Future<?> asynchronous(Runnable task, LocalDateTime triggerTime) {
        return schedulerModule().getDeferredExecutor().execute(task, triggerTime);
    }

    static Future<?> asynchronous(Runnable task) {
        return schedulerModule().getDeferredExecutor().execute(task);
    }

    static <EventResultType> Future<? extends EventResultType> asynchronousPeriod(IdentifiedCallable<? extends EventResultType> eventTask,
                                                                                  LocalDateTime startTime, Duration duration) {
        return schedulerModule().getPeriodicExecutor().submitPeriodic(eventTask, startTime, duration);
    }

    static <EventResultType> Future<? extends EventResultType> asynchronousPeriod(IdentifiedCallable<? extends EventResultType> eventTask,
                                                                                  Duration duration) {
        return schedulerModule().getPeriodicExecutor().submitPeriodic(eventTask, duration);
    }

    static Future<?> asynchronousPeriod(IdentifiedRunnable task, LocalDateTime triggerTime, Duration duration) {
        return schedulerModule().getPeriodicExecutor().executePeriodic(task, triggerTime, duration);
    }

    static Future<?> asynchronousPeriod(IdentifiedRunnable task, Duration duration) {
        return schedulerModule().getPeriodicExecutor().executePeriodic(task, duration);
    }

    static boolean cancelPeriodicTask(String taskId) {
        return schedulerModule().getPeriodicExecutor().cancelPeriodicTask(taskId);
    }

    static Future<?> removePeriodicTask(String taskId) {
        return schedulerModule().getPeriodicExecutor().removePeriodicTask(taskId);
    }

    static void clearDeferred() {
        schedulerModule().getDeferredExecutor().clear();
    }

    static void clearPeriodic() {
        schedulerModule().getPeriodicExecutor().clear();
    }
}
