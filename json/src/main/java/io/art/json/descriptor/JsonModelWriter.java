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
        MetaTransformer<?> transformer = object.getType().outputTransformer();
        try (JsonGenerator generator = jsonFactory.createGenerator(new OutputStreamWriter(outputStream, charset))) {
            switch (object.getType().externalKind()) {
                case ENTITY:
                    writeJsonEntity(generator, object);
                    return;
                case ARRAY:
                    writeArray(generator, object);
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
                case BYTE:
                    generator.writeNumber(transformer.toByte(cast(object)));
            }
        } catch (Throwable throwable) {
            throw new JsonException(throwable);
        }
    }

    private static void writeJsonEntity(JsonGenerator generator, TypedObject object) throws Throwable {
        if (isNull(object)) return;
        generator.writeStartObject();
        writeJsonFields(generator, object);
        generator.writeEndObject();
    }

    private static void writeArray(JsonGenerator generator, TypedObject object) throws Throwable {
        if (isNull(object)) return;
        generator.writeStartArray();
        writeJsonFields(generator, object);
        generator.writeEndObject();
    }

    private static void writeJsonEntity(JsonGenerator generator, String name, TypedObject object) throws Throwable {
        if (isNull(object)) return;
        generator.writeObjectFieldStart(name);
        writeJsonFields(generator, object);
        generator.writeEndObject();
    }

    private static void writeJsonFields(JsonGenerator generator, TypedObject object) throws Throwable {
        MetaType<?> type = object.getType();
        MetaProviderInstance provider = type.definition().provider().instantiate(object.getObject());
        for (MetaProperty<?> property : provider.properties().values()) {
            String name = property.name();
            Object value = provider.getValue(property);
            MetaTransformer<?> transformer = property.type().outputTransformer();
            if (isNull(value)) continue;
            switch (property.type().externalKind()) {
                case MAP:
                    writeJsonEntity(generator, name, typed(property.type(), value));
                    break;
                case ARRAY:
                    writeArray(generator, name, typed(property.type(), value));
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
                    break;
            }
        }
    }

    private static void writeArray(JsonGenerator generator, String name, TypedObject value) throws Throwable {
        generator.writeArrayFieldStart(name);
        MetaType<?> elementType = orElse(value.getType().arrayComponentType(), value.getType().parameters().get(0));
        MetaTransformer<?> transformer = elementType.outputTransformer();
        List<?> array = value.getType().outputTransformer().toArray(cast(value.getObject()));
        for (Object element : array) {
            if (isNull(element)) continue;
            switch (value.getType().externalKind()) {
                case ARRAY:
                    writeArray(generator, typed(value.getType(), transformer.toArray(cast(value))));
                    continue;
                case ENTITY:
                    writeJsonEntity(generator, value);
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
