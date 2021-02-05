package io.art.rsocket.refresher;

import io.art.core.changes.*;
import io.art.core.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.changes.ChangesListener.*;
import static io.art.core.changes.ChangesListenerRegistry.*;

@Getter
@Accessors(fluent = true)
public class RsocketModuleRefresher implements ModuleRefresher {
    private final ChangesListener serverListener = changesListener();
    private final ChangesListener serverLoggingListener = changesListener();
    private final ChangesListener communicatorListener = changesListener();
    private final ChangesListenerRegistry connectorListeners = changesListenerRegistry();
    private final Consumer consumer = new Consumer();

    @Getter
    @Accessors(fluent = true)
    public class Consumer {
        private final ChangesConsumer serverConsumer = serverListener.consumer();
        private final ChangesConsumer serverLoggingConsumer = serverLoggingListener.consumer();
        private final ChangesConsumerRegistry connectorConsumers = connectorListeners.getConsumers();
    }
}
