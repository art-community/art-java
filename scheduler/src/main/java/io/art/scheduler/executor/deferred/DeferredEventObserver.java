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
    private final AtomicBoolean terminating = new AtomicBoolean(false);
    private volatile boolean terminated = false;

    private final ThreadFactory threadFactory;
    private final DeferredExecutorImplementation executor;

    private final ThreadPoolExecutor pendingPool;
    private final Thread delayedObserver;
    private final ExecutorService fallbackExecutor;

    private final DelayQueue<DeferredEvent<?>> delayedEvents;
    private final Map<Long, PriorityBlockingQueue<DeferredEvent<?>>> pendingEvents;
    private final Map<Long, Future<?>> activeWorkers;
    private final Set<Long> waitingWorkers;

    DeferredEventObserver(DeferredExecutorImplementation executor) {
        this.executor = executor;
        int poolNumber = poolCounter.incrementAndGet();
        threadFactory = runnable -> newThread(SCHEDULER_NAME + DASH
                + poolNumber + DASH
                + SCHEDULER_THREAD_NAME + DASH
                + threadCounter.incrementAndGet(), runnable
        );
        pendingPool = createThreadPool();
        delayedEvents = new DelayQueue<>();
        pendingEvents = map(executor.getPendingInitialCapacity());
        activeWorkers = map(executor.getPendingInitialCapacity());
        waitingWorkers = concurrentSet(executor.getPendingInitialCapacity());
        fallbackExecutor = newSingleThreadExecutor(threadFactory);
        delayedObserver = newThread(this::observeDelayed);
        delayedObserver.start();
    }

    <EventResultType> Future<? extends EventResultType> addEvent(Callable<? extends EventResultType> action, LocalDateTime triggerTime, int order) {
        if (terminated) {
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

        if (terminating.get()) {
            return cast(EMPTY_FUTURE_TASK);
        }

        if (!delayedEvents.offer(event)) {
            return cast(forceExecuteEvent(event));
        }

        return task;
    }

    void shutdown() {
        if (terminating.compareAndSet(false, true)) {
            try {
                delayedObserver.interrupt();
                delayedObserver.join();

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
                } catch (InterruptedException | CancellationException ignore) {
                    // Ignoring exception because interrupting is normal situation when we want shutdown observer
                }
                poolCounter.decrementAndGet();
                threadCounter.set(0);
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
                long id = event.getTrigger();
                PriorityBlockingQueue<DeferredEvent<?>> queue = pendingEvents.get(id);
                if (isNull(queue)) {
                    queue = new PriorityBlockingQueue<>(executor.getPendingInitialCapacity(), comparing(DeferredEvent::getOrder));

                    if (!queue.offer(event)) {
                        forceExecuteEvent(event);
                        continue;
                    }

                    pendingEvents.put(id, queue);
                    activeWorkers.put(id, pendingPool.submit(() -> observePending(id)));
                    continue;
                }

                if (!queue.offer(event)) {
                    forceExecuteEvent(event);
                }
            }
        } catch (InterruptedException interruptedException) {
            // Ignoring exception because interrupting is normal situation when we want shutdown observer
        } catch (Throwable throwable) {
            executor.getExceptionHandler().onException(currentThread(), TASK_OBSERVING, throwable);
        } finally {
            if (terminating.get()) {
                finalizeDelayedEvents();
            }
        }
    }

    private <EventResultType> void erasePendingEvents(DeferredEvent<? extends EventResultType> event) {
        List<Long> toRemove = linkedList();
        Set<Entry<Long, PriorityBlockingQueue<DeferredEvent<?>>>> events = setOf(pendingEvents.entrySet());
        for (Entry<Long, PriorityBlockingQueue<DeferredEvent<?>>> entry : events) {
            Long key = entry.getKey();
            if (event.getTrigger() > key && isEmpty(entry.getValue())) {
                toRemove.add(key);
            }
        }
        for (Long id : toRemove) {
            pendingEvents.remove(id);
            Future<?> worker = activeWorkers.remove(id);
            if (waitingWorkers.contains(id)) {
                worker.cancel(true);
                waitingWorkers.remove(id);
            }
        }
    }

    private void finalizeDelayedEvents() {
        DeferredEvent<?> event;
        while (nonNull(event = delayedEvents.poll())) {
            erasePendingEvents(event);
            long id = event.getTrigger();
            PriorityBlockingQueue<DeferredEvent<?>> queue = pendingEvents.get(id);
            if (isNull(queue)) {
                queue = new PriorityBlockingQueue<>(executor.getPendingInitialCapacity(), comparing(DeferredEvent::getOrder));

                if (!queue.offer(event)) {
                    forceExecuteEvent(event);
                    continue;
                }

                pendingEvents.put(id, queue);
                activeWorkers.put(id, pendingPool.submit(() -> observePending(id)));
                continue;
            }

            if (!queue.offer(event)) {
                forceExecuteEvent(event);
            }
        }
    }

    private void observePending(Long id) {
        PriorityBlockingQueue<DeferredEvent<?>> queue;
        try {
            while (!terminating.get() && nonNull(queue = pendingEvents.get(id))) {
                waitingWorkers.add(id);
                DeferredEvent<?> event = queue.take();
                waitingWorkers.remove(id);
                runTask(event);
            }
        } catch (InterruptedException | CancellationException ignore) {
            // Ignoring exception because interrupting is normal situation when we want shutdown observer
        } finally {
            if (terminating.get() && nonNull(queue = pendingEvents.get(id))) {
                finalizePendingEvents(id, queue);
            }
        }
    }

    private void finalizePendingEvents(Long id, PriorityBlockingQueue<DeferredEvent<?>> queue) {
        DeferredEvent<?> event;
        while (nonNull(event = queue.poll())) {
            runTask(event);
        }
        activeWorkers.remove(id);
        waitingWorkers.remove(id);
        pendingEvents.remove(id);
    }

    private void runTask(DeferredEvent<?> event) {
        FutureTask<?> task = getTaskFromEvent(event);
        task.run();
        try {
            task.get(executor.getTaskExecutionTimeout().toMillis(), MILLISECONDS);
        } catch (InterruptedException | CancellationException ignore) {
            // Ignoring exception because interrupting is normal situation when we want shutdown observer
        } catch (Throwable throwable) {
            executor.getExceptionHandler().onException(currentThread(), TASK_EXECUTION, throwable);
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
                new LinkedBlockingQueue<>(executor.getPoolSize() * 2),
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
