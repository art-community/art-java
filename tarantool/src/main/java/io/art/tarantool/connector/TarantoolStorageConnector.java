package io.art.tarantool.connector;

import io.art.core.collection.*;
import io.art.core.network.balancer.*;
import io.art.tarantool.client.*;
import io.art.tarantool.configuration.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.tarantool.module.TarantoolModule.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;


public class TarantoolStorageConnector {
    private final Balancer<TarantoolClient> immutable;
    private final Balancer<TarantoolClient> routers;
    private final Balancer<TarantoolClient> mutable;
    private final String storageId;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public TarantoolStorageConnector(String storageId) {
        this.storageId = storageId;
        immutable = new RoundRobinBalancer<>();
        mutable = new RoundRobinBalancer<>();
        routers = new RoundRobinBalancer<>();
    }

    public TarantoolClient immutable() {
        return immutable.select();
    }

    public TarantoolClient router() {
        return routers.select();
    }

    public TarantoolClient mutable() {
        return mutable.select();
    }

    public List<TarantoolClient> all() {
        return combineToList(immutable.endpoints(), mutable.endpoints(), routers.endpoints());
    }

    public void each(Consumer<TarantoolClient> consumer) {
        all().forEach(consumer);
    }

    public void initialize() {
        if (initialized.compareAndSet(false, true)) {
            TarantoolModuleConfiguration configuration = tarantoolModule().configuration();
            ImmutableSet<TarantoolClientConfiguration> clients = configuration.storageConfiguration(storageId).getClients();
            for (TarantoolClientConfiguration client : clients) {
                if (client.isRouter()) {
                    routers.addEndpoint(new TarantoolClient(client, configuration));
                    continue;
                }

                if (client.isImmutable()) {
                    immutable.addEndpoint(new TarantoolClient(client, configuration));
                    continue;
                }
                immutable.addEndpoint(new TarantoolClient(client, configuration));
                mutable.addEndpoint(new TarantoolClient(client, configuration));
            }
        }
    }

    public void dispose() {
        immutable.endpoints().forEach(TarantoolClient::dispose);
        mutable.endpoints().forEach(TarantoolClient::dispose);
        routers.endpoints().forEach(TarantoolClient::dispose);
    }

    public boolean hasRouters() {
        return !routers.endpoints().isEmpty();
    }

    public boolean hasMutable() {
        return !mutable.endpoints().isEmpty();
    }

    public boolean hasImmutable() {
        return !immutable.endpoints().isEmpty();
    }
}
