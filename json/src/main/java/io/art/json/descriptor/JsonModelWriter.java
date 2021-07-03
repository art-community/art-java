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

package io.art.json.descriptor;

import com.fasterxml.jackson.core.*;
import io.art.core.exception.*;
import io.art.json.exception.*;
import io.art.meta.descriptor.Writer;
import io.art.meta.model.*;
import io.art.meta.schema.MetaProviderTemplate.*;
import io.art.meta.transformer.*;
import io.netty.buffer.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.Objects.*;
import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;

@AllArgsConstructor
public class JsonModelWriter implements Writer {
    private final JsonFactory jsonFactory;

    @Override
    public void write(TypedObject model, ByteBuffer buffer) {
        write(model, buffer, JsonException::new);
    }

    @Override
    public void write(TypedObject model, ByteBuf buffer) {
        write(model, buffer, JsonException::new);
    }

    @Override
    public void write(TypedObject object, OutputStream outputStream, Charset charset) {
        if (isNull(object)) return;
        MetaType<?> type = object.getType();
        try (JsonGenerator generator = jsonFactory.createGenerator(new OutputStreamWriter(outputStream, charset))) {
            writeValue(generator, type, object.getObject());
        } catch (Throwable throwable) {
            throw new JsonException(throwable);
        }
    }


    private static void writeEntity(JsonGenerator generator, MetaType<?> type, Object value) throws Throwable {
        if (isNull(value)) return;
        generator.writeStartObject();
        writeFields(generator, type, value);
        generator.writeEndObject();
    }

    private static void writeEntity(JsonGenerator generator, String name, MetaType<?> type, Object value) throws Throwable {
        if (isNull(value)) return;
        generator.writeObjectFieldStart(name);
        writeFields(generator, type, value);
        generator.writeEndObject();
    }


    private static void writeArray(JsonGenerator generator, String name, MetaType<?> type, List<?> value) throws Throwable {
        generator.writeArrayFieldStart(name);
        MetaType<?> elementType = orElse(type.arrayComponentType(), () -> type.parameters().get(0));
        for (Object element : value) {
            if (isNull(element)) continue;
            writeValue(generator, elementType, element);
        }
        generator.writeEndArray();
    }


    private static void writeArray(JsonGenerator generator, MetaType<?> type, Object value) throws Throwable {
        generator.writeStartArray();
        MetaType<?> elementType = orElse(type.arrayComponentType(), () -> type.parameters().get(0));
        List<?> array = type.outputTransformer().toArray(cast(value));
        for (Object element : array) {
            if (isNull(element)) continue;
            writeValue(generator, elementType, element);
        }
        generator.writeEndArray();
    }

    private static void writeFields(JsonGenerator generator, MetaType<?> type, Object value) throws Throwable {
        MetaProviderInstance provider = type.declaration().provider().instantiate(value);
        for (MetaProperty<?> property : provider.properties().values()) {
            Object field = provider.getValue(property);
            writeField(generator, property.name(), property.type(), field);
        }
    }

    private static void writeMap(JsonGenerator generator, MetaType<?> keyType, MetaType<?> valueType, Map<String, ?> map) throws Throwable {
        generator.writeStartObject();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (isNull(value) || isNull(key)) continue;
            writeField(generator, keyType.outputTransformer().toString(cast(key)), valueType, value);
        }
        generator.writeEndObject();
    }

    private static void writeMap(JsonGenerator generator, String name, MetaType<?> keyType, MetaType<?> valueType, Map<String, ?> map) throws Throwable {
        generator.writeObjectFieldStart(name);
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (isNull(value) || isNull(key)) continue;
            writeField(generator, keyType.outputTransformer().toString(cast(key)), valueType, value);
        }
        generator.writeEndObject();
    }

    private static void writeValue(JsonGenerator generator, MetaType<?> type, Object value) throws Throwable {
        MetaTransformer<?> transformer = type.outputTransformer();
        switch (type.externalKind()) {
            case LAZY:
                writeValue(generator, type.parameters().get(0), transformer.toLazy(cast(value)));
                return;
            case ENTITY:
                writeEntity(generator, type, value);
                return;
            case MAP:
            case LAZY_MAP:
                writeMap(generator, type.parameters().get(0), type.parameters().get(1), cast(value));
                return;
            case ARRAY:
            case LAZY_ARRAY:
                writeArray(generator, type, value);
                return;
            case BINARY:
                generator.writeBinary(transformer.toByteArray(cast(value)));
                return;
            case STRING:
                generator.writeString(transformer.toString(cast(value)));
                return;
            case LONG:
                generator.writeNumber(transformer.toLong(cast(value)));
                return;
            case DOUBLE:
                generator.writeNumber(transformer.toDouble(cast(value)));
                return;
            case FLOAT:
                generator.writeNumber(transformer.toFloat(cast(value)));
                return;
            case INTEGER:
                generator.writeNumber(transformer.toInteger(cast(value)));
                return;
            case BOOLEAN:
                generator.writeBoolean(transformer.toBoolean(cast(value)));
                return;
            case CHARACTER:
                generator.writeString(EMPTY_STRING + transformer.toCharacter(cast(value)));
                return;
            case SHORT:
                generator.writeNumber(transformer.toShort(cast(value)));
                return;
            case BYTE:
                generator.writeNumber(transformer.toByte(cast(value)));
        }
        throw new ImpossibleSituationException();
    }

    private static void writeField(JsonGenerator generator, String name, MetaType<?> type, Object value) throws Throwable {
        MetaTransformer<?> transformer = type.outputTransformer();
        if (isNull(value)) return;
        switch (type.externalKind()) {
            case ARRAY:
            case LAZY_ARRAY:
                writeArray(generator, name, type, transformer.toArray(cast(value)));
                return;
            case LAZY:
                writeField(generator, name, type.parameters().get(0), transformer.toLazy(cast(value)));
                return;
            case STRING:
                generator.writeStringField(name, transformer.toString(cast(value)));
                return;
            case INTEGER:
                generator.writeNumberField(name, transformer.toInteger(cast(value)));
                return;
            case DOUBLE:
                generator.writeNumberField(name, transformer.toDouble(cast(value)));
                return;
            case LONG:
                generator.writeNumberField(name, transformer.toLong(cast(value)));
                return;
            case BOOLEAN:
                generator.writeBooleanField(name, transformer.toBoolean(cast(value)));
                return;
            case CHARACTER:
                generator.writeStringField(name, EMPTY_STRING + transformer.toCharacter(cast(value)));
                return;
            case SHORT:
                generator.writeNumberField(name, transformer.toShort(cast(value)));
                return;
            case BYTE:
                generator.writeNumberField(name, transformer.toByte(cast(value)));
                return;
            case FLOAT:
                generator.writeNumberField(name, transformer.toFloat(cast(value)));
                return;
            case BINARY:
                generator.writeBinaryField(name, transformer.toByteArray(cast(value)));
                return;
            case ENTITY:
                writeEntity(generator, name, type, value);
                return;
            case MAP:
            case LAZY_MAP:
                writeMap(generator, name, type.parameters().get(0), type.parameters().get(1), cast(transformer.toMap(cast(value))));
        }
    }
}
