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

package ru.art.tarantool.state;

import lombok.Getter;
import lombok.Setter;
import org.tarantool.TarantoolClient;
import ru.art.core.module.ModuleState;
import ru.art.tarantool.configuration.TarantoolConfiguration;
import ru.art.tarantool.configuration.TarantoolConnectionConfiguration;
import ru.art.tarantool.configuration.lua.TarantoolCommonScriptConfiguration;
import ru.art.tarantool.configuration.lua.TarantoolValueScriptConfiguration;
import ru.art.tarantool.exception.TarantoolConnectionException;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.concurrent.ConcurrentHashMap.newKeySet;
import static ru.art.core.constants.StringConstants.COLON;
import static ru.art.core.factory.CollectionsFactory.concurrentHashMap;
import static ru.art.tarantool.connector.TarantoolConnector.connectToTarantool;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.CONFIGURATION_IS_NULL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.UNABLE_TO_CONNECT_TO_TARANTOOL;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModule;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModuleState;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class TarantoolModuleState implements ModuleState {
    private final Map<String, TarantoolClient> clients = concurrentHashMap();
    private final Set<TarantoolValueScriptConfiguration> loadedValueScripts = newKeySet();
    private final Set<TarantoolCommonScriptConfiguration> loadedCommonScripts = newKeySet();

    @SuppressWarnings("Duplicates")
    public TarantoolClient getClient(String instanceId) {
        TarantoolClient client = tarantoolModuleState().getClients().get(instanceId);
        if (isNull(client)) {
            client = connectToTarantool(instanceId);
            if (!client.isAlive()) {
                TarantoolConfiguration tarantoolConfiguration = tarantoolModule().getTarantoolConfigurations().get(instanceId);
                if (isNull(tarantoolConfiguration)) {
                    throw new TarantoolConnectionException(format(CONFIGURATION_IS_NULL, instanceId));
                }
                TarantoolConnectionConfiguration connectionConfiguration = tarantoolConfiguration.getConnectionConfiguration();
                String address = connectionConfiguration.getHost() + COLON + connectionConfiguration.getPort();
                throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, address));
            }
        }
        return client;
    }
}
