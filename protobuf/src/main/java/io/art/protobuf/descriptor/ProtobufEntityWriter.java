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
import io.art.entity.immutable.*;
import io.art.protobuf.exception.*;
import lombok.experimental.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.entity.immutable.Value.*;
import static io.art.protobuf.constants.ProtobufExceptionMessages.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import java.io.*;
import java.nio.file.*;

@UtilityClass
public class ProtobufEntityWriter {
    public static byte[] writeProtobufToBytes(io.art.entity.immutable.Value value) {
        return writeProtobuf(value).toByteArray();
    }

    public static void writeProtobuf(io.art.entity.immutable.Value value, OutputStream outputStream) {
        try {
            writeProtobuf(value).writeTo(outputStream);
        } catch (IOException ioException) {
            throw new ProtobufException(ioException);
        }
    }

    public static void writeProtobuf(io.art.entity.immutable.Value value, Path path) {
        writeFileQuietly(path, writeProtobuf(value).toByteArray());
    }

    public static com.google.protobuf.Value writeProtobuf(io.art.entity.immutable.Value value) {
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
                return writeEntity(asEntity(value));
            case ARRAY:
                return writeArray(asArray(value));
        }
        throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, value.getType()));
    }

    private static com.google.protobuf.Value writeArray(ArrayValue array) {
        ListValue protobufValues = ListValue.newBuilder()
                .addAllValues(array.asStream()
                        .map(ProtobufEntityWriter::writeProtobuf)
                        .collect(toList()))
                .build();
        return com.google.protobuf.Value.newBuilder().setListValue(protobufValues).build();
    }

    private static com.google.protobuf.Value writeEntity(Entity entity) {
        Struct protobufEntity = Struct.newBuilder()
                .putAllFields(entity.asMap().entrySet()
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
