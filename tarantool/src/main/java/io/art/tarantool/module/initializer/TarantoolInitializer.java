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

package io.art.tarantool.module.initializer;

import lombok.*;
import org.apache.logging.log4j.*;
import org.tarantool.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.constants.*;
import io.art.tarantool.exception.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.LoggingModule.*;
import static java.lang.Thread.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.concurrent.ForkJoinPool.*;
import static java.util.concurrent.TimeUnit.*;
import static lombok.AccessLevel.*;
import static org.apache.logging.log4j.io.IoBuilder.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolInstanceMode.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static io.art.tarantool.module.connector.TarantoolConnector.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class TarantoolInitializer {
    private final static OutputStream loggerOutputStream = forLogger(logger(TarantoolInitializer.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolInitializer.class);

    public static void initializeTarantools(TarantoolModuleConfiguration configuration) {
        configuration.getTarantoolConfigurations()
                .entrySet()
                .stream()
                .filter(entry -> nonNull(entry) && nonNull(entry.getKey()) && nonNull(entry.getValue()))
                .map(Map.Entry::getKey)
                .map(instanceId -> initializeTarantool(instanceId, configuration))
                .forEach(Runnable::run);
        ignoreException(loggerOutputStream::close, getLogger()::error);
    }

    protected static Runnable initializeTarantool(String instanceId, TarantoolModuleConfiguration configuration) {
        TarantoolConfiguration tarantoolConfiguration = getTarantoolConfiguration(instanceId, configuration.getTarantoolConfigurations());
        ForkJoinTask<?> task = commonPool().submit(() -> {
            try {
                initialTarantoolSynchronously(instanceId, tarantoolConfiguration);
            } catch (Throwable throwable) {
                logger(TarantoolInitializer.class).error(throwable.getMessage(), throwable);
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
                    task.get(tarantoolModule().configuration().getLocalConfiguration().getProcessStartupTimeoutMillis(), MILLISECONDS);
                    break;
                case REMOTE:
                    task.get(tarantoolModule().configuration().getConnectionTimeoutMillis(), MILLISECONDS);
                    break;
            }
        } catch (Throwable throwable) {
            TarantoolConnectionConfiguration connectionConfiguration = tarantoolConfiguration.getConnectionConfiguration();
            String address = connectionConfiguration.getHost() + COLON + connectionConfiguration.getPort();
            throw new TarantoolInitializationException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, address));
        }
    }
}
