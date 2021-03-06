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
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
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
import java.util.Map.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;


class DeferredEventObserver {
    private final static AtomicInteger poolCounter = new AtomicInteger(0);
    private final AtomicInteger threadCounter = new AtomicInteger(0);
    private final AtomicBoolean receiving = new AtomicBoolean(true);
    private final AtomicBoolean executing = new AtomicBoolean(true);

    private final ThreadFactory threadFactory;
    private final DeferredExecutorImplementation executor;

    private final ThreadPoolExecutor pendingPool;
    private final Thread delayedObserver;
    private final ExecutorService fallbackExecutor;

    private final DelayQueue<DeferredEvent<?>> delayedEvents;
    private final Map<Long, PriorityBlockingQueue<DeferredEvent<?>>> pendingQueues;
    private final Map<Long, Future<?>> activeWorkers;
    private final Set<Long> waitingWorkers;

    DeferredEventObserver(DeferredExecutorImplementation executor) {
        this.executor = executor;
        int poolNumber = poolCounter.incrementAndGet();
        threadFactory = runnable -> newDaemon(SCHEDULER_NAME + DASH
                + poolNumber + DASH
                + SCHEDULER_THREAD_NAME + DASH
                + threadCounter.incrementAndGet(), runnable
        );
        pendingPool = createThreadPool();
        delayedEvents = new DelayQueue<>();
        pendingQueues = concurrentMap(executor.getPendingInitialCapacity());
        activeWorkers = map(executor.getPendingInitialCapacity());
        waitingWorkers = concurrentSet(executor.getPendingInitialCapacity());
        fallbackExecutor = newSingleThreadExecutor(threadFactory);
        delayedObserver = newDaemon(this::observeDelayed);
        delayedObserver.start();
    }

    <EventResultType> Future<? extends EventResultType> addEvent(Callable<? extends EventResultType> action, LocalDateTime triggerTime, int order) {
        if (!receiving.get()) {
            throw new SchedulerModuleException(SCHEDULER_TERMINATED);
        }

        Callable<? extends EventResultType> wrapper = () -> {
            try {
                return action.call();
            } catch (Throwable throwable) {
                executor.getExceptionHandler().onException(currentThread(), TASK_EXECUTION, throwable);
                throw throwable;
            }
        };
        FutureTask<? extends EventResultType> task = new FutureTask<>(wrapper);
        DeferredEvent<? extends EventResultType> event = new DeferredEvent<>(task, triggerTime, order);

        if (delayedEvents.size() + 1 >= executor.getDelayedQueueMaximumCapacity()) {
            return cast(forceExecuteEvent(event));
        }

        delayedEvents.offer(event);

        return task;
    }


    private void observeDelayed() {
        try {
            while (receiving.get()) {
                DeferredEvent<?> event = delayedEvents.take();
                erasePendingQueues(event);
                long id = event.getTrigger();
                PriorityBlockingQueue<DeferredEvent<?>> queue = pendingQueues.get(id);
                if (isNull(queue)) {
                    queue = new PriorityBlockingQueue<>(executor.getPendingInitialCapacity(), comparing(DeferredEvent::getOrder));
                    queue.offer(event);
                    pendingQueues.put(id, queue);
                    activeWorkers.put(id, pendingPool.submit(() -> observePending(id)));
                    continue;
                }

                if (queue.size() + 1 >= executor.getPendingQueueMaximumCapacity()) {
                    cast(forceExecuteEvent(event));
                    continue;
                }

                queue.offer(event);
            }
        } catch (InterruptedException interruptedException) {
            // Ignoring exception because interrupting is normal situation when we want shutdown observer
        } catch (Throwable throwable) {
            executor.getExceptionHandler().onException(currentThread(), TASK_OBSERVING, throwable);
        } finally {
            if (!receiving.get()) {
                finalizeDelayedEvents();
            }
        }
    }

    private void finalizeDelayedEvents() {
        DeferredEvent<?> event;
        while (nonNull(event = delayedEvents.poll())) {
            erasePendingQueues(event);
            long id = event.getTrigger();
            PriorityBlockingQueue<DeferredEvent<?>> queue = pendingQueues.get(id);
            if (isNull(queue)) {
                queue = new PriorityBlockingQueue<>(executor.getPendingInitialCapacity(), comparing(DeferredEvent::getOrder));
                queue.offer(event);
                pendingQueues.put(id, queue);
                activeWorkers.put(id, pendingPool.submit(() -> observePending(id)));
                continue;
            }

            if (queue.size() + 1 >= executor.getPendingQueueMaximumCapacity()) {
                cast(forceExecuteEvent(event));
                continue;
            }

            queue.offer(event);
        }
    }

    private <EventResultType> void erasePendingQueues(DeferredEvent<? extends EventResultType> event) {
        List<Long> toRemove = linkedList();
        Set<Entry<Long, PriorityBlockingQueue<DeferredEvent<?>>>> events = setOf(pendingQueues.entrySet());
        for (Entry<Long, PriorityBlockingQueue<DeferredEvent<?>>> entry : events) {
            Long trigger = entry.getKey();
            PriorityBlockingQueue<DeferredEvent<?>> pendingQueue = entry.getValue();
            if (event.getTrigger() > trigger && isEmpty(pendingQueue)) {
                toRemove.add(trigger);
            }
        }
        for (Long id : toRemove) {
            pendingQueues.remove(id);
            Future<?> worker = activeWorkers.remove(id);
            if (waitingWorkers.contains(id)) {
                worker.cancel(true);
                waitingWorkers.remove(id);
            }
        }
    }

    private void observePending(Long id) {
        PriorityBlockingQueue<DeferredEvent<?>> queue;
        try {
            while (executing.get() && nonNull(queue = pendingQueues.get(id))) {
                waitingWorkers.add(id);
                DeferredEvent<?> event = queue.take();
                waitingWorkers.remove(id);
                runTask(event);
            }
        } catch (InterruptedException | CancellationException interruptedException) {
            // Ignoring exception because interrupting is normal situation when we want shutdown observer
        } finally {
            if (!executing.get() && nonNull(queue = pendingQueues.get(id))) {
                finalizePendingEvents(queue);
            }
        }
    }

    private void finalizePendingEvents(PriorityBlockingQueue<DeferredEvent<?>> queue) {
        DeferredEvent<?> event;
        while (nonNull(event = queue.poll())) {
            runTask(event);
        }
    }

    private void runTask(DeferredEvent<?> event) {
        FutureTask<?> task = getTaskFromEvent(event);
        task.run();
        for (; ; ) {
            try {
                task.get(executor.getTaskExecutionTimeout().toMillis(), MILLISECONDS);
                break;
            } catch (InterruptedException | CancellationException interruptedException) {
                // Ignoring exception because interrupting is normal situation when we want shutdown observer
            } catch (Throwable throwable) {
                executor.getExceptionHandler().onException(currentThread(), TASK_EXECUTION, throwable);
                break;
            }
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
                0L, MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory,
                (runnable, executor) -> this.executor
                        .getExceptionHandler()
                        .onException(currentThread(), TASK_EXECUTION, new SchedulerModuleException(REJECTED_EXCEPTION))
        );
    }


    void shutdown() {
        if (receiving.compareAndSet(true, false)) {
            try {
                delayedObserver.interrupt();
                delayedObserver.join();

                if (executing.compareAndSet(true, false)) {
                    for (Long id : waitingWorkers) {
                        activeWorkers.get(id).cancel(true);
                    }

                    pendingPool.shutdown();
                    fallbackExecutor.shutdown();

                    if (!executor.isAwaitOnShutdown()) {
                        poolCounter.decrementAndGet();
                        threadCounter.set(0);
                        return;
                    }

                    try {
                        if (!pendingPool.awaitTermination(executor.getPoolTerminationTimeout().getSeconds(), SECONDS)) {
                            executor.getExceptionHandler().onException(currentThread(), POOL_SHUTDOWN, new SchedulerModuleException(AWAIT_TERMINATION_EXCEPTION));
                        }

                        if (!fallbackExecutor.awaitTermination(executor.getPoolTerminationTimeout().getSeconds(), SECONDS)) {
                            executor.getExceptionHandler().onException(currentThread(), POOL_SHUTDOWN, new SchedulerModuleException(AWAIT_TERMINATION_EXCEPTION));
                        }

                        pendingQueues.clear();
                        activeWorkers.clear();
                        waitingWorkers.clear();
                    } catch (InterruptedException | CancellationException interruptedException) {
                        // Ignoring exception because interrupting is normal situation when we want shutdown observer
                    }
                    poolCounter.decrementAndGet();
                    threadCounter.set(0);
                }
            } catch (Throwable throwable) {
                executor.getExceptionHandler().onException(currentThread(), POOL_SHUTDOWN, throwable);
            }
        }
    }

    void clear() {
        delayedEvents.clear();
    }
}
