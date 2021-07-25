package io.art.communicator.refresher;

import io.art.core.changes.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.changes.ChangesListener.*;

@Getter
@Accessors(fluent = true)
public class CommunicatorRefresher {
    private final ChangesListener deactivationListener = changesListener();
    private final ChangesListener loggingListener = changesListener();
    private final ChangesListener resilienceListener = changesListener();
    private final Consumer consumer = new Consumer();

    public void produce() {
        loggingListener.produce();
        deactivationListener.produce();
        resilienceListener.produce();
    }

    @Getter
    @Accessors(fluent = true)
    public class Consumer {
        private final ChangesConsumer loggingConsumer = loggingListener.consumer();
        private final ChangesConsumer deactivationConsumer = deactivationListener.consumer();
        private final ChangesConsumer resilienceConsumer = resilienceListener.consumer();
    }
}
