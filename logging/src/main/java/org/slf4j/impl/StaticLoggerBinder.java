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

package org.slf4j.impl;

import io.art.core.exception.*;
import org.slf4j.*;
import org.slf4j.spi.*;
import static io.art.logging.module.LoggingModule.*;

public final class StaticLoggerBinder implements LoggerFactoryBinder {
    private static final ILoggerFactory iLoggerFactory = name -> new Slf4jLogger(logger(name));
    private static final Class<? extends ILoggerFactory> loggerFactoryClass = iLoggerFactory.getClass();

    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    private final ILoggerFactory loggerFactory;

    private StaticLoggerBinder() {
        try {
            loggerFactory = loggerFactoryClass.getConstructor().newInstance();
        } catch (Throwable throwable) {
            throw new InternalRuntimeException(throwable);
        }
    }

    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return loggerFactoryClass.getName();
    }
}
