package io.art.http.communicator;

import io.art.core.annotation.*;
import io.art.core.extensions.*;
import io.art.transport.constants.*;
import io.art.transport.payload.*;
import io.netty.buffer.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.meta.Meta.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import static io.art.transport.payload.TransportPayloadReader.*;
import static lombok.AccessLevel.*;

@Public
@Getter(value = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
public class HttpReactiveResponse {
    public <T> Flux<T> json(Class<T> type) {
        return output.map(element -> parse(element, JSON, type));
    }

    private final Flux<byte[]> output;

    public <T> Flux<T> yaml(Class<T> type) {
        return output.map(element -> parse(element, YAML, type));
    }

    public <T> Flux<T> messagePack(Class<T> type) {
        return output.map(element -> parse(element, MESSAGE_PACK, type));
    }

    public Flux<String> string() {
        return output.map(element -> parse(element, STRING, String.class));
    }

    public Flux<byte[]> bytes() {
        return output.map(element -> parse(element, BYTES, byte[].class));
    }

    public Flux<ByteBuf> buffer() {
        return output.map(element -> NettyBufferExtensions.from(parse(element, BYTES, byte[].class)));
    }

    private static <T> T parse(byte[] bytes, TransportModuleConstants.DataFormat dataFormat, Class<T> type) {
        TransportPayloadReader reader = transportPayloadReader(dataFormat);
        return cast(reader.read(NettyBufferExtensions.from(bytes), definition(type)).getValue());
    }
}
