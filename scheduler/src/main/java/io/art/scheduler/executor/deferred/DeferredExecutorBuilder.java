/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.scheduler.executor.deferred;

import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.ThreadConstants.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.Defaults.*;
import static java.lang.Thread.*;
import static java.util.Objects.*;

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
        return new DeferredExecutorImplementation(DeferredExecutorConfiguration.builder()
                .exceptionHandler(orElse(exceptionHandler, new DeferredExecutorExceptionHandler()))
                .eventsQueueMaxSize(orElse(eventsQueueMaxSize, DEFAULT_MAX_QUEUE_SIZE))
                .threadPoolCoreSize(orElse(threadPoolCoreSize, DEFAULT_THREAD_POOL_SIZE))
                .threadPoolExceptionHandler(orElse(threadPoolExceptionHandler, new DeferredExecutorUncaughtExceptionHandler()))
                .awaitAllTasksTerminationOnShutdown(nonNull(awaitAllTasksTerminationOnShutdown) && awaitAllTasksTerminationOnShutdown)
                .threadPoolTerminationTimeout(orElse(threadPoolTerminationTimeout, DEFAULT_SHUTDOWN_TIMEOUT))
                .build());
    }
}
