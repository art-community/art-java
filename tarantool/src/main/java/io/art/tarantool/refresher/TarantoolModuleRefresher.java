package io.art.tarantool.refresher;

import io.art.core.changes.*;
import io.art.core.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.changes.ChangesListenerRegistry.*;

@Getter
@Accessors(fluent = true)
public class TarantoolModuleRefresher implements ModuleRefresher {
    private final ChangesListenerRegistry clientListeners = changesListenerRegistry();
    private final ChangesListenerRegistry clusterListeners = changesListenerRegistry();
    private final Consumer consumer = new Consumer();

    public TarantoolModuleRefresher() {
        clientListeners.removed(id -> clientListeners.listenerFor(id).dispose());
        clusterListeners.removed(id -> clusterListeners.listenerFor(id).dispose());
    }

    public TarantoolModuleRefresher produce() {
        clientListeners.produce();
        clusterListeners.produce();
        return this;
    }

    @Getter
    @Accessors(fluent = true)
    public class Consumer {
        private final ChangesConsumerRegistry clientConsumers = clientListeners.getConsumers();
        private final ChangesConsumerRegistry clusterConsumers = clusterListeners.getConsumers();
    }
}
