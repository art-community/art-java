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

import io.art.logging.configuration.*;
import io.art.logging.logger.*;
import io.art.logging.messaging.*;
import io.art.logging.writer.*;
import lombok.*;
import static io.art.logging.factory.LoggerWriterFactory.*;
import static io.art.logging.module.LoggingModule.*;


@Getter
public class LoggerState {
    private final LoggerConsumer consumer;
    private final LoggerProducer producer;

    public LoggerState(LoggerImplementation logger) {
        LoggingModuleConfiguration moduleConfiguration = loggingModule().configuration();
        LoggingQueue queue = new LoggingQueue();
        consumer = new LoggerConsumer(queue, logger.getWriters());

        LoggerWriter fallbackWriter = loggerWriter(moduleConfiguration.getDefaultLogger().toLoggerConfiguration(), moduleConfiguration.getDefaultLogger().getWriter());
        producer = new LoggerProducer(queue, moduleConfiguration.getProducingExecutor(), fallbackWriter);
    }
}
