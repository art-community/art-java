package ru.art.task.deferred.executor;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Конфигурация для {@link DeferredExecutorImpl} и {@link DeferredEventObserver}
 */
class DeferredExecutorConfiguration {
    private ExceptionHandler exceptionHandler;
    private UncaughtExceptionHandler threadPoolExceptionHandler;
    private int eventsQueueMaxSize;
    private int threadPoolCoreSize;
    private boolean awaitAllTasksTerminationOnShutdown;
    private long threadPoolTerminationTimeoutMillis;

    ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    int getEventsQueueMaxSize() {
        return eventsQueueMaxSize;
    }

    void setEventsQueueMaxSize(int eventsQueueMaxSize) {
        this.eventsQueueMaxSize = eventsQueueMaxSize;
    }

    int getThreadPoolCoreSize() {
        return threadPoolCoreSize;
    }

    void setThreadPoolCoreSize(int threadPoolCoreSize) {
        this.threadPoolCoreSize = threadPoolCoreSize;
    }

    UncaughtExceptionHandler getThreadPoolExceptionHandler() {
        return threadPoolExceptionHandler;
    }

    void setThreadPoolExceptionHandler(UncaughtExceptionHandler threadPoolExceptionHandler) {
        this.threadPoolExceptionHandler = threadPoolExceptionHandler;
    }

    boolean awaitAllTasksTerminationOnShutdown() {
        return awaitAllTasksTerminationOnShutdown;
    }

    void setAwaitAllTasksTerminationOnShutdown(boolean awaitAllTasksTerminationOnShutdown) {
        this.awaitAllTasksTerminationOnShutdown = awaitAllTasksTerminationOnShutdown;
    }

    long getThreadPoolTerminationTimeoutMillis() {
        return threadPoolTerminationTimeoutMillis;
    }

    void setThreadPoolTerminationTimeoutMillis(long threadPoolTerminationTimeoutMillis) {
        this.threadPoolTerminationTimeoutMillis = threadPoolTerminationTimeoutMillis;
    }
}
