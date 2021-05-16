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

import io.art.scheduler.exception.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.ExceptionMessages.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.ExceptionMessages.ExceptionEvent.*;
import static java.lang.Thread.*;
import static java.util.Objects.*;
import static java.util.concurrent.ForkJoinPool.*;
import static java.util.concurrent.TimeUnit.*;
import java.time.*;
import java.util.concurrent.*;

//TODO: FIXME - Observer working incorrectly. Will be fixed later...
class DeferredEventObserver {
    private final DeferredExecutorImplementation implementation;
    private final ForkJoinPool threadPool;
    private final DelayQueue<DeferredEvent<?>> deferredEvents;
    private final ForkJoinTask<?> observingTask;
    private volatile boolean terminating = false;
    private final ForkJoinTask<?> EMPTY_TASK = new Task<>(emptyRunnable());

    DeferredEventObserver(DeferredExecutorImplementation implementation) {
        this.implementation = implementation;
        deferredEvents = new DelayQueue<>();
        threadPool = createThreadPool();
        observingTask = threadPool.submit(new Task<>(this::observeQueue));
    }

    <EventResultType> ForkJoinTask<? extends EventResultType> addEvent(Callable<? extends EventResultType> task, LocalDateTime triggerTime) {
        if (terminating) {
            return cast(EMPTY_TASK);
        }

        if (deferredEvents.size() + 1 > implementation.getQueueSize()) {
            return forceExecuteEvent(task);
        }

        ForkJoinTask<EventResultType> forkJoinTask = new Task<>(task);
        DeferredEvent<EventResultType> event = new DeferredEvent<>(forkJoinTask, triggerTime, deferredEvents.size());
        deferredEvents.add(event);
        return forkJoinTask;
    }

    void shutdown() {
        try {
            terminating = true;
            observingTask.cancel(true);
            deferredEvents.clear();
            threadPool.shutdown();
            if (!implementation.isAwaitOnShutdown()) {
                return;
            }
            if (!threadPool.awaitTermination(implementation.getPoolTerminationTimeout().getSeconds(), SECONDS)) {
                implementation.getExceptionHandler().onException(currentThread(), POOL_SHUTDOWN, new SchedulerModuleException(AWAIT_TERMINATION_EXCEPTION));
            }
        } catch (Throwable throwable) {
            implementation.getExceptionHandler().onException(currentThread(), POOL_SHUTDOWN, throwable);
        } finally {
            terminating = false;
        }
    }

    @SuppressWarnings(CONSTANT_CONDITIONS)
    private void observeQueue() {
        try {
            while (!terminating) {
                DeferredEvent<?> currentEvent = deferredEvents.take();
                try {
                    ForkJoinTask<?> task = getTaskFromEvent(currentEvent);
                    if (task.isCancelled()) continue;
                    task = task.fork();
                    DeferredEvent<?> nextEvent;
                    if (nonNull(nextEvent = deferredEvents.peek()) && nextEvent.getTriggerDateTime() == currentEvent.getTriggerDateTime() && !task.isCancelled()) {
                        task.join();
                    }
                } catch (Throwable throwable) {
                    implementation.getExceptionHandler().onException(currentThread(), TASK_EXECUTION, throwable);
                }
            }
        } catch (InterruptedException ignore) {
            // Ignoring exception because interrupting is normal situation when we want shutdown observer
        } catch (Throwable throwable) {
            implementation.getExceptionHandler().onException(currentThread(), TASK_OBSERVING, throwable);
        }
    }

    private ForkJoinTask<?> getTaskFromEvent(DeferredEvent<?> currentEvent) {
        return (ForkJoinTask<?>) currentEvent.getTask();
    }

    private ForkJoinPool createThreadPool() {
        UncaughtExceptionHandler exceptionHandler = (thread, throwable) -> implementation
                .getExceptionHandler()
                .onException(thread, TASK_EXECUTION, throwable);
        return new ForkJoinPool(implementation.getPoolSize() + 1, defaultForkJoinWorkerThreadFactory, exceptionHandler, true);
    }

    private <EventResultType> ForkJoinTask<? extends EventResultType> forceExecuteEvent(Callable<? extends EventResultType> task) {
        return threadPool.submit(task);
    }

    void clear() {
        deferredEvents.clear();
    }

    private final class Task<T> extends ForkJoinTask<T> {
        final Callable<? extends T> callable;
        volatile Thread executionThread;
        T result;

        Task(Callable<? extends T> callable) {
            this.callable = callable;
        }

        Task(Runnable runnable) {
            this.callable = () -> {
                runnable.run();
                return null;
            };
        }

        @Override
        public final T getRawResult() {
            return result;
        }

        @Override
        public final void setRawResult(T value) {
            result = value;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            boolean cancel = super.cancel(mayInterruptIfRunning);
            if (mayInterruptIfRunning && nonNull(executionThread)) {
                executionThread.interrupt();
            }
            return cancel;
        }

        @Override
        public final boolean exec() {
            try {
                executionThread = currentThread();
                result = callable.call();
            } catch (Throwable throwable) {
                DeferredEventObserver.this
                        .implementation
                        .getExceptionHandler()
                        .onException(currentThread(), TASK_EXECUTION, throwable);
            }
            return true;
        }
    }
}
