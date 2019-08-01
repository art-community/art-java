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
