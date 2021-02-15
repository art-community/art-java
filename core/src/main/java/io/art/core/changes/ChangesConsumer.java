package io.art.core.changes;

import io.art.core.property.*;
import lombok.*;

@RequiredArgsConstructor
public class ChangesConsumer {
    private final ChangesListener listener;

    public ChangesConsumer consume(Runnable action) {
        listener.consume(action);
        return this;
    }

    public ChangesConsumer consume(Runnable onChange, Runnable onDispose){
        listener.consume(onChange, onDispose);
        return this;
    }

    public <T> Property<T> consume(Property<T> value) {
        listener.consume(value);
        return value;
    }
}
