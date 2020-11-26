package io.art.rsocket.model;

import io.rsocket.*;
import lombok.*;
import io.art.value.immutable.Value;

@Getter
@AllArgsConstructor
public class RsocketPayloadValue {
    private final Payload payload;
    private final Value value;
}
