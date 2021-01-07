package io.art.rsocket.model;

import io.art.value.immutable.Value;
import io.rsocket.*;
import lombok.*;

@Getter
@AllArgsConstructor
public class RsocketPayloadValue {
    private final Payload payload;
    private final Value value;

    private static final RsocketPayloadValue EMPTY = new RsocketPayloadValue(null, null);

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public static RsocketPayloadValue emptyRsocketPayload() {
        return EMPTY;
    }
}
