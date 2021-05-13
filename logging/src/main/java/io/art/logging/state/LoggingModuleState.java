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

package io.art.logging.state;

import io.art.core.module.*;
import io.art.logging.logger.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

public class LoggingModuleState implements ModuleState {
    private final List<LoggerProcessor> loggers = copyOnWriteList();
    private final Map<String, Logger> cache = concurrentMap();
    private final List<Closeable> resources = linkedList();

    public Logger cached(String name, Supplier<Logger> logger) {
        return putIfAbsent(cache, name, logger);
    }

    public LoggerProcessor register(LoggerImplementation implementation) {
        LoggerProcessor processor = new LoggerProcessor(implementation);
        loggers.add(processor);
        return processor;
    }

    public void register(Closeable resource) {
        resources.add(resource);
    }

    public void remove(Closeable resource) {
        resources.remove(resource);
    }

    public void forEach(Consumer<LoggerProcessor> consumer) {
        loggers.forEach(consumer);
    }

    public boolean all(Predicate<LoggerProcessor> predicate) {
        return loggers.stream().allMatch(predicate);
    }

    public void close() {
        resources.forEach(resource -> ignoreException(resource::close));
    }
}
