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

package io.art.core.thread;

import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.Objects.*;
import javax.annotation.*;
import java.lang.Thread.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@Getter
@Builder
public class ConfigurableThreadFactory implements ThreadFactory, Supplier<String>, UncaughtExceptionHandler {
    private final String name;
    private final boolean daemon;
    private final BiConsumer<Thread, Throwable> uncaughtExceptionHandler;

    @Builder.Default
    private final AtomicLong counterReference = new AtomicLong();

    @Override
    public final Thread newThread(@Nonnull Runnable runnable) {
        String newThreadName = name + DASH + counterReference.incrementAndGet();
        Thread thread = new Thread(runnable, newThreadName);
        if (daemon) {
            thread.setDaemon(true);
        }
        if (nonNull(uncaughtExceptionHandler)) {
            thread.setUncaughtExceptionHandler(this);
        }
        return thread;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        apply(uncaughtExceptionHandler, handler -> handler.accept(thread, exception));
    }

    @Override
    public final String get() {
        return name;
    }

    public ConfigurableThreadFactory threadFactory(String name) {
        return ConfigurableThreadFactory.builder()
                .name(name)
                .build();
    }

    public ConfigurableThreadFactory daemonsThreadFactory(String name) {
        return ConfigurableThreadFactory.builder()
                .name(name)
                .daemon(true)
                .build();
    }
}
