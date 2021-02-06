package io.art.core.changes;

import lombok.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

public class ChangesListenerRegistry {
    private final Map<String, ChangesListener> listeners = concurrentMap();
    @Getter
    private final ChangesConsumerRegistry consumers = new ChangesConsumerRegistry(this);

    public ChangesListener listener(String id) {
        return putIfAbsent(listeners, id, ChangesListener::changesListener);
    }

    public static ChangesListenerRegistry changesListenerRegistry() {
        return new ChangesListenerRegistry();
    }
}
