package io.art.server.refresher;

import io.art.core.changes.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.changes.ChangesListener.*;

@Getter
@Accessors(fluent = true)
public class ServerRefresher {
    private final ChangesListener deactivationListener = changesListener();
    private final ChangesListener loggingListener = changesListener();
    private final ChangesListener validationListener = changesListener();
    private final Consumer consumer = new Consumer();

    public void produce() {
        loggingListener.produce();
        deactivationListener.produce();
        validationListener.produce();
    }



    @Getter
    @Accessors(fluent = true)
    public class Consumer {
        private final ChangesConsumer deactivationConsumer = deactivationListener.consumer();
        private final ChangesConsumer loggingConsumer = loggingListener.consumer();
        private final ChangesConsumer validationConsumer = validationListener.consumer();
    }
}
