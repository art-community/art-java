/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.http.logger;

import org.apache.logging.log4j.Logger;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.DefaultHttpLogWriter.Level;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.Precorrelation;
import org.zalando.logbook.RawHttpRequest;
import static org.zalando.logbook.DefaultHttpLogWriter.Level.INFO;
import static ru.art.logging.LoggingModule.loggingModule;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@SuppressWarnings("SameParameterValue")
public class ZalangoLogbookLogWriter implements HttpLogWriter {
    private final Logger logger;
    private final Predicate<Logger> activator;
    private final BiConsumer<Logger, String> consumer;

    public ZalangoLogbookLogWriter() {
        this(loggingModule().getLogger(ZalangoLogbookLogWriter.class));
    }

    private ZalangoLogbookLogWriter(final Logger logger) {
        this(logger, INFO);
    }

    private ZalangoLogbookLogWriter(final Logger logger, final Level level) {
        this.logger = logger;
        this.activator = chooseActivator(level);
        this.consumer = chooseConsumer(level);
    }

    @SuppressWarnings("Duplicates")
    private static Predicate<Logger> chooseActivator(final Level level) {
        switch (level) {
            case DEBUG:
                return Logger::isDebugEnabled;
            case INFO:
                return Logger::isInfoEnabled;
            case WARN:
                return Logger::isWarnEnabled;
            case ERROR:
                return Logger::isErrorEnabled;
            default:
                return Logger::isTraceEnabled;
        }
    }

    @SuppressWarnings("Duplicates")
    private static BiConsumer<Logger, String> chooseConsumer(final Level level) {
        switch (level) {
            case DEBUG:
                return Logger::debug;
            case INFO:
                return Logger::info;
            case WARN:
                return Logger::warn;
            case ERROR:
                return Logger::error;
            default:
                return Logger::trace;
        }
    }

    @Override
    public boolean isActive(final RawHttpRequest request) {
        return activator.test(logger);
    }

    @Override
    public void writeRequest(final Precorrelation<String> precorrelation) {
        consumer.accept(logger, precorrelation.getRequest());
    }

    @Override
    public void writeResponse(final Correlation<String, String> correlation) {
        consumer.accept(logger, correlation.getResponse());
    }
}
