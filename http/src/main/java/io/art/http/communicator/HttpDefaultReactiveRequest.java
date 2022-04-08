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
public class HttpDefaultReactiveRequest {
    private final Flux<byte[]> input;

    public static HttpDefaultReactiveRequest json(Publisher<Object> value) {
        return create(value, JSON);
    }

    public static HttpDefaultReactiveRequest yaml(Publisher<Object> value) {
        return create(value, YAML);
    }

    public static HttpDefaultReactiveRequest messagePack(Publisher<Object> value) {
        return create(value, MESSAGE_PACK);
    }

    public static HttpDefaultReactiveRequest string(Publisher<String> value) {
        TransportPayloadWriter writer = transportPayloadWriter(STRING);
        return new HttpDefaultReactiveRequest(from(value).map(element -> releaseToByteArray(writer.write(new TypedObject(definition(element.getClass()), element)))));
    }

    public static HttpDefaultReactiveRequest bytes(Publisher<byte[]> value) {
        TransportPayloadWriter writer = transportPayloadWriter(BYTES);
        return new HttpDefaultReactiveRequest(from(value).map(element -> releaseToByteArray(writer.write(new TypedObject(definition(element.getClass()), element)))));
    }

    public static HttpDefaultReactiveRequest buffer(Publisher<ByteBuf> value) {
        return create(from(value).map(NettyBufferExtensions::releaseToByteArray), BYTES);
    }

    public static HttpDefaultReactiveRequest file(Publisher<File> files) {
        return create(from(files).map(file -> readFileBytes(file.toPath())), BYTES);
    }

    public static HttpDefaultReactiveRequest path(Publisher<Path> paths) {
        return create(from(paths).map(FileExtensions::readFileBytes), BYTES);
    }

    private static HttpDefaultReactiveRequest create(Publisher<Object> value, DataFormat dataFormat) {
        TransportPayloadWriter writer = transportPayloadWriter(dataFormat);
        return new HttpDefaultReactiveRequest(from(value).map(element -> releaseToByteArray(writer.write(new TypedObject(definition(element.getClass()), element)))));
    }
}
