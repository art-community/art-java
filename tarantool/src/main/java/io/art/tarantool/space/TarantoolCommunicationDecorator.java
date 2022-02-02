package io.art.tarantool.space;

import io.art.core.annotation.*;
import lombok.*;
import static lombok.AccessLevel.*;

@Public
@Getter(value = PACKAGE)
public class TarantoolCommunicationDecorator {
    private boolean immutable;

    public TarantoolCommunicationDecorator immutable() {
        immutable = true;
        return this;
    }

    public TarantoolCommunicationDecorator mutable() {
        immutable = false;
        return this;
    }
}
