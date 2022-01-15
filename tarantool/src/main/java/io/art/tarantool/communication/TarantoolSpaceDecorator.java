package io.art.tarantool.communication;

import io.art.core.annotation.*;
import lombok.*;
import static lombok.AccessLevel.*;

@Public
@Getter(value = PACKAGE)
public class TarantoolSpaceDecorator {
    private boolean immutable;

    public TarantoolSpaceDecorator immutable() {
        immutable = true;
        return this;
    }

    public TarantoolSpaceDecorator mutable() {
        immutable = false;
        return this;
    }
}
