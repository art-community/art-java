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
import io.art.core.extensions.*;
import io.art.json.descriptor.*;
import io.art.message.pack.descriptor.*;
import io.art.meta.model.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.yaml.descriptor.*;
import io.netty.buffer.*;
import lombok.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.json.module.JsonModule.*;
import static io.art.message.pack.module.MessagePackModule.*;
import static io.art.transport.payload.TransportPayload.*;
import static io.art.yaml.module.YamlModule.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor(access = PRIVATE)
public class TransportPayloadReader {
    private final DataFormat dataFormat;

    private final static Map<DataFormat, TransportPayloadReader> cache = map(DataFormat.values().length);

    @Getter(lazy = true, value = PRIVATE)
    private final BiFunction<ByteBuf, MetaType<?>, TransportPayload> reader = reader(dataFormat);

    @Getter(lazy = true, value = PRIVATE)
    private static final JsonReader jsonReader = jsonModule().configuration().getReader();

    @Getter(lazy = true, value = PRIVATE)
    private static final MessagePackReader messagePackReader = messagePackModule().configuration().getReader();

    @Getter(lazy = true, value = PRIVATE)
    private static final YamlReader yamlReader = yamlModule().configuration().getReader();

    public TransportPayload read(ByteBuf buffer, MetaType<?> type) {
        return getReader().apply(buffer, type);
    }

    private static BiFunction<ByteBuf, MetaType<?>, TransportPayload> reader(DataFormat dataFormat) {
        switch (dataFormat) {
            case JSON:
                return (buffer, type) -> buffer.capacity() == 0
                        ? emptyTransportPayload()
                        : new TransportPayload(buffer, lazy(() -> getJsonReader().read(type, buffer)));
            case MESSAGE_PACK:
                return (buffer, type) -> buffer.capacity() == 0
                        ? emptyTransportPayload()
                        : new TransportPayload(buffer, lazy(() -> getMessagePackReader().read(type, buffer)));
            case YAML:
                return (buffer, type) -> buffer.capacity() == 0
                        ? emptyTransportPayload()
                        : new TransportPayload(buffer, lazy(() -> getYamlReader().read(type, buffer)));
            case BYTES:
                return (buffer, type) -> buffer.capacity() == 0
                        ? emptyTransportPayload()
                        : new TransportPayload(buffer, lazy(() -> type.inputTransformer().fromByteArray(NettyBufferExtensions.toByteArray(buffer))));
            case STRING:
                return (buffer, type) -> buffer.capacity() == 0
                        ? emptyTransportPayload()
                        : new TransportPayload(buffer, lazy(() -> type.inputTransformer().fromString(NettyBufferExtensions.toString(buffer))));
        }
        throw new ImpossibleSituationException();
    }

    public static TransportPayloadReader transportPayloadReader(DataFormat dataFormat) {
        return putIfAbsent(cache, dataFormat, () -> new TransportPayloadReader(dataFormat));
    }
}
