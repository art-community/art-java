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
import io.art.scheduler.queue.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.Errors.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.Errors.ExceptionEvent.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.*;
import static java.lang.Thread.*;
import static java.util.Comparator.*;
import static java.util.Objects.*;
import static java.util.concurrent.Executors.*;
import static java.util.concurrent.TimeUnit.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;


class DeferredEventObserver {
    private final static AtomicInteger poolCounter = new AtomicInteger(0);
    private final AtomicInteger threadCounter = new AtomicInteger(0);
    private final AtomicBoolean accepting = new AtomicBoolean(true);
    private final AtomicBoolean executing = new AtomicBoolean(true);
    private final ReentrantLock pendingLock = new ReentrantLock();

    private final DeferredExecutorImplementation executor;
    private final ThreadPoolExecutor pendingPool;
    private final Thread delayedObserver;
    private final ExecutorService fallbackPool;
    private final DelayWaitingQueue<DeferredEvent<?>> delayedEvents;
    private final Map<Long, PriorityWaitingQueue<DeferredEvent<?>>> pendingQueues;

    DeferredEventObserver(DeferredExecutorImplementation executor) {
        this.executor = executor;
        int poolNumber = poolCounter.incrementAndGet();
        ThreadFactory threadFactory = runnable -> newDaemon(SCHEDULER_NAME + DASH
                + poolNumber + DASH
                + SCHEDULER_THREAD_NAME + DASH
                + threadCounter.incrementAndGet(), runnable
        );
        pendingPool = createThreadPool(threadFactory);
        delayedEvents = new DelayWaitingQueue<>(executor.getMaximumCapacity(), this::offerPendingEvent);
        pendingQueues = concurrentMap(executor.getInitialCapacity());
        fallbackPool = newSingleThreadExecutor(threadFactory);
        delayedObserver = newDaemon(this::observeDelayed);
        delayedObserver.start();
    }

    <EventResultType> Future<? extends EventResultType> addEvent(Callable<? extends EventResultType> action, LocalDateTime triggerTime, int order) {
        if (!accepting.get()) {
            throw new SchedulerModuleException(SCHEDULER_TERMINATED);
        }

        Callable<? extends EventResultType> wrapper = () -> call(action);
        FutureTask<? extends EventResultType> task = new FutureTask<>(wrapper);
        DeferredEvent<? extends EventResultType> event = new DeferredEvent<>(task, triggerTime, order);

        if (!delayedEvents.offer(event)) {
            return cast(forceExecuteEvent(event));
        }

        return task;
    }

    private <EventResultType> EventResultType call(Callable<? extends EventResultType> action) throws Exception {
        try {
            return action.call();
        } catch (Throwable throwable) {
            executor.getExceptionHandler().onException(currentThread(), TASK_EXECUTION, throwable);
            throw throwable;
        }
    }

    private void observeDelayed() {
        try {
            while (accepting.get()) {
                DeferredEvent<?> event = delayedEvents.take();
                if (isNull(event)) continue;
                offerPendingEvent(event);
            }
        } catch (Throwable throwable) {
            executor.getExceptionHandler().onException(currentThread(), TASK_OBSERVING, throwable);
        } finally {
            if (!accepting.get()) {
                delayedEvents.erase();
            }
        }
    }

    private void offerPendingEvent(DeferredEvent<?> event) {
        long id = event.getTrigger();
        final ReentrantLock pendingLock = this.pendingLock;
        pendingLock.lock();
        try {
            PriorityWaitingQueue<DeferredEvent<?>> queue = pendingQueues.get(id);
            if (isNull(queue)) {
                if (pendingQueues.size() + 1 > executor.getMaximumCapacity()) {
                    cast(forceExecuteEvent(event));
                    return;
                }
                queue = new PriorityWaitingQueue<>(executor.getMaximumCapacity(), this::runTask, comparing(DeferredEvent::getOrder));
                queue.offer(event);
                pendingQueues.put(id, queue);
                pendingPool.submit(() -> observePending(id));
                return;
            }
            if (!queue.offer(event)) {
                cast(forceExecuteEvent(event));
            }
        } finally {
            pendingLock.unlock();
        }
    }

    private void observePending(Long id) {
        PriorityWaitingQueue<DeferredEvent<?>> queue;
        try {
            while (executing.get() && nonNull(queue = pendingQueues.get(id))) {
                apply(queue.take(), this::runTask);
            }
        } finally {
            if (!executing.get() && nonNull(queue = pendingQueues.get(id))) {
                queue.erase();
            }
        }
    }

    private void runTask(DeferredEvent<?> event) {
        FutureTask<?> task = getTaskFromEvent(event);
        task.run();
        try {
            task.get(executor.getTaskExecutionTimeout().toMillis(), MILLISECONDS);
        } catch (Throwable throwable) {
            executor.getExceptionHandler().onException(currentThread(), TASK_EXECUTION, throwable);
        } finally {
            removePendingQueue(event);
        }
    }

    private void removePendingQueue(DeferredEvent<?> event) {
        final ReentrantLock pendingLock = this.pendingLock;
        pendingLock.lock();
        try {
            PriorityWaitingQueue<DeferredEvent<?>> queue = pendingQueues.get(event.getTrigger());
            if (nonNull(queue) && queue.isEmpty()) {
                pendingQueues.remove(event.getTrigger());
                queue.terminate();
            }
        } finally {
            pendingLock.unlock();
        }
    }

    private FutureTask<?> getTaskFromEvent(DeferredEvent<?> currentEvent) {
        return (FutureTask<?>) currentEvent.getTask();
    }

    private Future<?> forceExecuteEvent(DeferredEvent<?> event) {
        return fallbackPool.submit(getTaskFromEvent(event));
    }

    private ThreadPoolExecutor createThreadPool(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(
                executor.getPoolSize(),
                executor.getPoolSize(),
                0L, MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory,
                (runnable, executor) -> this.executor
                        .getExceptionHandler()
                        .onException(currentThread(), TASK_EXECUTION, new SchedulerModuleException(REJECTED_EXCEPTION))
        );
    }

    void shutdown() {
        if (accepting.compareAndSet(true, false)) {
            ExceptionHandler exceptionHandler = executor.getExceptionHandler();
            try {
                delayedEvents.terminate();
                delayedObserver.join();

                if (executing.compareAndSet(true, false)) {

                    for (PriorityWaitingQueue<DeferredEvent<?>> queue : pendingQueues.values()) {
                        queue.terminate();
                    }

                    pendingPool.shutdown();
                    fallbackPool.shutdown();

                    if (!pendingPool.awaitTermination(executor.getPoolTerminationTimeout().getSeconds(), SECONDS)) {
                        exceptionHandler.onException(currentThread(), POOL_SHUTDOWN, new SchedulerModuleException(AWAIT_TERMINATION_EXCEPTION));
                    }

                    if (!fallbackPool.awaitTermination(executor.getPoolTerminationTimeout().getSeconds(), SECONDS)) {
                        exceptionHandler.onException(currentThread(), POOL_SHUTDOWN, new SchedulerModuleException(AWAIT_TERMINATION_EXCEPTION));
                    }

                    pendingQueues.clear();
                    poolCounter.decrementAndGet();
                    threadCounter.set(0);
                }
            } catch (Throwable throwable) {
                exceptionHandler.onException(currentThread(), POOL_SHUTDOWN, throwable);
            }
        }
    }
}
