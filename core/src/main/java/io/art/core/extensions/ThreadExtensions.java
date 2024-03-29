/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.core.extensions;

import io.art.core.exception.*;
import lombok.experimental.*;
import static io.art.core.handler.ExceptionHandler.*;
import static java.lang.Thread.*;
import java.util.function.*;

@UtilityClass
public class ThreadExtensions {
    public static void thread(String name, Runnable runnable) {
        newThread(name, runnable).start();
    }

    public static void thread(Runnable runnable) {
        newThread(runnable).start();
    }

    public static Thread newThread(String name, Runnable runnable) {
        return new Thread(runnable, name);
    }

    public static Thread newThread(Runnable runnable) {
        return new Thread(runnable);
    }

    public static Thread newDaemon(String name, Runnable runnable) {
        Thread thread = new Thread(runnable, name);
        thread.setDaemon(true);
        return thread;
    }

    public static Thread newDaemon(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    }

    public static void daemon(String name, Runnable runnable) {
        newDaemon(name, runnable).start();
    }

    public static void daemon(Runnable runnable) {
        newDaemon(runnable).start();
    }

    public static void block() {
        consumeException((Function<Throwable, RuntimeException>) InternalRuntimeException::new)
                .run(() -> currentThread().join());
    }

    public static void sleep(long millis) {
        consumeException((Function<Throwable, RuntimeException>) InternalRuntimeException::new)
                .run(() -> Thread.sleep(millis));
    }
}
