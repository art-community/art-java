package io.art.refactored.module.state;

import org.tarantool.*;
import static io.art.core.factory.MapFactory.*;
import static java.util.Optional.*;
import java.util.*;


public class TarantoolModuleState {
    private final Map<String, TarantoolClient> activeClients = map();

    public Optional<TarantoolClient> getClient(String clientId) {
        return ofNullable(activeClients.get(clientId));
    }

    public void registerClient(String clientId, TarantoolClient client) {
        activeClients.put(clientId, client);
    }
}
