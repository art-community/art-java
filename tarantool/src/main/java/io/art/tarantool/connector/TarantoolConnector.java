package io.art.tarantool.connector;

import io.art.core.network.balancer.*;
import io.art.tarantool.client.*;
import io.art.tarantool.configuration.*;
import lombok.*;


public class TarantoolConnector {
    @Getter
    private final String id;
    private final Balancer<TarantoolClient> immutable;
    private final Balancer<TarantoolClient> mutable;

    public TarantoolConnector(String id, TarantoolConnectorConfiguration configuration) {
        this.id = id;
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

    private void initializeClients(TarantoolConnectorConfiguration configuration) {
        for (TarantoolClientConfiguration client : configuration.getClients().values()) {
            if (client.isImmutable()) {
                immutable.addEndpoint(new TarantoolClient(client));
                continue;
            }
            mutable.addEndpoint(new TarantoolClient(client));
        }
    }
}
