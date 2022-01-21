package io.art.http.communicator;

import io.art.core.annotation.*;
import io.art.core.extensions.*;
import io.art.meta.model.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.transport.payload.*;
import io.netty.buffer.*;
import lombok.*;
import org.reactivestreams.*;
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
public class HttpReactiveRequest {
    private final Flux<byte[]> input;

    public static HttpReactiveRequest json(Publisher<Object> value) {
        return create(value, JSON);
    }

    public static HttpReactiveRequest yaml(Publisher<Object> value) {
        return create(value, YAML);
    }

    public static HttpReactiveRequest messagePack(Publisher<Object> value) {
        return create(value, MESSAGE_PACK);
    }

    public static HttpReactiveRequest string(Publisher<String> value) {
        TransportPayloadWriter writer = transportPayloadWriter(STRING);
        return new HttpReactiveRequest(from(value).map(element -> toByteArray(writer.write(new TypedObject(definition(element.getClass()), element)))));
    }

    public static HttpReactiveRequest bytes(Publisher<byte[]> value) {
        TransportPayloadWriter writer = transportPayloadWriter(BYTES);
        return new HttpReactiveRequest(from(value).map(element -> toByteArray(writer.write(new TypedObject(definition(element.getClass()), element)))));
    }

    public static HttpReactiveRequest buffer(Publisher<ByteBuf> value) {
        return create(from(value).map(NettyBufferExtensions::toByteArray), BYTES);
    }

    public static HttpReactiveRequest file(Publisher<File> files) {
        return create(from(files).map(file -> readFileBytes(file.toPath())), BYTES);
    }

    public static HttpReactiveRequest path(Publisher<Path> paths) {
        return create(from(paths).map(FileExtensions::readFileBytes), BYTES);
    }

    private static HttpReactiveRequest create(Publisher<Object> value, DataFormat dataFormat) {
        TransportPayloadWriter writer = transportPayloadWriter(dataFormat);
        return new HttpReactiveRequest(from(value).map(element -> toByteArray(writer.write(new TypedObject(definition(element.getClass()), element)))));
    }
}
