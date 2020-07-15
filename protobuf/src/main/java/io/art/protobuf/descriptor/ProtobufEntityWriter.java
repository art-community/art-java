/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.protobuf.descriptor;

import com.google.protobuf.*;
import lombok.experimental.*;
import io.art.entity.Value;
import io.art.entity.*;
import io.art.entity.constants.ValueType.*;
import io.art.protobuf.exception.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.entity.Value.*;
import static io.art.protobuf.constants.ProtobufExceptionMessages.*;
import java.io.*;
import java.nio.file.*;

@UtilityClass
public class ProtobufEntityWriter {
    public static byte[] writeProtobufToBytes(Value value) {
        return writeProtobuf(value).toByteArray();
    }

    public static void writeProtobuf(Value value, OutputStream outputStream) {
        try {
            writeProtobuf(value).writeTo(outputStream);
        } catch (IOException ioException) {
            throw new ProtobufException(ioException);
        }
    }

    public static void writeProtobuf(Value value, Path path) {
        writeFileQuietly(path, writeProtobuf(value).toByteArray());
    }

    public static com.google.protobuf.Value writeProtobuf(Value value) {
        if (isEmpty(value)) return com.google.protobuf.Value.getDefaultInstance();
        switch (value.getType()) {
            case STRING:
                return com.google.protobuf.Value.newBuilder().setStringValue(asPrimitive(value).getString()).build();
            case LONG:
                return com.google.protobuf.Value.newBuilder().setNumberValue(asPrimitive(value).getLong()).build();
            case DOUBLE:
                return com.google.protobuf.Value.newBuilder().setNumberValue(asPrimitive(value).getDouble()).build();
            case FLOAT:
                return com.google.protobuf.Value.newBuilder().setNumberValue(asPrimitive(value).getFloat()).build();
            case INT:
                return com.google.protobuf.Value.newBuilder().setNumberValue(asPrimitive(value).getInt()).build();
            case BYTE:
                return com.google.protobuf.Value.newBuilder().setNumberValue(asPrimitive(value).getByte()).build();
            case BOOL:
                return com.google.protobuf.Value.newBuilder().setBoolValue(asPrimitive(value).getBool()).build();
            case ENTITY:
                return writeEntityToProtobuf(asEntity(value));
            case COLLECTION:
                return writeCollectionToProtobuf(asCollection(value));
        }
        throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, value.getType()));
    }

    private static com.google.protobuf.Value writeCollectionToProtobuf(CollectionValue<?> collectionValue) {
        ListValue protobufValues = ListValue.newBuilder().addAllValues(collectionValue.getElements()
                .stream()
                .map(element -> writeCollectionValueToProtobuf(collectionValue.getElementsType(), element))
                .collect(toList())).build();
        return com.google.protobuf.Value.newBuilder().setListValue(protobufValues).build();
    }

    private static <T> com.google.protobuf.Value writeCollectionValueToProtobuf(CollectionElementsType type, T value) {
        switch (type) {
            case STRING:
                return com.google.protobuf.Value.newBuilder().setStringValue(cast(value)).build();
            case LONG:
            case DOUBLE:
            case FLOAT:
            case INT:
            case BYTE:
                return com.google.protobuf.Value.newBuilder().setNumberValue(cast(value)).build();
            case BOOL:
                return com.google.protobuf.Value.newBuilder().setBoolValue(cast(value)).build();
            case ENTITY:
                return writeEntityToProtobuf((Entity) value);
            case COLLECTION:
                return writeCollectionToProtobuf((CollectionValue<?>) value);
            case VALUE:
                return writeProtobuf((Value) value);
        }
        throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, type));
    }

    private static com.google.protobuf.Value writeEntityToProtobuf(Entity entity) {
        Struct protobufEntity = Struct.newBuilder()
                .putAllFields(entity.getFields().entrySet()
                        .stream()
                        .filter(entry -> isPrimitive(entry.getKey()))
                        .filter(entry -> nonNull(entry.getValue()))
                        .filter(entry -> !entry.getValue().isEmpty())
                        .collect(toMap(entry -> entry.getKey().toString(), entry -> writeProtobuf(entry.getValue()))))
                .build();
        return com.google.protobuf.Value.newBuilder()
                .setStructValue(protobufEntity)
                .build();
    }
}
