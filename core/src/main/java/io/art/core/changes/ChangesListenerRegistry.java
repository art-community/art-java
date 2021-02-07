package io.art.core.changes;

import lombok.*;
import static io.art.core.changes.ChangesListener.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

public class ChangesListenerRegistry {
    private final Map<String, ChangesListener> listeners = concurrentMap();

    @Getter
    private final ChangesListener countListener = changesListener();

    @Getter
    private final ChangesConsumerRegistry consumers = new ChangesConsumerRegistry(this);

    @Getter
    private final ChangesProducerRegistry producers = new ChangesProducerRegistry(this);


    public ChangesListener listenerFor(String id) {
        if (!listeners.containsKey(id)) {
            countListener.emit(listeners.size() + 1);
            countListener.produce();
        }
        return putIfAbsent(listeners, id, ChangesListener::changesListener);
    }

    public ChangesListenerRegistry produce() {
        listeners.values().forEach(ChangesListener::produce);
        return this;
    }

    public static ChangesListenerRegistry changesListenerRegistry() {
        return new ChangesListenerRegistry();
    }
}
