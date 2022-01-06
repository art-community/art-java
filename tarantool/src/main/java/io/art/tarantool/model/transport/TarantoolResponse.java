package io.art.tarantool.model.transport;

import lombok.*;
import org.msgpack.value.Value;

@Getter
public class TarantoolResponse {
    private final TarantoolHeader header;
    private final boolean error;
    private Value body;

    public TarantoolResponse(TarantoolHeader header, boolean error, Value body) {
        this.header = header;
        this.error = error;
        this.body = body;
    }

    public TarantoolResponse(TarantoolHeader header, boolean error) {
        this.header = header;
        this.error = error;
    }
}