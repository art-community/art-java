/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.transport.payload;

import io.art.core.exception.*;
import io.art.json.descriptor.*;
import io.art.message.pack.descriptor.*;
import io.art.meta.model.*;
import io.art.transport.configuration.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.yaml.descriptor.*;
import io.netty.buffer.*;
import lombok.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.json.module.JsonModule.*;
import static io.art.message.pack.module.MessagePackModule.*;
import static io.art.transport.module.TransportModule.*;
import static io.art.yaml.module.YamlModule.*;
import static io.netty.buffer.ByteBufAllocator.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@RequiredArgsConstructor(access = PRIVATE)
public class TransportPayloadWriter {
    private final DataFormat dataFormat;

    @Getter(lazy = true, value = PRIVATE)
    private final Function<TypedObject, ByteBuf> writer = writer(dataFormat);

    @Getter(lazy = true, value = PRIVATE)
    private static final TransportModuleConfiguration configuration = transportModule().configuration();

    @Getter(lazy = true, value = PRIVATE)
    private static final JsonWriter jsonWriter = jsonModule().configuration().getWriter();

    @Getter(lazy = true, value = PRIVATE)
    private static final MessagePackWriter messagePackWriter = messagePackModule().configuration().getWriter();

    @Getter(lazy = true, value = PRIVATE)
    private static final YamlWriter yamlWriter = yamlModule().configuration().getWriter();

    public ByteBuf write(TypedObject value) {
        return getWriter().apply(value);
    }

    private static Function<TypedObject, ByteBuf> writer(DataFormat dataFormat) {
        switch (dataFormat) {
            case JSON:
                return value -> {
                    ByteBuf buffer = createBuffer();
                    getJsonWriter().write(value, buffer);
                    return buffer;
                };
            case MESSAGE_PACK:
                return value -> {
                    ByteBuf buffer = createBuffer();
                    getMessagePackWriter().write(value, buffer);
                    return buffer;
                };
            case YAML:
                return value -> {
                    ByteBuf buffer = createBuffer();
                    getYamlWriter().write(value, buffer);
                    return buffer;
                };
        }
        throw new ImpossibleSituationException();
    }

    private static ByteBuf createBuffer() {
        if (!withTransport()) return DEFAULT.ioBuffer();
        TransportModuleConfiguration configuration = getConfiguration();
        int writeBufferInitialCapacity = configuration.getWriteBufferInitialCapacity();
        int writeBufferMaxCapacity = configuration.getWriteBufferMaxCapacity();
        switch (configuration.getWriteBufferType()) {
            case DEFAULT:
                return DEFAULT.buffer(writeBufferInitialCapacity, writeBufferMaxCapacity);
            case HEAP:
                return DEFAULT.heapBuffer(writeBufferInitialCapacity, writeBufferMaxCapacity);
            case IO:
                return DEFAULT.ioBuffer(writeBufferInitialCapacity, writeBufferMaxCapacity);
            case DIRECT:
                return DEFAULT.directBuffer(writeBufferInitialCapacity, writeBufferMaxCapacity);
        }
        return DEFAULT.ioBuffer();
    }

    public static TransportPayloadWriter transportPayloadWriter(DataFormat dataFormat) {
        return new TransportPayloadWriter(dataFormat);
    }
}
