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
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.ExceptionMessages.*;
import static io.art.scheduler.constants.SchedulerModuleConstants.ExceptionMessages.ExceptionEvent.*;
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
    private final static AtomicInteger threadCounter = new AtomicInteger(0);
    private final static ThreadFactory threadFactory = runnable -> newDaemon(SCHEDULER_THREAD_NAME + DASH + threadCounter.incrementAndGet(), runnable);

    private final AtomicBoolean terminating = new AtomicBoolean(false);
    private volatile boolean terminated = false;
    private final ReentrantLock takenLock = new ReentrantLock();
    private final ReentrantLock pendingLock = new ReentrantLock();

    private final DeferredExecutorImplementation executor;
    private final ThreadPoolExecutor pendingPool;
    private final Thread delayedObserver;
    private final ExecutorService fallbackExecutor;
    private final DelayQueue<DeferredEvent<?>> delayedEvents;
    private final Map<Long, PriorityBlockingQueue<DeferredEvent<?>>> pendingEvents;
    private final PriorityBlockingQueue<Long> pendingIds;
    private final PriorityBlockingQueue<Long> takenIds;

    DeferredEventObserver(DeferredExecutorImplementation executor) {
        this.executor = executor;
        pendingPool = createThreadPool();
        delayedEvents = new DelayQueue<>();
        takenIds = new PriorityBlockingQueue<>(executor.getPendingQueueInitialCapacity(), Long::compare);
        pendingIds = new PriorityBlockingQueue<>(executor.getPendingQueueInitialCapacity(), Long::compare);
        pendingEvents = concurrentMap(executor.getPendingQueueInitialCapacity());
        fallbackExecutor = newSingleThreadExecutor(threadFactory);
        for (int thread = 0; thread < this.executor.getPoolSize(); thread++) {
            pendingPool.submit(this::observePending);
        }
        delayedObserver = newDaemon(this::observeDelayed);
        delayedObserver.start();
    }

    <EventResultType> Future<? extends EventResultType> addEvent(Callable<? extends EventResultType> action, LocalDateTime triggerTime, int order) {
        if (terminated) {
            throw new SchedulerModuleException(SCHEDULER_TERMINATED);
        }

        if (terminating.get()) {
            return cast(EMPTY_FUTURE_TASK);
        }

        FutureTask<? extends EventResultType> task = new FutureTask<>(action);
        DeferredEvent<? extends EventResultType> event = new DeferredEvent<>(task, triggerTime, order);

        if (delayedEvents.size() + 1 > executor.getDelayedQueueSize()) {
            return cast(forceExecuteEvent(event));
        }

        if (!delayedEvents.offer(event)) {
            return cast(forceExecuteEvent(event));
        }

        return task;
    }

    void shutdown() {
        if (terminating.compareAndSet(false, true)) {
            try {

                delayedEvents.clear();
                pendingEvents.clear();

                pendingIds.clear();
                takenIds.clear();

                delayedObserver.interrupt();
                pendingPool.shutdown();
                fallbackExecutor.shutdownNow();

                if (!executor.isAwaitOnShutdown()) {
                    return;
                }

                if (!pendingPool.awaitTermination(executor.getPoolTerminationTimeout().getSeconds(), SECONDS)) {
                    executor.getExceptionHandler().onException(currentThread(), POOL_SHUTDOWN, new SchedulerModuleException(AWAIT_TERMINATION_EXCEPTION));
                }

                if (!fallbackExecutor.awaitTermination(executor.getPoolTerminationTimeout().getSeconds(), SECONDS)) {
                    executor.getExceptionHandler().onException(currentThread(), POOL_SHUTDOWN, new SchedulerModuleException(AWAIT_TERMINATION_EXCEPTION));
                }
            } catch (Throwable throwable) {
                executor.getExceptionHandler().onException(currentThread(), POOL_SHUTDOWN, throwable);
            } finally {
                terminated = true;
            }
        }
    }

    private void observeDelayed() {
        try {
            while (!terminating.get()) {
                DeferredEvent<?> event = poll(delayedEvents);
                if (isNull(event)) return;

                erasePendingEvents(event);

                long id = event.getTriggerDateTime();

                pendingLock.lock();
                try {
                    PriorityBlockingQueue<DeferredEvent<?>> queue = pendingEvents.get(id);
                    if (isNull(queue)) {
                        queue = new PriorityBlockingQueue<>(executor.getPendingQueueInitialCapacity(), comparing(DeferredEvent::getOrder));

                        if (!queue.offer(event)) {
                            forceExecuteEvent(event);
                            continue;
                        }

                        pendingEvents.put(id, queue);

                        if (!pendingIds.offer(id)) {
                            pendingEvents.remove(id);
                            forceExecuteEvent(event);
                        }

                        continue;
                    }

                    if (!queue.offer(event)) {
                        forceExecuteEvent(event);
                    }
                } finally {
                    pendingLock.unlock();
                }
            }
        } catch (Throwable throwable) {
            executor.getExceptionHandler().onException(currentThread(), TASK_OBSERVING, throwable);
        }
    }

    private void observePending() {
        try {
            while (!terminating.get()) {
                Long id = poll(pendingIds);
                if (isNull(id)) return;

                takenLock.lock();
                try {
                    if (!takenIds.offer(id)) {
                        continue;
                    }
                } finally {
                    takenLock.unlock();
                }

                PriorityBlockingQueue<DeferredEvent<?>> events;
                while (nonNull(events = pendingEvents.get(id))) {
                    pendingLock.lock();
                    try {
                        DeferredEvent<?> event;
                        while (nonNull(event = events.poll())) {
                            FutureTask<?> task = getTaskFromEvent(event);
                            task.run();
                            task.get(executor.getTaskExecutionTimeout().toMillis(), MILLISECONDS);
                        }
                    } finally {
                        pendingLock.unlock();
                    }
                }
            }
        } catch (InterruptedException ignore) {
            // Ignoring exception because interrupting is normal situation when we want shutdown observer
        } catch (Throwable throwable) {
            executor.getExceptionHandler().onException(currentThread(), TASK_EXECUTION, throwable);
        }
    }

    private <EventResultType> void erasePendingEvents(DeferredEvent<? extends EventResultType> event) {
        takenLock.lock();
        pendingLock.lock();
        try {
            Long currentId;
            while (nonNull(currentId = takenIds.peek())) {

                if (event.getTriggerDateTime() > currentId && isEmpty(pendingEvents.get(currentId))) {
                    pendingEvents.remove(currentId);
                    takenIds.poll();
                    continue;
                }

                return;
            }
        } catch (Throwable throwable) {
            executor.getExceptionHandler().onException(currentThread(), TASK_OBSERVING, throwable);
        } finally {
            pendingLock.unlock();
            takenLock.unlock();
        }
    }

    private FutureTask<?> getTaskFromEvent(DeferredEvent<?> currentEvent) {
        return (FutureTask<?>) currentEvent.getTask();
    }

    private Future<?> forceExecuteEvent(DeferredEvent<?> event) {
        return fallbackExecutor.submit(getTaskFromEvent(event));
    }

    private ThreadPoolExecutor createThreadPool() {
        return new ThreadPoolExecutor(
                executor.getPoolSize(),
                executor.getPoolSize(),
                executor.getPoolKeepAlive().toMillis(), MILLISECONDS,
                new LinkedBlockingQueue<>(executor.getPoolSize()),
                threadFactory,
                (runnable, executor) -> this.executor
                        .getExceptionHandler()
                        .onException(currentThread(), TASK_EXECUTION, new SchedulerModuleException(REJECTED_EXCEPTION))
        );
    }


    private <T> T poll(Queue<T> queue) {
        for (; ; ) {
            if (terminating.get()) return null;
            T element;
            if (nonNull(element = queue.poll())) return element;
        }
    }

    void clear() {
        delayedEvents.clear();
    }
}
