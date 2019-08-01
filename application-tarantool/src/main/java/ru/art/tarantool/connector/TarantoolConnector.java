package ru.art.tarantool.connector;

import lombok.NoArgsConstructor;
import org.tarantool.*;
import ru.art.tarantool.configuration.TarantoolConfiguration;
import ru.art.tarantool.configuration.TarantoolConnectionConfiguration;
import ru.art.tarantool.exception.TarantoolConnectionException;
import ru.art.tarantool.initializer.TarantoolInitializer;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.constants.StringConstants.COLON;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.CONFIGURATION_IS_NULL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.UNABLE_TO_CONNECT_TO_TARANTOOL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.TARANTOOL_SUCCESSFULLY_CONNECTED;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModule;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModuleState;

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
        } catch (Exception e) {
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
        TarantoolClientImpl tarantoolClient = new TarantoolClientImpl(socketChannelProvider, config);
        try {
            tarantoolClient.waitAlive();
        } catch (Exception e) {
            throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, address), e);
        }
        return tarantoolClient;
    }
}
