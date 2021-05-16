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

package io.art.logging.factory;

import io.art.logging.configuration.*;
import io.art.logging.logger.*;
import io.art.logging.manager.*;
import lombok.experimental.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.logging.factory.LoggerWriterFactory.*;

@UtilityClass
public class LoggerFactory {
    public static Logger createLogger(String name, LoggerConfiguration configuration, LoggingManager manager) {
        LoggerConstructionConfiguration constructionConfiguration = LoggerConstructionConfiguration.builder()
                .name(name)
                .loggerConfiguration(configuration)
                .writers(configuration
                        .getWriters()
                        .stream()
                        .map(writerConfiguration -> loggerWriter(manager, writerConfiguration))
                        .collect(immutableArrayCollector()))
                .build();
        return new LoggerImplementation(constructionConfiguration, manager.register(constructionConfiguration).getProducer());
    }
}
