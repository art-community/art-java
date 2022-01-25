package io.art.tarantool.state;


import io.art.core.module.*;
import io.art.tarantool.client.*;
import io.art.tarantool.storage.*;
import java.util.function.*;

public class TarantoolModuleState implements ModuleState {
    public Supplier<TarantoolStorage> getConnector(String clusterId) {
        return null;
    }

    public Supplier<TarantoolClient> getClient(String clusterId, String clientId) {
        return null;
    }
}
