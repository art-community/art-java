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

package io.art.tarantool.module;

import io.art.core.module.*;
import io.art.tarantool.configuration.TarantoolModuleConfiguration.*;
import lombok.*;
import org.apache.logging.log4j.*;
import org.tarantool.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.module.state.*;
import static io.art.core.context.Context.context;
import static io.art.core.wrapper.ExceptionWrapper.ignoreException;
import static io.art.logging.LoggingModule.logger;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolInitializationMode.*;
import static io.art.tarantool.module.connector.TarantoolConnector.connectToTarantoolCluster;
import static io.art.tarantool.module.connector.TarantoolConnector.connectToTarantoolInstance;
import static io.art.tarantool.module.initializer.TarantoolInitializer.*;
import java.util.*;

@Getter
public class TarantoolModule implements StatefulModule<TarantoolModuleConfiguration, Configurator, TarantoolModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static StatefulModuleProxy<TarantoolModuleConfiguration, TarantoolModuleState> tarantoolModule = context().getStatefulModule(TarantoolModule.class.getSimpleName());
    private final String id = TARANTOOL_MODULE_ID;
    private final TarantoolModuleConfiguration configuration = new TarantoolModuleConfiguration();
    private final Configurator configurator = new Configurator();
    private final TarantoolModuleState state = new TarantoolModuleState();
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(TarantoolModule.class);

    @Override
    public void onLoad() {
        if (configuration.getInitializationMode() != BOOTSTRAP) {
            return;
        }
        initializeTarantools(configuration);
    }

    public static StatefulModuleProxy<TarantoolModuleConfiguration, TarantoolModuleState> tarantoolModule() {
        return getTarantoolModule();
    }

    public static TarantoolModuleState tarantoolModuleState() {
        return tarantoolModule().state();
    }

    public static TarantoolConfiguration getTarantoolConfiguration(String instanceId, Map<String, TarantoolConfiguration> configurations) {
        TarantoolConfiguration configuration = configurations.get(instanceId);
        if (isNull(configuration)) {
            throw new TarantoolConnectionException(format(CONFIGURATION_IS_NULL, instanceId));
        }
        return configuration;
    }

    public static TarantoolClient getClient(String instanceId) {
        TarantoolClient client = tarantoolModuleState().getClients().get(instanceId);
        if (isNull(client)) {
            client = connectToTarantoolInstance(instanceId);
            tarantoolModuleState().getClients().put(instanceId, client);
        }
        return client;
    }

    public static TarantoolClient getClusterClient(String instanceId) {
        TarantoolClient client = tarantoolModuleState().getClusterClients().get(instanceId);
        if (isNull(client)) {
            client = connectToTarantoolCluster(instanceId);
            tarantoolModuleState().getClusterClients().put(instanceId, client);
        }
        return client;
    }

    @Override
    public void onUnload() {
        tarantoolModuleState().getClients().entrySet().stream().filter(client -> !client.getValue().isClosed()).forEach(this::closeTarantoolClient);
        tarantoolModuleState().getClusterClients().entrySet().stream().filter(client -> !client.getValue().isClosed()).forEach(this::closeTarantoolClient);
    }

    private void closeTarantoolClient(Map.Entry<String, TarantoolClient> entry) {
        ignoreException(() -> {
            entry.getValue().close();
            getLogger().info(format(TARANTOOL_CLIENT_CLOSED, entry.getKey()));
        }, getLogger()::error);
    }
}
