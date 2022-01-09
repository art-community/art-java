package io.art.tarantool.module.state;


import io.art.core.module.*;
import io.art.tarantool.client.*;
import io.art.tarantool.cluster.*;
import java.util.function.*;

public class TarantoolModuleState implements ModuleState {
    public Supplier<TarantoolConnector> getConnector(String clusterId) {
        return null;
    }

    public Supplier<TarantoolClient> getClient(String clusterId, String clientId) {
        return null;
    }
}
