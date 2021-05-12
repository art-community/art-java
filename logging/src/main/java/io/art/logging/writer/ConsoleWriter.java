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

package io.art.logging.writer;

import io.art.logging.configuration.*;
import io.art.logging.constants.*;
import io.art.logging.model.*;
import static io.art.core.colorizer.AnsiColorizer.*;
import static io.art.core.constants.AnsiColor.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.logging.colorizer.LogColorizer.*;
import static java.text.MessageFormat.*;

public class ConsoleWriter implements LoggerWriter {
    private final LoggerConfiguration loggerConfiguration;
    private final LoggerWriterConfiguration writerConfiguration;

    public ConsoleWriter(LoggerConfiguration loggerConfiguration, LoggerWriterConfiguration writerConfiguration) {
        this.loggerConfiguration = loggerConfiguration;
        this.writerConfiguration = writerConfiguration;
    }

    @Override
    public void write(LoggingMessage message) {
        if (message.getLevel() == LoggingLevel.ERROR) {
            System.err.println(buildMessage(message));
            return;
        }

        System.out.println(buildMessage(message));
    }

    private String buildMessage(LoggingMessage message) {
        String dateTime = writerConfiguration.getDateTimeFormatter().format(message.getDateTime());
        LoggingLevel level = message.getLevel();
        return format("{0} {1} {2}: {3} - {4}",
                message(dateTime, BLUE),
                byLevel(level, level.name()),
                message(OPENING_SQUARE_BRACES + message.getThread().getName() + CLOSING_SQUARE_BRACES, PURPLE_BOLD),
                message.getLogger(),
                message.getMessage()
        );
    }
}
