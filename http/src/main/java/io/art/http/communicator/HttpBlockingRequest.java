package io.art.http.communicator;

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
public class HttpBlockingRequest {
    private final byte[] input;

    public static HttpBlockingRequest json(Object value) {
        return create(value, JSON);
    }

    public static HttpBlockingRequest yaml(Object value) {
        return create(value, YAML);
    }

    public static HttpBlockingRequest messagePack(Object value) {
        return create(value, MESSAGE_PACK);
    }

    public static HttpBlockingRequest string(String value) {
        return create(value, STRING);
    }

    public static HttpBlockingRequest bytes(byte[] value) {
        return create(value, BYTES);
    }

    public static HttpBlockingRequest buffer(ByteBuf value) {
        return create(releaseToByteArray(value), BYTES);
    }

    public static HttpBlockingRequest file(File file) {
        return create(readFileBytes(file.toPath()), BYTES);
    }

    public static HttpBlockingRequest path(Path path) {
        return create(readFileBytes(path), BYTES);
    }

    private static HttpBlockingRequest create(Object value, DataFormat dataFormat) {
        TransportPayloadWriter writer = transportPayloadWriter(dataFormat);
        byte[] bytes = releaseToByteArray(writer.write(new TypedObject(definition(value.getClass()), value)));
        return new HttpBlockingRequest(bytes);
    }
}
