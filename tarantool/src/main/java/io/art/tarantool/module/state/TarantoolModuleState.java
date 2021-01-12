package io.art.tarantool.module.state;


import io.art.core.module.ModuleState;
import io.art.tarantool.module.client.TarantoolClusterClient;
import io.tarantool.driver.ClusterTarantoolClient;
import io.tarantool.driver.api.TarantoolClient;

import java.util.Map;
import java.util.Optional;

import static io.art.core.factory.MapFactory.map;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class TarantoolModuleState implements ModuleState {
    private final Map<String, TarantoolClusterClient> activeClusters = map();
    private final Map<String, Map<String, TarantoolClient>> activeClients = map();


    public Optional<TarantoolClusterClient> getClusterClient(String clusterId){
        return ofNullable(activeClusters.get(clusterId));
    }

    public void registerClusterClient(String clusterId, TarantoolClusterClient client){
        activeClusters.put(clusterId, client);
    }

    public Optional<TarantoolClient> getClient(String clusterId, String clientId){
        if (isNull(activeClients.get(clusterId))) return empty();
        return ofNullable(activeClients.get(clusterId).get(clientId));
    }

    public void registerClient(String clusterId, String clientId, TarantoolClient client){
        if (isNull(activeClients.get(clusterId))) activeClients.put(clusterId, map());
        activeClients.get(clusterId).put(clientId, client);
    }


}
