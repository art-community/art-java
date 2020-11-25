package io.art.refactored.module;

import org.tarantool.TarantoolClient;
import io.art.refactored.configuration.TarantoolInstanceConfiguration;
import io.art.refactored.configuration.TarantoolModuleConfiguration;
import io.art.refactored.exception.TarantoolModuleException;
import io.art.refactored.module.state.TarantoolModuleState;
import io.art.refactored.dao.TarantoolInstance;
import io.art.refactored.dao.TarantoolSpace;

import static io.art.refactored.constants.TarantoolModuleConstants.ExceptionMessages.CONFIGURATION_IS_NULL;
import static io.art.refactored.module.connector.TarantoolConnector.connect;
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
