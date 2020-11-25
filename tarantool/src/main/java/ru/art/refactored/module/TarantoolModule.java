package ru.art.refactored.module;

import org.tarantool.TarantoolClient;
import ru.art.refactored.configuration.TarantoolInstanceConfiguration;
import ru.art.refactored.configuration.TarantoolModuleConfiguration;
import ru.art.refactored.exception.TarantoolModuleException;
import ru.art.refactored.module.state.TarantoolModuleState;
import ru.art.refactored.dao.TarantoolInstance;
import ru.art.refactored.dao.TarantoolSpace;

import static ru.art.refactored.constants.TarantoolModuleConstants.ExceptionMessages.CONFIGURATION_IS_NULL;
import static ru.art.refactored.module.connector.TarantoolConnector.connect;
import static java.lang.String.format;

import java.util.Optional;

public class TarantoolModule {
    private final TarantoolModuleConfiguration configuration;
    private final TarantoolModuleState state = new TarantoolModuleState();

    public TarantoolModule(TarantoolModuleConfiguration configuration){
        this.configuration = configuration;
    }

    public TarantoolInstance getInstance(String clientId){
        return new TarantoolInstance(getClient(clientId));
    }

    public TarantoolSpace getSpace(String clientId, String space){
        return new TarantoolSpace(getClient(clientId), space);
    }

    public TarantoolClient getClient(String clientId){
        Optional<TarantoolClient> existingClient = state.getClient(clientId);
        if (existingClient.isPresent()) return existingClient.get();
        TarantoolInstanceConfiguration config = configuration.instances.get(clientId);
        if (config == null) throw new TarantoolModuleException(format(CONFIGURATION_IS_NULL, clientId));
        TarantoolClient newClient = connect(clientId, config);
        state.registerClient(clientId, newClient);
        return newClient;
    }
}
