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
import io.art.json.exception.*;
import io.art.meta.model.*;
import io.art.meta.schema.MetaProviderTemplate.*;
import io.art.meta.transformer.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.meta.constants.MetaConstants.MetaTypeExternalKind.*;
import static io.art.meta.model.TypedObject.*;
import static java.util.Objects.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

@AllArgsConstructor
public class JsonModelWriter {
    private final JsonFactory jsonFactory;

    public void write(TypedObject object, OutputStream outputStream, Charset charset) {
        if (isNull(object)) return;
        MetaType<?> type = object.getType();
        MetaTransformer<?> transformer = type.outputTransformer();
        try (JsonGenerator generator = jsonFactory.createGenerator(new OutputStreamWriter(outputStream, charset))) {
            switch (type.externalKind()) {
                case ENTITY:
                    writeEntity(generator, type, object);
                    return;
                case MAP:
                    if (type.parameters().get(0).externalKind() != STRING) {
                        writeMap(generator, type.parameters().get(1), cast(object));
                        break;
                    }
                    break;
                case ARRAY:
                    writeArray(generator, type, object);
                    return;
                case BINARY:
                    generator.writeBinary(transformer.toByteArray(cast(object)));
                    return;
                case STRING:
                    generator.writeString(transformer.toString(cast(object)));
                    return;
                case LONG:
                    generator.writeNumber(transformer.toLong(cast(object)));
                    return;
                case DOUBLE:
                    generator.writeNumber(transformer.toDouble(cast(object)));
                    return;
                case FLOAT:
                    generator.writeNumber(transformer.toFloat(cast(object)));
                    return;
                case INTEGER:
                    generator.writeNumber(transformer.toInteger(cast(object)));
                    return;
                case BOOLEAN:
                    generator.writeBoolean(transformer.toBoolean(cast(object)));
                    return;
                case CHARACTER:
                    generator.writeString(EMPTY_STRING + transformer.toCharacter(cast(object)));
                    return;
                case SHORT:
                    generator.writeNumber(transformer.toShort(cast(object)));
                    return;
                case BYTE:
                    generator.writeNumber(transformer.toByte(cast(object)));
            }
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

    private static void writeArray(JsonGenerator generator, MetaType<?> type, Object value) throws Throwable {
        if (isNull(value)) return;
        generator.writeStartArray();
        writeFields(generator, type, value);
        generator.writeEndObject();
    }

    private static void writeEntity(JsonGenerator generator, String name, MetaType<?> type, Object value) throws Throwable {
        if (isNull(value)) return;
        generator.writeObjectFieldStart(name);
        writeFields(generator, type, value);
        generator.writeEndObject();
    }

    private static void writeFields(JsonGenerator generator, MetaType<?> type, Object value) throws Throwable {
        MetaProviderInstance provider = type.definition().provider().instantiate(value);
        for (MetaProperty<?> property : provider.properties().values()) {
            Object field = provider.getValue(property);
            writeField(generator, property.name(), property.type(), field);
        }
    }

    private static void writeMap(JsonGenerator generator, MetaType<?> valueType, Map<String, ?> map) throws Throwable {
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (isNull(value) || isNull(entry.getKey())) continue;
            writeField(generator, key, valueType, value);
        }
    }

    private static void writeField(JsonGenerator generator, String name, MetaType<?> type, Object value) throws Throwable {
        MetaTransformer<?> transformer = type.outputTransformer();
        if (isNull(value)) return;
        switch (type.externalKind()) {
            case ARRAY:
                writeArray(generator, name, typed(type, value));
                break;
            case STRING:
                generator.writeStringField(name, transformer.toString(cast(value)));
                break;
            case INTEGER:
                generator.writeNumberField(name, transformer.toInteger(cast(value)));
                break;
            case DOUBLE:
                generator.writeNumberField(name, transformer.toDouble(cast(value)));
                break;
            case LONG:
                generator.writeNumberField(name, transformer.toLong(cast(value)));
                break;
            case BOOLEAN:
                generator.writeBooleanField(name, transformer.toBoolean(cast(value)));
                break;
            case CHARACTER:
                generator.writeStringField(name, EMPTY_STRING + transformer.toCharacter(cast(value)));
                break;
            case SHORT:
                generator.writeNumberField(name, transformer.toShort(cast(value)));
                break;
            case BYTE:
                generator.writeNumberField(name, transformer.toByte(cast(value)));
                break;
            case FLOAT:
                generator.writeNumberField(name, transformer.toFloat(cast(value)));
                break;
            case BINARY:
                generator.writeBinaryField(name, transformer.toByteArray(cast(value)));
                break;
            case ENTITY:
                writeEntity(generator, name, type, value);
                break;
            case MAP:
                if (type.parameters().get(0).externalKind() != STRING) {
                    break;
                }
                writeMap(generator, type.parameters().get(1), cast(value));
                break;
        }
    }

    private static void writeArray(JsonGenerator generator, String name, TypedObject value) throws Throwable {
        generator.writeArrayFieldStart(name);
        MetaType<?> elementType = orElse(value.getType().arrayComponentType(), () -> value.getType().parameters().get(0));
        MetaTransformer<?> transformer = elementType.outputTransformer();
        List<?> array = value.getType().outputTransformer().toArray(cast(value.getObject()));
        for (Object element : array) {
            if (isNull(element)) continue;
            switch (elementType.externalKind()) {
                case ARRAY:
                    writeArray(generator, elementType, transformer.toArray(cast(element)));
                    continue;
                case MAP:
                    if (elementType.parameters().get(0).externalKind() != STRING) {
                        continue;
                    }
                    writeMap(generator, elementType.parameters().get(1), cast(element));
                    continue;
                case ENTITY:
                    writeFields(generator, elementType, element);
                    continue;
                case BINARY:
                    generator.writeBinary(transformer.toByteArray(cast(element)));
                    continue;
                case STRING:
                    generator.writeString(transformer.toString(cast(element)));
                    continue;
                case INTEGER:
                    generator.writeNumber(transformer.toInteger(cast(element)));
                    continue;
                case BOOLEAN:
                    generator.writeBoolean(transformer.toBoolean(cast(element)));
                    continue;
                case DOUBLE:
                    generator.writeNumber(transformer.toDouble(cast(element)));
                    continue;
                case LONG:
                    generator.writeNumber(transformer.toLong(cast(element)));
                    continue;
                case CHARACTER:
                    generator.writeString(EMPTY_STRING + transformer.toCharacter(cast(element)));
                    break;
                case SHORT:
                    generator.writeNumber(transformer.toShort(cast(element)));
                    continue;
                case BYTE:
                    generator.writeNumber(transformer.toByte(cast(element)));
                    continue;
                case FLOAT:
                    generator.writeNumber(transformer.toFloat(cast(element)));
            }
        }
        generator.writeEndArray();
    }
}
