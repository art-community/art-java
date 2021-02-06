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

package io.art.scheduler.executor.deferred;

import static java.lang.Integer.*;
import static java.time.LocalDateTime.*;
import java.time.*;
import java.util.concurrent.*;

public class DeferredExecutorImplementation implements DeferredExecutor {
    private final DeferredEventObserver observer;

    DeferredExecutorImplementation(DeferredExecutorConfiguration configuration) {
        observer = new DeferredEventObserver(configuration);
    }

    @Override
    public <EventResultType> Future<? extends EventResultType> submit(Callable<? extends EventResultType> eventTask, LocalDateTime triggerTime) {
        return observer.addEvent(eventTask, triggerTime);
    }

    @Override
    public <EventResultType> Future<? extends EventResultType> submit(Callable<? extends EventResultType> eventTask) {
        return submit(eventTask, now());
    }

    @Override
    public Future<?> execute(Runnable task, LocalDateTime triggerTime) {
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
        observer.shutdown();
    }

    @Override
    public void clear() {
        observer.clear();
    }
}
