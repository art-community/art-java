package io.art.rsocket.model;

import io.rsocket.*;
import lombok.*;
import io.art.entity.Value;

@Getter
@AllArgsConstructor
public class RsocketPayloadValue {
    private final Payload payload;
    private final Value value;
}
