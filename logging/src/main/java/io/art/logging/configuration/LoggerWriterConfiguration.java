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

import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.logging.constants.*;
import lombok.Builder;
import lombok.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.logging.constants.LoggingWriterType.*;
import static java.time.format.DateTimeFormatter.*;
import java.nio.charset.*;
import java.time.format.*;

@Getter
@Builder(toBuilder = true)
public class LoggerWriterConfiguration {
    private final LoggingWriterType type;
    private final ConsoleWriterConfiguration console;
    private final DateTimeFormatter dateTimeFormatter;

    @Builder.Default
    private final ImmutableSet<String> categories = emptyImmutableSet();

    @Builder.Default
    private final Boolean enabled = false;

    public static LoggerWriterConfiguration from(ConfigurationSource source) {
        LoggerWriterConfigurationBuilder builder = builder();
        builder.type(LoggingWriterType.parse(source.getString("type"), CONSOLE));
        builder.console(ConsoleWriterConfiguration.builder().build());
        builder.categories(immutableSetOf(source.getStringArray("categories")));
        builder.dateTimeFormatter(ofPattern(source.getString("dateTimeFormat")));
        return builder.build();
    }

    @Getter
    @Builder
    public static class ConsoleWriterConfiguration {
        @Builder.Default
        private final Boolean colored = false;

        @Builder.Default
        private final Charset charset = context().configuration().getCharset();
    }
}
