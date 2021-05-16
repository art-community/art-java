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

package io.art.logging.manager;

import io.art.core.extensions.*;
import io.art.logging.configuration.*;
import io.art.logging.state.*;
import lombok.*;
import static io.art.core.extensions.ExecutorExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.PairFactory.*;
import static java.lang.Thread.*;
import static java.util.Comparator.*;
import static java.util.Objects.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@RequiredArgsConstructor
public class LoggingManager {
    private final LoggingModuleConfiguration configuration;
    private final List<LoggerProcessor> processors = copyOnWriteList();
    private final List<Closeable> resources = linkedList();

    private final AtomicBoolean activated = new AtomicBoolean(false);

    public boolean isActivated() {
        return activated.get();
    }

    public void activate() {
        if (activated.compareAndSet(false, true)) {
            configuration.getConsumingExecutor().execute(this::processConsuming);
        }
    }

    public void deactivate() {
        if (activated.compareAndSet(true, false)) {
            terminateQuietly(configuration.getConsumingExecutor());
            resources.forEach(StreamsExtensions::closeQuietly);
        }
    }

    public LoggerProcessor register(LoggerConstructionConfiguration configuration) {
        LoggerProcessor processor = new LoggerProcessor(this, configuration.getWriters());
        processors.add(processor);
        return processor;
    }

    public void register(Closeable resource) {
        resources.add(resource);
    }

    public void remove(Closeable resource) {
        resources.remove(resource);
    }

    private void processConsuming() {
        while (!interrupted() && activated.get()) {
            processors
                    .stream()
                    .map(processor -> pairOf(processor, processor.getQueue().poll()))
                    .filter(processor -> nonNull(processor.getSecond()))
                    .sorted(comparing(processor -> processor.getSecond().getDateTime()))
                    .forEach(processor -> processor.getFirst().getConsumer().consume(processor.getSecond()));

        }
        while (!processors.stream().allMatch(processor -> processor.getQueue().isEmpty())) {
            processors
                    .stream()
                    .map(processor -> pairOf(processor, processor.getQueue().poll()))
                    .filter(processor -> nonNull(processor.getSecond()))
                    .sorted(comparing(processor -> processor.getSecond().getDateTime()))
                    .forEach(processor -> processor.getFirst().getConsumer().consume(processor.getSecond()));

        }
    }
}
