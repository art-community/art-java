package io.art.rsocket.communicator;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.transport.payload.*;
import io.netty.buffer.*;
import lombok.*;
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
public class RsocketBlockingRequest {
    private final byte[] input;
    private final DataFormat dataFormat;

    public static RsocketBlockingRequest json(Object value) {
        return create(value, JSON);
    }

    public static RsocketBlockingRequest yaml(Object value) {
        return create(value, YAML);
    }

    public static RsocketBlockingRequest messagePack(Object value) {
        return create(value, MESSAGE_PACK);
    }

    public static RsocketBlockingRequest string(String value) {
        return create(value, STRING);
    }

    public static RsocketBlockingRequest bytes(byte[] value) {
        return create(value, BYTES);
    }

    public static RsocketBlockingRequest buffer(ByteBuf value) {
        return create(releaseToByteArray(value), BYTES);
    }

    public static RsocketBlockingRequest file(File file) {
        return create(readFileBytes(file.toPath()), BYTES);
    }

    public static RsocketBlockingRequest path(Path path) {
        return create(readFileBytes(path), BYTES);
    }

    private static RsocketBlockingRequest create(Object value, DataFormat dataFormat) {
        TransportPayloadWriter writer = transportPayloadWriter(dataFormat);
        byte[] bytes = releaseToByteArray(writer.write(new TypedObject(definition(value.getClass()), value)));
        return new RsocketBlockingRequest(bytes, dataFormat);
    }
}
