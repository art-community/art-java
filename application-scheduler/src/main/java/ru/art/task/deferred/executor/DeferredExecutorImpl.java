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

import static java.lang.Integer.MAX_VALUE;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static ru.art.task.deferred.executor.DeferredExecutionExceptionMessages.DEFERRED_TASK_IS_NULL;
import static ru.art.task.deferred.executor.DeferredExecutionExceptionMessages.DEFERRED_TASK_TRIGGER_TIME_IS_NULL;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Реализация ключевого компонента проекта. Описание функций: {@link DeferredExecutor}
 */
public class DeferredExecutorImpl implements DeferredExecutor {
    private final ReentrantLock lock = new ReentrantLock();
    private final DeferredEventObserver observer;

    DeferredExecutorImpl(DeferredExecutorConfiguration configuration) {
        observer = new DeferredEventObserver(configuration);
    }

    public static DeferredExecutorBuilder builder() {
        return new DeferredExecutorBuilder();
    }

    @Override
    public <EventResultType> Future<? extends EventResultType> submit(Callable<? extends EventResultType> eventTask, LocalDateTime triggerTime) {
        if (isNull(eventTask)) {
            throw new DeferredExecutionException(DEFERRED_TASK_IS_NULL);
        }
        if (isNull(triggerTime)) {
            throw new DeferredExecutionException(DEFERRED_TASK_TRIGGER_TIME_IS_NULL);
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return observer.addEvent(eventTask, triggerTime);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <EventResultType> Future<? extends EventResultType> submit(Callable<? extends EventResultType> eventTask) {
        return submit(eventTask, now());
    }

    @Override
    public Future<?> execute(Runnable task, LocalDateTime triggerTime) {
        if (isNull(task)) {
            throw new DeferredExecutionException(DEFERRED_TASK_IS_NULL);
        }
        if (isNull(triggerTime)) {
            throw new DeferredExecutionException(DEFERRED_TASK_TRIGGER_TIME_IS_NULL);
        }
        return submit(() -> {
            task.run();
            return null;
        }, triggerTime);
    }

    @Override
    public Future<?> execute(Runnable command) {
        return execute(command, now());
    }

    @Override
    public void shutdown() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            observer.shutdown();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        observer.clear();
    }

    static class DeferredExecutorConstants {
        static final int DEFAULT_MAX_QUEUE_SIZE = MAX_VALUE - 8;
        static final int DEFAULT_SHUTDOWN_TIMEOUT = 60 * 1000;
    }
}