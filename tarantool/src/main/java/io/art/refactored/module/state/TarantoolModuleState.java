package io.art.refactored.module.state;

import org.tarantool.TarantoolClient;
import java.util.Map;
import java.util.Optional;

import static io.art.core.factory.CollectionsFactory.mapOf;

public class TarantoolModuleState {
    private final Map<String, TarantoolClient> activeClients = mapOf();

    public Optional<TarantoolClient> getClient(String clientId){
        TarantoolClient client = activeClients.get(clientId);
        if (client == null) return Optional.empty();
        return Optional.ofNullable(client);
    }

    public void registerClient(String clientId, TarantoolClient client){
        activeClients.put(clientId, client);
    }
}
