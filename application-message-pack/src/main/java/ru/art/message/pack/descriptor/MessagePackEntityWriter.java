/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.message.pack.descriptor;

import lombok.experimental.*;
import org.msgpack.core.*;
import org.msgpack.core.buffer.*;
import org.msgpack.value.*;
import ru.art.entity.Value;
import ru.art.entity.*;
import static java.util.stream.Collectors.*;
import static org.msgpack.core.MessagePack.*;
import static org.msgpack.value.ValueFactory.*;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ArrayConstants.*;
import static ru.art.entity.Value.*;
import java.io.*;
import java.nio.file.*;

@UtilityClass
public class MessagePackEntityWriter {
    public static void writeMessagePack(Value value, OutputStream outputStream) {
    }

    public static void writeMessagePack(Value value, Path path) {
    }

    public static byte[] writeMessagePack(Value value) {
        org.msgpack.value.Value messagePackValue = newNil();
        if (isEmpty(value)) {
            return EMPTY_BYTES;
        }
        if (isPrimitive(value)) {
            ArrayBufferOutput output = new ArrayBufferOutput();
            try {
                newDefaultPacker(output).packValue(writePrimitive(asPrimitive(value))).close();
                return output.toByteArray();
            } catch (Throwable e) {
                throw new MessagePackException(e);
            }
        }
        switch (value.getType()) {
            case ENTITY:
                break;
            case COLLECTION:
                break;
            case MAP:
                break;
            case STRING_PARAMETERS_MAP:
                break;
        }
        ArrayBufferOutput output = new ArrayBufferOutput();
        try {
            newDefaultPacker(output).packValue(messagePackValue).close();
            return output.toByteArray();
        } catch (Throwable e) {
            throw new MessagePackException(e);
        }
    }

    private static org.msgpack.value.Value writePrimitive(Primitive primitive) {
        if (isEmpty(primitive)) {
            return newNil();
        }
        switch (primitive.getPrimitiveType()) {
            case STRING:
                return newString(primitive.getString());
            case LONG:
                return newInteger(primitive.getLong());
            case INT:
                return newInteger(primitive.getInt());
            case DOUBLE:
                return newFloat(primitive.getDouble());
            case FLOAT:
                return newFloat(primitive.getFloat());
            case BOOL:
                return newBoolean(primitive.getBool());
            case BYTE:
                return newBinary(new byte[]{primitive.getByte()});
        }
        return newNil();
    }

    private static org.msgpack.value.Value writeCollectionValue(CollectionValue<?> collectionValue) {
        if (isEmpty(collectionValue)) {
            return newArray();
        }
        switch (collectionValue.getElementsType()) {
            case STRING:
                return newArray(collectionValue.getStringList()
                        .stream()
                        .map(ValueFactory::newString)
                        .collect(toList()));
            case LONG:
                switch (collectionValue.getCollectionMode()) {
                    case PRIMITIVE_ARRAY:
                        break;
                    case COLLECTION:
                        return newArray(collectionValue.getLongList()
                                .stream()
                                .map(ValueFactory::newInteger)
                                .collect(toList()));
                }
            case DOUBLE:
                break;
            case FLOAT:
                break;
            case INT:
                break;
            case BOOL:
                break;
            case BYTE:
                break;
            case ENTITY:
                break;
            case COLLECTION:
                break;
            case MAP:
                break;
            case STRING_PARAMETERS_MAP:
                break;
            case VALUE:
                break;
        }
        return newArray();
    }
}