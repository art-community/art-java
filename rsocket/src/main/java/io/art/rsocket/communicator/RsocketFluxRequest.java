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
import java.io.*;
import java.nio.file.*;

@Public
@Getter(value = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
public class RsocketFluxRequest {
    private final Flux<byte[]> input;
    private final DataFormat dataFormat;

    public static RsocketFluxRequest json(Flux<Object> value) {
        return create(value, JSON);
    }

    public static RsocketFluxRequest yaml(Flux<Object> value) {
        return create(value, YAML);
    }

    public static RsocketFluxRequest messagePack(Flux<Object> value) {
        return create(value, MESSAGE_PACK);
    }

    public static RsocketFluxRequest string(Flux<String> value) {
        TransportPayloadWriter writer = transportPayloadWriter(STRING);
        Flux<byte[]> input = value.map(element -> toByteArray(writer.write(new TypedObject(definition(element.getClass()), element))));
        return new RsocketFluxRequest(input, STRING);
    }

    public static RsocketFluxRequest bytes(Flux<byte[]> value) {
        TransportPayloadWriter writer = transportPayloadWriter(BYTES);
        Flux<byte[]> input = value.map(element -> toByteArray(writer.write(new TypedObject(definition(element.getClass()), element))));
        return new RsocketFluxRequest(input, BYTES);
    }

    public static RsocketFluxRequest buffer(Flux<ByteBuf> value) {
        return create(value.map(NettyBufferExtensions::toByteArray), BYTES);
    }

    public static RsocketFluxRequest file(Flux<File> files) {
        return create(files.map(file -> readFileBytes(file.toPath())), BYTES);
    }

    public static RsocketFluxRequest path(Flux<Path> paths) {
        return create(paths.map(FileExtensions::readFileBytes), BYTES);
    }

    private static RsocketFluxRequest create(Flux<Object> value, DataFormat dataFormat) {
        TransportPayloadWriter writer = transportPayloadWriter(dataFormat);
        Flux<byte[]> input = value.map(element -> toByteArray(writer.write(new TypedObject(definition(element.getClass()), element))));
        return new RsocketFluxRequest(input, dataFormat);
    }
}
