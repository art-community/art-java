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

package ru.art.tarantool.module.initializer;

import lombok.*;
import org.apache.logging.log4j.Logger;
import org.tarantool.*;
import ru.art.tarantool.configuration.*;
import ru.art.tarantool.constants.TarantoolModuleConstants;
import ru.art.tarantool.exception.*;

import static java.lang.Thread.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.concurrent.ForkJoinPool.*;
import static java.util.concurrent.TimeUnit.*;
import static lombok.AccessLevel.*;
import static org.apache.logging.log4j.io.IoBuilder.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.wrapper.ExceptionWrapper.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.tarantool.module.connector.TarantoolConnector.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.UNABLE_TO_CONNECT_TO_TARANTOOL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInstanceMode.*;
import static ru.art.tarantool.module.TarantoolModule.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class TarantoolInitializer {
    private final static OutputStream loggerOutputStream = forLogger(loggingModule().getLogger(TarantoolInitializer.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolInitializer.class);

    public static void initializeTarantools() {
        tarantoolModule().getTarantoolConfigurations()
                .entrySet()
                .stream()
                .filter(entry -> nonNull(entry) && nonNull(entry.getKey()) && nonNull(entry.getValue()))
                .map(Map.Entry::getKey)
                .map(TarantoolInitializer::initializeTarantool)
                .forEach(Runnable::run);
        ignoreException(loggerOutputStream::close, getLogger()::error);
    }

    protected static Runnable initializeTarantool(String instanceId) {
        TarantoolConfiguration tarantoolConfiguration = getTarantoolConfiguration(instanceId, tarantoolModule().getTarantoolConfigurations());
        ForkJoinTask<?> task = commonPool().submit(() -> {
            try {
                initialTarantoolSynchronously(instanceId, tarantoolConfiguration);
            } catch (Throwable throwable) {
                loggingModule().getLogger(TarantoolInitializer.class).error(throwable.getMessage(), throwable);
                throw throwable;
            }
        });
        ignoreException(() -> sleep(DEFAULT_TARANTOOL_INSTANCE_STARTUP_BETWEEN_TIME));
        return () -> waitInitializationTask(instanceId, tarantoolConfiguration, task);
    }

    private static void initialTarantoolSynchronously(String instanceId, TarantoolConfiguration tarantoolConfiguration) {
        TarantoolModuleConstants.TarantoolInstanceMode instanceMode = tarantoolConfiguration.getInstanceMode();
        String address = tarantoolConfiguration.getConnectionAddress();
        try {
            TarantoolClient tarantoolClient = tryConnectToInstance(instanceId);
            if (tarantoolClient.isAlive()) {
                return;
            }
        } catch (Throwable throwable) {
            if (instanceMode == LOCAL) {
                getLogger().warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_ON_STARTUP, instanceId, address));
                TarantoolInstanceLauncher.launchTarantoolInstance(instanceId);
                return;
            }
            throw throwable;
        }
        if (instanceMode == LOCAL) {
            getLogger().warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_ON_STARTUP, instanceId, address));
            TarantoolInstanceLauncher.launchTarantoolInstance(instanceId);
        }
    }

    private static void waitInitializationTask(String instanceId, TarantoolConfiguration tarantoolConfiguration, ForkJoinTask<?> task) {
        try {
            switch (tarantoolConfiguration.getInstanceMode()) {
                case LOCAL:
                    task.get(tarantoolModule().getLocalConfiguration().getProcessStartupTimeoutMillis(), MILLISECONDS);
                    break;
                case REMOTE:
                    task.get(tarantoolModule().getConnectionTimeoutMillis(), MILLISECONDS);
                    break;
            }
        } catch (Throwable throwable) {
            TarantoolConnectionConfiguration connectionConfiguration = tarantoolConfiguration.getConnectionConfiguration();
            String address = connectionConfiguration.getHost() + COLON + connectionConfiguration.getPort();
            throw new TarantoolInitializationException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, address));
        }
    }
}
