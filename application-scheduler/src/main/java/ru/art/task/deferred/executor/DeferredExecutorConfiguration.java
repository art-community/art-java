/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
