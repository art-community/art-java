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

package ru.art.tarantool.module.connector;

import lombok.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import org.tarantool.*;
import ru.art.tarantool.configuration.*;
import ru.art.tarantool.exception.*;

import static java.text.MessageFormat.*;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.UNABLE_TO_CONNECT_TO_TARANTOOL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static ru.art.tarantool.module.TarantoolModule.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@UtilityClass
public final class TarantoolConnector {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolConnector.class);

    public static TarantoolClient connectToTarantoolInstance(String instanceId) {
        String[] address = getInstanceAddress(instanceId);
        return connect(instanceId, address);
    }

    public static TarantoolClient connectToTarantoolCluster(String instanceId) {
        String[] addresses = getClusterAddresses(instanceId);
        return connect(instanceId, addresses);
    }

    public static TarantoolClient tryConnectToInstance(String instanceId) {
        TarantoolConfiguration tarantoolConfiguration = getTarantoolConfiguration(instanceId, tarantoolModule().getTarantoolConfigurations());
        TarantoolConnectionConfiguration connectionConfiguration = tarantoolConfiguration.getConnectionConfiguration();
        TarantoolClientConfig config = getClientConfig(connectionConfiguration);
        String[] address = getInstanceAddress(instanceId);

        SocketChannelProvider socketChannelProvider = new RoundRobinSocketProviderImpl(address);
        TarantoolClientImpl tarantoolClient;

        try {
            getLogger().info(format(TRYING_TO_CONNECT,
                    instanceId,
                    address,
                    config.initTimeoutMillis));
            tarantoolClient = new TarantoolClientImpl(socketChannelProvider, config);
        } catch (Throwable throwable) {
            throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, address), throwable);
        }
        return tarantoolClient;
    }

    private static String[] getInstanceAddress(String instanceId) {
        TarantoolConfiguration tarantoolConfiguration = getTarantoolConfiguration(instanceId, tarantoolModule().getTarantoolConfigurations());
        TarantoolConnectionConfiguration connectionConfiguration = tarantoolConfiguration.getConnectionConfiguration();
        String address = connectionConfiguration.getHost() + COLON + connectionConfiguration.getPort();
        return new String[]{address};
    }

    private static String[] getClusterAddresses(String instanceId) {
        Map<String, TarantoolConfiguration> configurations = tarantoolModule().getTarantoolConfigurations();
        TarantoolConfiguration tarantoolConfiguration = getTarantoolConfiguration(instanceId, configurations);

        Set<String> replicas;
        return isEmpty(replicas = tarantoolConfiguration.getReplicas())
                ? getInstanceAddress(instanceId)
                : replicas.stream().map(configurations::get)
                .filter(Objects::nonNull)
                .map(TarantoolConfiguration::getConnectionConfiguration)
                .map(configuration -> configuration.getHost() + COLON + configuration.getPort())
                .toArray(String[]::new);
    }

    private static TarantoolClientImpl connect(String instanceId, String[] addresses) {
        int retries = 0;
        TarantoolConfiguration tarantoolConfiguration = getTarantoolConfiguration(instanceId, tarantoolModule().getTarantoolConfigurations());
        TarantoolConnectionConfiguration connectionConfiguration = tarantoolConfiguration.getConnectionConfiguration();
        TarantoolClientConfig config = getClientConfig(connectionConfiguration);
        SocketChannelProvider socketChannelProvider = new RoundRobinSocketProviderImpl(addresses);

        while (retries < connectionConfiguration.getMaxRetryCount()) {
            try {
                getLogger().info(format(WAITING_FOR_CONNECT,
                        instanceId,
                        Arrays.toString(addresses),
                        config.initTimeoutMillis));
                TarantoolClientImpl client = new TarantoolClientImpl(socketChannelProvider, config);
                getLogger().info(format(TARANTOOL_SUCCESSFULLY_CONNECTED, instanceId, Arrays.toString(addresses)));
                return client;
            } catch (CommunicationException exception) {
                getLogger().warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_RETRY, instanceId, Arrays.toString(addresses)), exception);
            } catch (Throwable throwable) {
                throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, Arrays.toString(addresses)), throwable);
            }
            retries++;
        }
        throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, Arrays.toString(addresses)));
    }

    private static TarantoolClientConfig getClientConfig(TarantoolConnectionConfiguration connectionConfiguration) {
        TarantoolClientConfig config = new TarantoolClientConfig();
        config.initTimeoutMillis = tarantoolModule().getConnectionTimeoutMillis();
        config.username = connectionConfiguration.getUsername();
        config.password = connectionConfiguration.getPassword();
        config.operationExpiryTimeMillis = connectionConfiguration.getOperationTimeoutMillis();
        return config;
    }

}