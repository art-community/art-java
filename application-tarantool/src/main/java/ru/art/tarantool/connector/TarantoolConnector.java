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

package ru.art.tarantool.connector;

import lombok.*;
import org.tarantool.*;
import ru.art.tarantool.configuration.*;
import ru.art.tarantool.exception.*;
import ru.art.tarantool.initializer.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.UNABLE_TO_CONNECT_TO_TARANTOOL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static ru.art.tarantool.module.TarantoolModule.*;

@SuppressWarnings("Duplicates")
@NoArgsConstructor(access = PRIVATE)
public final class TarantoolConnector {
    public static TarantoolClient connectToTarantool(String instanceId) {
        TarantoolClient connectedClient = tarantoolModuleState().getClients().get(instanceId);
        if (nonNull(connectedClient)) {
            return connectedClient;
        }
        TarantoolClientConfig config = new TarantoolClientConfig();
        config.initTimeoutMillis = tarantoolModule().getConnectionTimeout();
        TarantoolConfiguration tarantoolConfiguration = tarantoolModule()
                .getTarantoolConfigurations()
                .get(instanceId);
        if (isNull(tarantoolConfiguration)) {
            throw new TarantoolConnectionException(format(CONFIGURATION_IS_NULL, instanceId));
        }
        TarantoolConnectionConfiguration connectionConfiguration = tarantoolConfiguration.getConnectionConfiguration();
        config.username = connectionConfiguration.getUsername();
        config.password = connectionConfiguration.getPassword();
        String address = connectionConfiguration.getHost() + COLON + connectionConfiguration.getPort();
        SocketChannelProvider socketChannelProvider = new RoundRobinSocketProviderImpl(address);
        TarantoolClientImpl tarantoolClient = new TarantoolClientImpl(socketChannelProvider, config);
        try {
            tarantoolClient.waitAlive();
        } catch (Throwable e) {
            throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, address), e);
        }
        loggingModule().getLogger(TarantoolInitializer.class).info(format(TARANTOOL_SUCCESSFULLY_CONNECTED, instanceId, address));
        tarantoolModuleState().getClients().put(instanceId, tarantoolClient);
        return tarantoolClient;
    }

    public static TarantoolClient tryConnectToTarantool(String instanceId) {
        TarantoolClientConfig config = new TarantoolClientConfig();
        config.initTimeoutMillis = tarantoolModule().getProbeConnectionTimeout();
        TarantoolConfiguration tarantoolConfiguration = tarantoolModule()
                .getTarantoolConfigurations()
                .get(instanceId);
        if (isNull(tarantoolConfiguration)) {
            throw new TarantoolConnectionException(format(CONFIGURATION_IS_NULL, instanceId));
        }
        TarantoolConnectionConfiguration connectionConfiguration = tarantoolConfiguration.getConnectionConfiguration();
        String address = connectionConfiguration.getHost() + COLON + connectionConfiguration.getPort();
        SocketChannelProvider socketChannelProvider = new RoundRobinSocketProviderImpl(address);
        TarantoolClientImpl tarantoolClient;
        try {
            tarantoolClient = new TarantoolClientImpl(socketChannelProvider, config);
            tarantoolClient.waitAlive();
        } catch (Throwable e) {
            throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, address), e);
        }
        return tarantoolClient;
    }
}
