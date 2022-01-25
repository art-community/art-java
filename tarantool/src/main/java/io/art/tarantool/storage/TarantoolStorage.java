package io.art.tarantool.storage;

import io.art.core.network.balancer.*;
import io.art.tarantool.client.*;
import io.art.tarantool.configuration.*;


public class TarantoolStorage {
    private final Balancer<TarantoolClient> immutable;
    private final Balancer<TarantoolClient> mutable;

    public TarantoolStorage(TarantoolStorageConfiguration configuration) {
        immutable = new RoundRobinBalancer<>();
        mutable = new RoundRobinBalancer<>();
        initializeClients(configuration);
    }

    public TarantoolClient immutable() {
        return immutable.select();
    }

    public TarantoolClient mutable() {
        return mutable.select();
    }

    private void initializeClients(TarantoolStorageConfiguration configuration) {
        for (TarantoolClientConfiguration client : configuration.getClients()) {
            if (client.isImmutable()) {
                immutable.addEndpoint(new TarantoolClient(client));
                continue;
            }
            mutable.addEndpoint(new TarantoolClient(client));
        }
    }

    public void dispose() {
        immutable.endpoints().forEach(TarantoolClient::dispose);
        mutable.endpoints().forEach(TarantoolClient::dispose);
    }
}
