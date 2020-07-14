/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.task.deferred.executor;

import static java.lang.Thread.*;
import static java.util.Objects.*;
import static io.art.core.constants.ThreadConstants.*;

/**
 * Строитель конфигурации для {@link DeferredExecutorImplementation} и {@link DeferredEventObserver}
 */
public class DeferredExecutorBuilder {
    private ExceptionHandler exceptionHandler;
    private UncaughtExceptionHandler threadPoolExceptionHandler;
    private Integer eventsQueueMaxSize;
    private Integer threadPoolCoreSize;
    private Boolean awaitAllTasksTerminationOnShutdown;
    private Long threadPoolTerminationTimeout;

    public DeferredExecutorBuilder withExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public DeferredExecutorBuilder withThreadPoolExceptionHandler(UncaughtExceptionHandler threadPoolExceptionHandler) {
        this.threadPoolExceptionHandler = threadPoolExceptionHandler;
        return this;
    }

    public DeferredExecutorBuilder eventsQueueMaxSize(Integer eventsQueueMaxSize) {
        this.eventsQueueMaxSize = eventsQueueMaxSize;
        return this;
    }

    public DeferredExecutorBuilder threadPoolCoreSize(Integer threadPoolCoreSize) {
        this.threadPoolCoreSize = threadPoolCoreSize;
        return this;
    }

    public DeferredExecutorBuilder awaitAllTasksTerminationOnShutdown(Boolean awaitAllTasksTerminationOnShutdown) {
        this.awaitAllTasksTerminationOnShutdown = awaitAllTasksTerminationOnShutdown;
        return this;
    }

    public DeferredExecutorBuilder threadPoolTerminationTimeout(Long threadPoolTerminationTimeout) {
        this.threadPoolTerminationTimeout = threadPoolTerminationTimeout;
        return this;
    }

    public DeferredExecutor build() {
        DeferredExecutorConfiguration executorConfiguration = new DeferredExecutorConfiguration();
        executorConfiguration.setExceptionHandler(isNull(exceptionHandler) ? new DeferredExecutorExceptionHandler() : exceptionHandler);
        executorConfiguration.setEventsQueueMaxSize(isNull(eventsQueueMaxSize) ? DeferredExecutorImplementation.DeferredExecutorConstants.DEFAULT_MAX_QUEUE_SIZE : eventsQueueMaxSize);
        executorConfiguration.setThreadPoolCoreSize(isNull(threadPoolCoreSize) ? DEFAULT_THREAD_POOL_SIZE : threadPoolCoreSize);
        executorConfiguration.setThreadPoolExceptionHandler(isNull(threadPoolExceptionHandler) ? new DeferredExecutorThreadPoolExceptionHandler() : threadPoolExceptionHandler);
        executorConfiguration.setAwaitAllTasksTerminationOnShutdown(isNull(awaitAllTasksTerminationOnShutdown) ? false : awaitAllTasksTerminationOnShutdown);
        executorConfiguration.setThreadPoolTerminationTimeout(isNull(threadPoolTerminationTimeout) ? DeferredExecutorImplementation.DeferredExecutorConstants.DEFAULT_SHUTDOWN_TIMEOUT : threadPoolTerminationTimeout);
        return new DeferredExecutorImplementation(executorConfiguration);
    }
}
