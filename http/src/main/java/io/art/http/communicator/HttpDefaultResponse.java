package io.art.http.communicator;

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
import static lombok.AccessLevel.*;
import java.nio.file.*;

@Getter(value = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
public class HttpDefaultResponse {
    private final Flux<byte[]> output;

    public <T> T json(Class<T> type) {
        return parse(blockFirst(output), JSON, type);
    }

    public <T> T yaml(Class<T> type) {
        return parse(blockFirst(output), JSON, type);
    }

    public <T> T messagePack(Class<T> type) {
        return parse(blockFirst(output), JSON, type);
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
            createDirectories(path.getParent());
            writeFile(path, bytes());
        } catch (Throwable throwable) {
            throw new HttpException(throwable);
        }
        return path;
    }

    private static <T> T parse(byte[] bytes, TransportModuleConstants.DataFormat dataFormat, Class<T> type) {
        TransportPayloadReader reader = transportPayloadReader(dataFormat);
        return cast(reader.read(NettyBufferExtensions.from(bytes), definition(type)).getValue());
    }
}
