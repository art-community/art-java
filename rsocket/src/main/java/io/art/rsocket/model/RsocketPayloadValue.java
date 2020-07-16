package io.art.rsocket.model;

import io.rsocket.*;
import lombok.*;
import io.art.entity.immutable.Value;

@Getter
@AllArgsConstructor
public class RsocketPayloadValue {
    private final Payload payload;
    private final Value value;
}
