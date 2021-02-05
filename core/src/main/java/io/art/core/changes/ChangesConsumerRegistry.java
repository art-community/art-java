package io.art.core.changes;

import lombok.*;

@RequiredArgsConstructor
public class ChangesConsumerRegistry {
    private final ChangesListenerRegistry listeners;

    public ChangesConsumer consumer(String id) {
        return listeners.listener(id).consumer();
    }

}
