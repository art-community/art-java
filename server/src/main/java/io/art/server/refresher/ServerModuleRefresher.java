package io.art.server.refresher;

import io.art.core.changes.*;
import io.art.core.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.changes.ChangesListener.*;

@Getter
@Accessors(fluent = true)
public class ServerModuleRefresher implements ModuleRefresher {
    private final ChangesListener serverLoggingListener = changesListener();
    private final Consumer consumer = new Consumer();

    public void produce() {
        serverLoggingListener.produce();
    }

    @Getter
    @Accessors(fluent = true)
    public class Consumer {
        private final ChangesConsumer serverLoggingConsumer = serverLoggingListener.consumer();
    }
}
