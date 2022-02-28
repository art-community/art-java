package io.art.tarantool.model;

import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;

@Getter
@ToString
public class TarantoolResponse {
    private final TarantoolHeader header;
    private final boolean error;
    private final boolean chunk;
    private Value body;

    public TarantoolResponse(TarantoolHeader header, Value body) {
        IntegerValue code = header.getCode();
        this.header = header;
        this.error = !code.equals(IPROTO_CHUNK) && !code.equals(IPROTO_OK);
        this.chunk = code.equals(IPROTO_CHUNK);
        this.body = body;
    }

    public TarantoolResponse(TarantoolHeader header) {
        IntegerValue code = header.getCode();
        this.header = header;
        this.error = !code.equals(IPROTO_CHUNK) && !code.equals(IPROTO_OK);
        this.chunk = code.equals(IPROTO_CHUNK);
    }
}
