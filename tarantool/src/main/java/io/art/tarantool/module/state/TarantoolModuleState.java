package io.art.tarantool.module.state;


import io.art.core.module.ModuleState;
import io.tarantool.driver.api.TarantoolClient;

import java.util.Map;
import java.util.Optional;

import static io.art.core.factory.MapFactory.map;
import static java.util.Optional.ofNullable;

public class TarantoolModuleState implements ModuleState {
    private final Map<String, TarantoolClient> activeClients = map();

    public Optional<TarantoolClient> getClient(String clientId){
        return ofNullable(activeClients.get(clientId));
    }

    public void registerClient(String clientId, TarantoolClient client){
        activeClients.put(clientId, client);
    }
}
