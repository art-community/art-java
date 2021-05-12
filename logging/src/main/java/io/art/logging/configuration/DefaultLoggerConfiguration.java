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

package io.art.logging.configuration;

import io.art.core.factory.*;
import io.art.core.source.*;
import io.art.logging.constants.*;
import lombok.*;
import static io.art.logging.constants.LoggingLevel.*;
import static io.art.logging.constants.LoggingModuleConstants.ConfigurationKeys.*;

@Getter
@Builder(toBuilder = true)
public class DefaultLoggerConfiguration {
    private final LoggingLevel level;

    private final LoggerWriterConfiguration writer;

    @Builder.Default
    private final Boolean enabled = false;

    public LoggerConfiguration toLoggerConfiguration() {
        return LoggerConfiguration.builder()
                .level(level)
                .writers(ArrayFactory.immutableArrayOf(writer))
                .enabled(enabled)
                .build();
    }

    public static DefaultLoggerConfiguration from(ConfigurationSource source) {
        DefaultLoggerConfiguration.DefaultLoggerConfigurationBuilder builder = DefaultLoggerConfiguration.builder();
        builder.level(LoggingLevel.parse(source.getString("level"), INFO));
        builder.writer(source.getNested(WRITER_SECTION, LoggerWriterConfiguration::from));
        return builder.build();
    }
}
