package io.art.http.refresher;

import io.art.core.changes.*;
import io.art.core.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.changes.ChangesListener.*;
import static io.art.core.changes.ChangesListenerRegistry.*;

@Getter
@Accessors(fluent = true)
public class HttpModuleRefresher implements ModuleRefresher {
    private final ChangesListener serverListener = changesListener();
    private final ChangesListener serverLoggingListener = changesListener();
    private final ChangesListenerRegistry connectorListeners = changesListenerRegistry();
    private final ChangesListenerRegistry connectorLoggingListeners = changesListenerRegistry();
    private final Consumer consumer = new Consumer();

    public void produce() {
        serverListener.produce();
        serverLoggingListener.produce();
        connectorListeners.produce();
        connectorLoggingListeners.produce();
    }

    @Getter
    @Accessors(fluent = true)
    public class Consumer {
        private final ChangesConsumer serverConsumer = serverListener.consumer();
        private final ChangesConsumer serverLoggingConsumer = serverLoggingListener.consumer();
        private final ChangesConsumerRegistry connectorConsumers = connectorListeners.getConsumers();
        private final ChangesConsumerRegistry connectorLoggingConsumers = connectorLoggingListeners.getConsumers();
    }
}
