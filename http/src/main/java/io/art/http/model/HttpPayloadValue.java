package io.art.http.model;

import io.art.value.immutable.Value;
import io.netty.buffer.*;
import lombok.*;

@Getter
@AllArgsConstructor
public class HttpPayloadValue {
    private final ByteBuf payload;
    private final Value value;

    private static final HttpPayloadValue EMPTY = new HttpPayloadValue(null, null);

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public static HttpPayloadValue emptyHttpPayload() {
        return EMPTY;
    }
}
