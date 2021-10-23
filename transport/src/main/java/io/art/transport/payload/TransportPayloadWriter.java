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
import io.art.core.property.*;
import io.art.json.descriptor.*;
import io.art.message.pack.descriptor.*;
import io.art.meta.model.*;
import io.art.transport.configuration.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.yaml.descriptor.*;
import io.netty.buffer.*;
import static io.art.core.builder.MapBuilder.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.context.Context.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.json.module.JsonModule.*;
import static io.art.message.pack.module.MessagePackModule.*;
import static io.art.transport.allocator.WriteBufferAllocator.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import static io.art.transport.module.TransportModule.*;
import static io.art.yaml.module.YamlModule.*;
import java.util.*;
import java.util.function.*;

public class TransportPayloadWriter {
    private final LazyProperty<Function<TypedObject, ByteBuf>> writer;

    private TransportPayloadWriter(DataFormat dataFormat) {
        writer = lazy(() -> writer(dataFormat));
    }

    private static final LazyProperty<TransportModuleConfiguration> configuration = lazy(() -> transportModule().configuration());
    private static final LazyProperty<JsonWriter> jsonWriter = lazy(() -> jsonModule().configuration().getWriter());
    private static final LazyProperty<MessagePackWriter> messagePackWriter = lazy(() -> messagePackModule().configuration().getWriter());
    private static final LazyProperty<YamlWriter> yamlWriter = lazy(() -> yamlModule().configuration().getWriter());
    private final static Map<DataFormat, TransportPayloadWriter> cache = mapBuilder(JSON, new TransportPayloadWriter(JSON))
            .with(MESSAGE_PACK, new TransportPayloadWriter(MESSAGE_PACK))
            .with(YAML, new TransportPayloadWriter(YAML))
            .with(STRING, new TransportPayloadWriter(STRING))
            .with(BYTES, new TransportPayloadWriter(BYTES))
            .build();


    public ByteBuf write(TypedObject value) {
        return writer.get().apply(value);
    }

    private static Function<TypedObject, ByteBuf> writer(DataFormat dataFormat) {
        switch (dataFormat) {
            case JSON:
                return value -> {
                    ByteBuf buffer = allocateWriteBuffer(configuration.get());
                    jsonWriter.get().write(value, buffer);
                    return buffer;
                };
            case MESSAGE_PACK:
                return value -> {
                    ByteBuf buffer = allocateWriteBuffer(configuration.get());
                    messagePackWriter.get().write(value, buffer);
                    return buffer;
                };
            case YAML:
                return value -> {
                    ByteBuf buffer = allocateWriteBuffer(configuration.get());
                    yamlWriter.get().write(value, buffer);
                    return buffer;
                };
            case BYTES:
                return value -> allocateWriteBuffer(configuration.get()).writeBytes(value.getType()
                        .outputTransformer()
                        .toByteArray(cast(value.getObject())));
            case STRING:
                return value -> allocateWriteBuffer(configuration.get()).writeBytes(value.getType()
                        .outputTransformer()
                        .toString(cast(value.getObject()))
                        .getBytes(context().configuration().getCharset()));
        }
        throw new ImpossibleSituationException();
    }

    public static TransportPayloadWriter transportPayloadWriter(DataFormat dataFormat) {
        return cache.get(dataFormat);
    }
}
