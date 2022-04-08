package io.art.rsocket.communicator;

import io.art.core.annotation.*;
import io.art.core.extensions.*;
import io.art.rsocket.exception.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.transport.payload.*;
import io.netty.buffer.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.meta.Meta.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Errors.*;
import static io.art.transport.payload.TransportPayloadReader.*;
import static java.nio.file.Files.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.nio.file.*;

@Public
@Getter(value = PACKAGE)
public class RsocketBlockingResponse {
    private final RsocketDefaultCommunicator communicator;
    private final Flux<byte[]> output;
    private final RsocketReactiveResponse reactive;
    private final DataFormat dataFormat;

    RsocketBlockingResponse(RsocketDefaultCommunicator communicator, Flux<byte[]> output, DataFormat dataFormat) {
        this.communicator = communicator;
        this.output = output;
        this.dataFormat = dataFormat;
        reactive = new RsocketReactiveResponse(communicator, output, dataFormat);
    }

    public <T> T parse(Class<T> type) {
        return parse(blockFirst(output), type);
    }

    public String string() {
        return parse(blockFirst(output), String.class);
    }

    public byte[] bytes() {
        return parse(blockFirst(output), byte[].class);
    }

    public ByteBuf buffer() {
        return NettyBufferExtensions.from(bytes());
    }

    public Path download(Path path) {
        if (path.toFile().isDirectory()) {
            throw new RsocketException(format(WRITING_FILE_TO_DIRECTORY, path));
        }
        try {
            if (nonNull(path.getParent())) {
                createDirectories(path.getParent());
            }
            writeFile(path, bytes());
        } catch (Throwable throwable) {
            throw new RsocketException(throwable);
        }
        return path;
    }

    public RsocketReactiveResponse reactive() {
        return reactive;
    }

    public void dispose() {
        communicator.dispose();
    }

    private <T> T parse(byte[] bytes, Class<T> type) {
        TransportPayloadReader reader = transportPayloadReader(dataFormat);
        return cast(reader.read(definition(type), NettyBufferExtensions.from(bytes)).getValue());
    }
}
