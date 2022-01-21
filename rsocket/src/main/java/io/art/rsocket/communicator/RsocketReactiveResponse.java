package io.art.rsocket.communicator;

import io.art.core.annotation.*;
import io.art.core.extensions.*;
import io.art.transport.constants.TransportModuleConstants.*;
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
public class RsocketReactiveResponse {
    private final RsocketDefaultCommunicator communicator;
    private final Flux<byte[]> output;
    private final DataFormat dataFormat;

    public <T> Flux<T> parse(Class<T> type) {
        return output.map(element -> parse(element, dataFormat, type));
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

    public void dispose() {
        communicator.dispose();
    }

    private static <T> T parse(byte[] bytes, DataFormat dataFormat, Class<T> type) {
        TransportPayloadReader reader = transportPayloadReader(dataFormat);
        return cast(reader.read(NettyBufferExtensions.from(bytes), definition(type)).getValue());
    }
}
