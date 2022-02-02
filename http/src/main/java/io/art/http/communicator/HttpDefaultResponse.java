package io.art.http.communicator;

import io.art.core.annotation.*;
import io.art.core.extensions.*;
import io.art.http.exception.*;
import io.art.transport.constants.*;
import io.art.transport.payload.*;
import io.netty.buffer.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.http.constants.HttpModuleConstants.Errors.*;
import static io.art.meta.Meta.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import static io.art.transport.payload.TransportPayloadReader.*;
import static java.nio.file.Files.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.nio.file.*;

@Public
@Getter(value = PACKAGE)
public class HttpDefaultResponse {
    private final HttpDefaultCommunicator communicator;
    private final Flux<byte[]> output;
    private final HttpReactiveResponse reactive;

    HttpDefaultResponse(HttpDefaultCommunicator communicator, Flux<byte[]> output) {
        this.communicator = communicator;
        this.output = output;
        reactive = new HttpReactiveResponse(output);
    }

    public <T> T json(Class<T> type) {
        return parse(blockFirst(output), JSON, type);
    }

    public <T> T yaml(Class<T> type) {
        return parse(blockFirst(output), YAML, type);
    }

    public <T> T messagePack(Class<T> type) {
        return parse(blockFirst(output), MESSAGE_PACK, type);
    }

    public String string() {
        return parse(blockFirst(output), STRING, String.class);
    }

    public byte[] bytes() {
        return parse(blockFirst(output), BYTES, byte[].class);
    }

    public ByteBuf buffer() {
        return NettyBufferExtensions.from(bytes());
    }

    public Path download(Path path) {
        if (path.toFile().isDirectory()) {
            throw new HttpException(format(WRITING_FILE_TO_DIRECTORY, path));
        }
        try {
            if (nonNull(path.getParent())) {
                createDirectories(path.getParent());
            }
            writeFile(path, bytes());
        } catch (Throwable throwable) {
            throw new HttpException(throwable);
        }
        return path;
    }

    public HttpReactiveResponse reactive() {
        return reactive;
    }

    public void dispose() {
        communicator.dispose();
    }

    private static <T> T parse(byte[] bytes, TransportModuleConstants.DataFormat dataFormat, Class<T> type) {
        TransportPayloadReader reader = transportPayloadReader(dataFormat);
        return cast(reader.read(definition(type), NettyBufferExtensions.from(bytes)).getValue());
    }
}
