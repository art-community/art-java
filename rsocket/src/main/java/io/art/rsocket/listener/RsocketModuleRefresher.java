package io.art.rsocket.listener;

import io.art.core.changes.*;
import io.art.core.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.changes.ChangesListener.*;

@Getter
@Accessors(fluent = true)
public class RsocketModuleRefresher implements ModuleRefresher {
    private final ChangesListener serverListener = changesListener();
    private final ChangesListener serverLoggingListener = changesListener();
    private final Consumer consumer = new Consumer();

    @Getter
    @Accessors(fluent = true)
    public class Consumer {
        private final ChangesConsumer serverConsumer = serverListener.consumer();
        private final ChangesConsumer serverLoggingConsumer = serverLoggingListener.consumer();
    }
}
