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

import io.art.core.property.*;
import io.art.logging.configuration.*;
import io.art.logging.constants.*;
import io.art.logging.manager.*;
import io.art.logging.model.*;
import static io.art.core.checker.TerminalChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.SystemExtensions.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.logging.colorizer.AnsiColorizer.AnsiColor.*;
import static io.art.logging.colorizer.AnsiColorizer.*;
import static io.art.logging.colorizer.LogColorizer.*;
import static io.art.logging.constants.LoggingLevel.*;
import static io.art.logging.constants.LoggingModuleConstants.*;
import static java.lang.System.*;
import static java.util.Objects.*;
import java.text.*;

public class ConsoleWriter implements LoggerWriter {
    private final LoggerWriterConfiguration writerConfiguration;
    private final LazyProperty<Boolean> colored;

    public ConsoleWriter(LoggingManager manager, LoggerWriterConfiguration writerConfiguration) {
        this.writerConfiguration = writerConfiguration;
        colored = lazy(() -> writerConfiguration.getConsole().isColored() && (isNull(console()) || terminalSupportColors()));
    }


    @Override
    public void write(LoggingMessage message) {
        if (message.getLevel() == ERROR) {
            printError(format(message));
            return;
        }

        printMessage(format(message));
    }

    private String format(LoggingMessage message) {
        String dateTime = writerConfiguration.getDateTimeFormatter().format(message.getDateTime());
        LoggingLevel level = message.getLevel();
        if (colored.get()) {
            return MessageFormat.format(LOGGING_FORMAT,
                    message(dateTime, BLUE_BOLD),
                    byLevel(level, level.name()),
                    additional(OPENING_SQUARE_BRACES + message.getThread().getName() + CLOSING_SQUARE_BRACES),
                    special(message.getLogger()),
                    message.getMessage()
            );
        }
        return MessageFormat.format(LOGGING_FORMAT,
                dateTime,
                level.name(),
                OPENING_SQUARE_BRACES + message.getThread().getName() + CLOSING_SQUARE_BRACES,
                message.getLogger(),
                message.getMessage()
        );
    }
}
