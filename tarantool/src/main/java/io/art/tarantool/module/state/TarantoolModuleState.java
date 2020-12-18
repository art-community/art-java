package io.art.tarantool.module.state;


import io.art.core.module.ModuleState;
import io.art.tarantool.module.client.TarantoolClusterClient;
import io.tarantool.driver.ClusterTarantoolClient;
import io.tarantool.driver.api.TarantoolClient;

import java.util.Map;
import java.util.Optional;

import static io.art.core.factory.MapFactory.map;
import static java.util.Optional.ofNullable;

public class TarantoolModuleState implements ModuleState {
    private final Map<String, TarantoolClusterClient> activeClusters = map();
    private final Map<String, TarantoolClient> activeClients = map();

    public Optional<TarantoolClusterClient> getClusterClient(String clusterId){
        return ofNullable(activeClusters.get(clusterId));
    }

    public void registerClusterClient(String clusterId, TarantoolClusterClient client){
        activeClusters.put(clusterId, client);
    }

    public Optional<TarantoolClient> getClient(String clientId){
        return ofNullable(activeClients.get(clientId));
    }

    public void registerClient(String clientId, TarantoolClient client){
        activeClients.put(clientId, client);
    }


}
