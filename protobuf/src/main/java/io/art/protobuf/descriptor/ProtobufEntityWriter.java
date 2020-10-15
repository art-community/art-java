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

import com.google.protobuf.Value;
import com.google.protobuf.*;
import io.art.entity.immutable.*;
import io.art.protobuf.exception.*;
import lombok.experimental.*;
import static com.google.protobuf.UnknownFieldSet.*;
import static com.google.protobuf.UnsafeByteOperations.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.entity.immutable.Value.*;
import static io.art.protobuf.constants.ProtobufConstants.*;
import static io.art.protobuf.constants.ProtobufConstants.ExceptionMessages.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class ProtobufEntityWriter {
    public static void writeProtobuf(io.art.entity.immutable.Value value, OutputStream outputStream) {
        if (io.art.entity.immutable.Value.valueIsNull(value)) {
            return;
        }
        try {
            Value protobuf = writeProtobuf(value);
            if (nonNull(protobuf)) {
                protobuf.writeTo(outputStream);
            }
        } catch (IOException ioException) {
            throw new ProtobufException(ioException);
        }
    }

    public static byte[] writeProtobufToBytes(io.art.entity.immutable.Value value) {
        return let(writeProtobuf(value), MessageLite::toByteArray, EMPTY_BYTES);
    }

    public static void writeProtobuf(io.art.entity.immutable.Value value, Path path) {
        Value protobuf = writeProtobuf(value);
        apply(protobuf, result -> writeFileQuietly(path, result.toByteArray()));
    }

    public static com.google.protobuf.Value writeProtobuf(io.art.entity.immutable.Value value) {
        if (valueIsNull(value)) return null;
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
            case BINARY:
                return com.google.protobuf.Value.newBuilder()
                        .setUnknownFields(newBuilder()
                                .addField(BINARY_UNKNOWN_FIELD_NUMBER, UnknownFieldSet.Field
                                        .newBuilder()
                                        .addLengthDelimited(unsafeWrap(asBinary(value).getContent()))
                                        .build())
                                .build())
                        .build();
            case ENTITY:
                return writeEntity(asEntity(value));
            case ARRAY:
                return writeArray(asArray(value));
        }
        throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, value.getType()));
    }


    private static com.google.protobuf.Value writeArray(ArrayValue array) {
        ListValue.Builder listBuilder = ListValue.newBuilder();
        for (io.art.entity.immutable.Value element : array.asList()) {
            Value protobuf = writeProtobuf(element);
            if (isNull(protobuf)) continue;
            listBuilder.addValues(protobuf);
        }
        return com.google.protobuf.Value.newBuilder().setListValue(listBuilder.build()).build();
    }

    private static com.google.protobuf.Value writeEntity(Entity entity) {
        Struct.Builder builder = Struct.newBuilder();
        Set<Primitive> keys = entity.asMap().keySet();
        for (Primitive key : keys) {
            if (valueIsNull(key)) continue;
            io.art.entity.immutable.Value value = entity.get(key);
            Value protobuf = writeProtobuf(value);
            if (isNull(protobuf)) continue;
            builder.putFields(key.getString(), protobuf);
        }
        return com.google.protobuf.Value.newBuilder().setStructValue(builder.build()).build();
    }
}
