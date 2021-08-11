package io.art.core.changes;

import lombok.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;
import java.util.function.*;

public class ChangesListenerRegistry {
    private final Map<String, ChangesListener> listeners = concurrentMap();
    private final List<Consumer<String>> added = linkedList();
    private final List<Consumer<String>> removed = linkedList();

    @Getter
    private final ChangesConsumerRegistry consumers = new ChangesConsumerRegistry(this);

    @Getter
    private final ChangesProducerRegistry producers = new ChangesProducerRegistry(this);


    public ChangesListenerRegistry added(Consumer<String> action) {
        added.add(action);
        return this;
    }

    public ChangesListenerRegistry removed(Consumer<String> action) {
        removed.add(action);
        return this;
    }

    public ChangesListenerRegistry update(Set<String> ids) {
        ids.stream()
                .filter(id -> !listeners.containsKey(id))
                .forEach(id -> added.forEach(consumer -> consumer.accept(id)));
        listeners.keySet().stream()
                .filter(id -> !ids.contains(id))
                .forEach(id -> removed.forEach(consumer -> consumer.accept(id)));
        return this;
    }

    public ChangesListener listenerFor(String id) {
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
