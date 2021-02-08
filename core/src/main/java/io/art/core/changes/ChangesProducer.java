package io.art.core.changes;

import lombok.*;

@RequiredArgsConstructor
public class ChangesProducer {
    private final ChangesListener listener;

    public ChangesProducer produce() {
        listener.produce();
        return this;
    }

    public <T> T emit(T value) {
        return listener.emit(value);
    }
}
