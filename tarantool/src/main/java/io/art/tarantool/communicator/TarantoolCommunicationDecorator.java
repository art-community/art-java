package io.art.tarantool.communicator;

import io.art.core.annotation.*;
import lombok.*;
import static lombok.AccessLevel.*;

@Public
@Getter(value = PACKAGE)
public class TarantoolCommunicationDecorator {
    private boolean immutable;
    private boolean channel;

    public TarantoolCommunicationDecorator immutable() {
        immutable = true;
        return this;
    }

    public TarantoolCommunicationDecorator mutable() {
        immutable = false;
        return this;
    }

    public TarantoolCommunicationDecorator channel() {
        channel = true;
        return this;
    }
}
