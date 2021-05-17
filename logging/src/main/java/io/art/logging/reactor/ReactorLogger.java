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

package io.art.logging.reactor;

import io.art.logging.logger.*;
import lombok.*;
import static java.lang.String.*;

@AllArgsConstructor
public class ReactorLogger implements reactor.util.Logger {
    private final Logger logger;

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String message) {
        logger.trace(message);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(format(format, arguments));
    }

    @Override
    public void trace(String message, Throwable throwable) {
        logger.trace(message);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(format(format, arguments));
    }

    @Override
    public void debug(String message, Throwable throwable) {
        logger.debug(message, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(format(format, arguments));
    }

    @Override
    public void info(String message, Throwable throwable) {
        logger.info(message, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(format(format, arguments));
    }

    @Override
    public void warn(String message, Throwable throwable) {
        logger.warn(message, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format(format, arguments));
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
