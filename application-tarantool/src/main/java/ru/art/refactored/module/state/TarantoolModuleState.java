package ru.art.refactored.module.state;

import org.tarantool.TarantoolClient;
import java.util.Map;
import java.util.Optional;

import static ru.art.core.factory.CollectionsFactory.*;

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
