package io.art.rsocket.communicator;

import io.art.core.annotation.*;
import io.art.core.extensions.*;
import io.art.meta.model.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.transport.payload.*;
import io.netty.buffer.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.NettyBufferExtensions.*;
import static io.art.meta.Meta.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import static io.art.transport.payload.TransportPayloadWriter.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.from;
import java.io.*;
import java.nio.file.*;

@Public
@Getter(value = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
public class RsocketMonoRequest {
    private final Mono<byte[]> input;
    private final DataFormat dataFormat;

    public static RsocketMonoRequest json(Mono<Object> value) {
        return create(value, JSON);
    }

    public static RsocketMonoRequest yaml(Mono<Object> value) {
        return create(value, YAML);
    }

    public static RsocketMonoRequest messagePack(Mono<Object> value) {
        return create(value, MESSAGE_PACK);
    }

    public static RsocketMonoRequest string(Mono<String> value) {
        TransportPayloadWriter writer = transportPayloadWriter(STRING);
        Mono<byte[]> input = value.map(element -> toByteArray(writer.write(new TypedObject(definition(element.getClass()), element))));
        return new RsocketMonoRequest(input, STRING);
    }

    public static RsocketMonoRequest bytes(Mono<byte[]> value) {
        TransportPayloadWriter writer = transportPayloadWriter(BYTES);
        Mono<byte[]> input = value.map(element -> toByteArray(writer.write(new TypedObject(definition(element.getClass()), element))));
        return new RsocketMonoRequest(input, BYTES);
    }

    public static RsocketMonoRequest buffer(Mono<ByteBuf> value) {
        return create(value.map(NettyBufferExtensions::toByteArray), BYTES);
    }

    public static RsocketMonoRequest file(Mono<File> file) {
        return create(file.map(input -> readFileBytes(input.toPath())), BYTES);
    }

    public static RsocketMonoRequest path(Mono<Path> path) {
        return create(path.map(FileExtensions::readFileBytes), BYTES);
    }

    private static RsocketMonoRequest create(Mono<Object> value, DataFormat dataFormat) {
        TransportPayloadWriter writer = transportPayloadWriter(dataFormat);
        Mono<byte[]> input = value.map(element -> toByteArray(writer.write(new TypedObject(definition(element.getClass()), element))));
        return new RsocketMonoRequest(input, dataFormat);
    }
}
