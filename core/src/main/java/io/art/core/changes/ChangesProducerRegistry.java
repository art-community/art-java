package io.art.core.changes;

import lombok.*;

@RequiredArgsConstructor
public class ChangesProducerRegistry {
    private final ChangesListenerRegistry listeners;

    public ChangesProducer countProducer() {
        return listeners.getCountListener().producer();
    }

    public ChangesProducer producerFor(String id) {
        return listeners.listenerFor(id).producer();
    }
}
