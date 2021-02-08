package io.art.core.changes;

import lombok.*;

@RequiredArgsConstructor
public class ChangesConsumerRegistry {
    private final ChangesListenerRegistry listeners;

    public ChangesConsumer countConsumer() {
        return listeners.getCountListener().consumer();
    }

    public ChangesConsumer consumerFor(String id) {
        return listeners.listenerFor(id).consumer();
    }
}
