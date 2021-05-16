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
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.logging.constants.LoggingModuleConstants.ConfigurationKeys.*;
import static io.art.logging.constants.LoggingModuleConstants.Defaults.*;
import static io.art.logging.constants.LoggingWriterType.*;
import java.nio.charset.*;
import java.time.format.*;

@Getter
@Builder(toBuilder = true)
public class LoggerWriterConfiguration {
    private final Charset charset;

    private final LoggingWriterType type;
    private final ConsoleWriterConfiguration console;
    private final FileWriterConfiguration file;
    private final TcpWriterConfiguration tcp;
    private final DateTimeFormatter dateTimeFormatter;

    @Builder.Default
    private final ImmutableSet<String> categories = emptyImmutableSet();

    public static LoggerWriterConfiguration from(ConfigurationSource source) {
        LoggerWriterConfigurationBuilder builder = builder();
        builder.type(LoggingWriterType.parse(source.getString(TYPE_KEY), CONSOLE));
        builder.console(ConsoleWriterConfiguration.from(source));
        builder.file(FileWriterConfiguration.from(source));
        builder.categories(immutableSetOf(source.getStringArray(CATEGORIES_KEY)));
        builder.dateTimeFormatter(let(source.getString(DATE_TIME_FORMAT_KEY), DateTimeFormatter::ofPattern, DEFAULT_LOG_DATE_TIME_FORMAT));
        builder.charset(context().configuration().getCharset());
        return builder.build();
    }
}
