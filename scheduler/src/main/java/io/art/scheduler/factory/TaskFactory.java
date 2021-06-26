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

package io.art.scheduler.factory;

import io.art.core.runnable.*;
import io.art.logging.logger.*;
import io.art.scheduler.model.*;
import io.art.scheduler.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.module.LoggingModule.*;
import static java.util.UUID.*;
import java.util.function.*;

@UtilityClass
public class TaskFactory {
    @Getter(lazy = true)
    private final static Logger logger = logger(SchedulerModule.class);

    public static RunnableTask task(ExceptionRunnable runnable) {
        return task(randomUUID().toString(), runnable);
    }

    public static RunnableTask task(String id, ExceptionRunnable runnable) {
        return new RunnableTask(id, taskId -> ignoreException(runnable, TaskFactory::logError));
    }

    public static RunnableTask task(Consumer<String> consumer) {
        return task(randomUUID().toString(), consumer);
    }

    public static RunnableTask task(String id, Consumer<String> consumer) {
        return new RunnableTask(id, consumer);
    }

    private static void logError(Throwable error) {
        getLogger().error(error);
    }
}
