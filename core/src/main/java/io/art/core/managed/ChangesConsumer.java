package io.art.core.managed;

import lombok.*;

@RequiredArgsConstructor
public class ChangesConsumer {
    private final ChangesListener listener;

    public ChangesConsumer consume(Runnable action) {
        listener.consume(action);
        return this;
    }

    public <T> ManagedValue<T> consume(ManagedValue<T> value) {
        listener.consume(value);
        return value;
    }
}
