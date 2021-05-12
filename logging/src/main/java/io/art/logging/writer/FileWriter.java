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
import io.art.logging.exception.*;
import io.art.logging.model.*;
import static com.google.common.base.Throwables.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.handler.ExceptionHandler.*;
import static io.art.logging.constants.LoggingModuleConstants.Errors.*;
import static io.art.logging.constants.LoggingModuleConstants.*;
import static java.nio.file.StandardOpenOption.*;
import static java.time.LocalDateTime.*;
import static java.util.Arrays.*;
import static java.util.Comparator.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import java.io.*;
import java.text.*;
import java.time.*;
import java.util.*;

public class FileWriter implements LoggerWriter {
    private final LoggerConfiguration loggerConfiguration;
    private final LoggerWriterConfiguration writerConfiguration;
    private volatile OutputStream outputStream;
    private LocalDateTime currentTimeStamp;

    public FileWriter(LoggerConfiguration loggerConfiguration, LoggerWriterConfiguration writerConfiguration) {
        this.loggerConfiguration = loggerConfiguration;
        this.writerConfiguration = writerConfiguration;
        FileWriterConfiguration fileConfiguration = writerConfiguration.getFile();
        File directory = fileConfiguration.getDirectory().toFile();
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new LoggingModuleException(MessageFormat.format(UNABLE_TO_CREATE_LOG_DIRECTORY, directory.toString()));
            }
        }
        String prefix = fileConfiguration.getPrefix();
        currentTimeStamp = now();
        String currentTimeStampString = fileConfiguration.getTimestampFormat().format(currentTimeStamp);
        String suffix = fileConfiguration.getSuffix();
        String fileName = letIfNotEmpty(prefix, filePrefix -> filePrefix + currentTimeStampString, currentTimeStampString) + suffix;

        System.out.println(fileConfiguration.getDirectory().resolve(fileName));

        File[] currentFiles = directory.listFiles();
        if (isNull(currentFiles) || isEmpty(currentFiles)) {
            outputStream = fileOutputStream(fileConfiguration.getDirectory().resolve(fileName));
            return;
        }

        currentTimeStamp = stream(currentFiles)
                .map(file -> parseFileTimeStamp(fileConfiguration, file.getName()))
                .filter(Objects::nonNull)
                .min(comparing(identity()))
                .orElse(currentTimeStamp);

        String fileTimestamp = fileConfiguration.getTimestampFormat().format(currentTimeStamp);
        fileName = letIfNotEmpty(fileConfiguration.getPrefix(), filePrefix -> filePrefix + fileTimestamp, fileTimestamp) + fileConfiguration.getSuffix();
        System.out.println(fileConfiguration.getDirectory().resolve(fileName));

        outputStream = fileOutputStream(fileConfiguration.getDirectory().resolve(fileName), CREATE, APPEND, WRITE);
    }


    @Override
    public void write(LoggingMessage message) {
        rotate();
        try {
            outputStream.write(format(message).getBytes(writerConfiguration.getFile().getCharset()));
            outputStream.flush();
        } catch (IOException exception) {
            System.err.println(getStackTraceAsString(exception));
        }
    }

    private LocalDateTime parseFileTimeStamp(FileWriterConfiguration configuration, String name) {
        if (isEmpty(name)) {
            return null;
        }
        int prefixIndex = name.indexOf(configuration.getPrefix());
        if (prefixIndex == -1) {
            int suffixIndex = name.indexOf(configuration.getSuffix());
            if (suffixIndex == -1) {
                return nullIfException(() -> parse(name, configuration.getTimestampFormat()));
            }
            return nullIfException(() -> parse(name.substring(0, suffixIndex), configuration.getTimestampFormat()));
        }

        int suffixIndex = name.indexOf(configuration.getSuffix());
        if (suffixIndex == -1) {
            return nullIfException(() -> parse(name.substring(prefixIndex + 1), configuration.getTimestampFormat()));
        }

        return nullIfException(() -> parse(name.substring(prefixIndex + 1, suffixIndex), configuration.getTimestampFormat()));
    }

    private void rotate() {
        LocalDateTime newTimeStamp = now();
        if (newTimeStamp.isBefore(currentTimeStamp.plus(writerConfiguration.getFile().getRotationPeriod()))) {
            return;
        }
        try {
            outputStream.close();
            FileWriterConfiguration fileConfiguration = writerConfiguration.getFile();
            String fileTimestamp = fileConfiguration.getTimestampFormat().format(newTimeStamp);
            String fileName = letIfNotEmpty(fileConfiguration.getPrefix(), filePrefix -> filePrefix + fileTimestamp, fileTimestamp) + fileConfiguration.getSuffix();
            outputStream = fileOutputStream(fileConfiguration.getDirectory().resolve(fileName));
            currentTimeStamp = newTimeStamp;
        } catch (IOException ioException) {
            System.err.println(getStackTraceAsString(ioException));
        }
    }

    private String format(LoggingMessage message) {
        String dateTime = writerConfiguration.getDateTimeFormatter().format(message.getDateTime());
        LoggingLevel level = message.getLevel();
        return MessageFormat.format(LOGGING_FORMAT,
                dateTime,
                level.name(),
                OPENING_SQUARE_BRACES + message.getThread().getName() + CLOSING_SQUARE_BRACES,
                message.getLogger(),
                message.getMessage() + System.lineSeparator()
        );
    }
}
