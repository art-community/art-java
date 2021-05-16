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
import static io.art.scheduler.constants.SchedulerModuleConstants.Defaults.*;
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

    private final DeferredExecutorImplementation executor;

    private final ThreadPoolExecutor pendingPool;
    private final Thread delayedObserver;
    private final ExecutorService fallbackExecutor = newSingleThreadExecutor(threadFactory);

    private final AtomicBoolean terminating = new AtomicBoolean(false);
    private volatile boolean terminated = false;

    private final DelayQueue<DeferredEvent<?>> delayedEvents = new DelayQueue<>();
    private final Map<Long, PriorityBlockingQueue<DeferredEvent<?>>> pendingEvents = concurrentMap();
    private final PriorityBlockingQueue<Long> pendingIds = new PriorityBlockingQueue<>(INITIAL_RUNNING_QUEUE_SIZE, Long::compare);
    private final PriorityBlockingQueue<Long> takenIds = new PriorityBlockingQueue<>(INITIAL_RUNNING_QUEUE_SIZE, Long::compare);

    private final ReentrantLock takenLock = new ReentrantLock();
    private final ReentrantLock pendingLock = new ReentrantLock();

    DeferredEventObserver(DeferredExecutorImplementation executor) {
        this.executor = executor;
        pendingPool = createThreadPool();
        for (int thread = 0; thread < this.executor.getPoolSize(); thread++) {
            pendingPool.submit(this::observePending);
        }
        delayedObserver = new Thread(this::observeDelayed);
        delayedObserver.start();
    }

    <EventResultType> Future<? extends EventResultType> addEvent(Callable<? extends EventResultType> action, LocalDateTime triggerTime) {
        if (terminated) {
            throw new SchedulerModuleException(SCHEDULER_TERMINATED);
        }

        if (terminating.get()) {
            return cast(EMPTY_FUTURE_TASK);
        }

        FutureTask<? extends EventResultType> task = new FutureTask<>(action);
        DeferredEvent<? extends EventResultType> event = new DeferredEvent<>(task, triggerTime, delayedEvents.size());

        if (delayedEvents.size() + 1 > executor.getQueueSize()) {
            return cast(forceExecuteEvent(event));
        }

        delayedEvents.add(event);
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
                DeferredEvent<?> event = delayedEvents.take();

                erasePendingEvents(event);

                long id = event.getTriggerDateTime();

                pendingLock.lock();
                try {
                    PriorityBlockingQueue<DeferredEvent<?>> queue = pendingEvents.get(id);
                    if (isNull(queue)) {
                        queue = new PriorityBlockingQueue<>(INITIAL_RUNNING_QUEUE_SIZE, comparing(DeferredEvent::getOrder));

                        if (!queue.add(event)) {
                            forceExecuteEvent(event);
                            continue;
                        }

                        pendingEvents.put(id, queue);

                        if (!pendingIds.add(id)) {
                            pendingEvents.remove(id);
                            forceExecuteEvent(event);
                        }

                        continue;
                    }

                    if (!queue.add(event)) {
                        forceExecuteEvent(event);
                    }
                } finally {
                    pendingLock.unlock();
                }
            }
        } catch (InterruptedException ignore) {
            // Ignoring exception because interrupting is normal situation when we want shutdown observer
        } catch (Throwable throwable) {
            executor.getExceptionHandler().onException(currentThread(), TASK_OBSERVING, throwable);
        }
    }

    private void observePending() {
        try {
            while (!terminating.get()) {
                Long id = pendingIds.take();

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
                            task.get();
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
                DEFAULT_POOL_KEEP_ALIVE.toMillis(), MILLISECONDS,
                new LinkedBlockingDeque<>(executor.getQueueSize()),
                threadFactory,
                (runnable, executor) -> this.executor
                        .getExceptionHandler()
                        .onException(currentThread(), TASK_EXECUTION, new SchedulerModuleException(REJECTED_EXCEPTION))
        );
    }

    void clear() {
        delayedEvents.clear();
    }
}
