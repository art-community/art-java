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
import io.art.entity.builder.*;
import io.art.entity.immutable.Value;
import io.art.protobuf.exception.*;
import lombok.experimental.*;
import static com.google.protobuf.Value.KindCase.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.NioBufferExtensions.*;
import static io.art.entity.factory.ArrayFactory.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.BinaryValue.*;
import static io.art.entity.immutable.Entity.*;
import static io.art.protobuf.constants.ProtobufConstants.*;
import static io.art.protobuf.constants.ProtobufConstants.ExceptionMessages.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class ProtobufEntityReader {
    public static Value readProtobuf(byte[] bytes) {
        try {
            return readProtobuf(com.google.protobuf.Value.parseFrom(bytes));
        } catch (Throwable throwable) {
            throw new ProtobufException(throwable);
        }
    }

    public static Value readProtobuf(InputStream inputStream) {
        try {
            return readProtobuf(com.google.protobuf.Value.parseFrom(inputStream));
        } catch (Throwable throwable) {
            throw new ProtobufException(throwable);
        }
    }

    public static Value readProtobuf(Path path) {
        try {
            return readProtobuf(com.google.protobuf.Value.parseFrom(readFileBytes(path)));
        } catch (InvalidProtocolBufferException throwable) {
            throw new ProtobufException(throwable);
        }
    }

    public static Value readProtobuf(com.google.protobuf.Value protobufValue) {
        if (isNull(protobufValue) || protobufValue.getKindCase() == NULL_VALUE) return null;
        switch (protobufValue.getKindCase()) {
            case NUMBER_VALUE:
                return doublePrimitive(protobufValue.getNumberValue());
            case STRING_VALUE:
                return stringPrimitive(protobufValue.getStringValue());
            case BOOL_VALUE:
                return boolPrimitive(protobufValue.getBoolValue());
            case STRUCT_VALUE:
                return readEntity(protobufValue.getStructValue());
            case LIST_VALUE:
                return readArray(protobufValue.getListValue());
            case KIND_NOT_SET:
                List<ByteString> byteStrings = protobufValue.getUnknownFields().getField(BINARY_UNKNOWN_FIELD_NUMBER).getLengthDelimitedList();
                if (byteStrings.isEmpty()) {
                    return null;
                }
                return binary(toByteArray(byteStrings.get(0).asReadOnlyByteBuffer()));
        }
        throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, protobufValue.getKindCase()));
    }

    private static Value readArray(ListValue protobufCollection) {
        List<com.google.protobuf.Value> values = protobufCollection.getValuesList();
        return array(index -> readProtobuf(values.get(index)), values::size);
    }

    private static Value readEntity(Struct protobufEntity) {
        EntityBuilder entityBuilder = entityBuilder();
        protobufEntity.getFieldsMap().forEach((key, value) -> entityBuilder.lazyPut(key, () -> readProtobuf(value)));
        return entityBuilder.build();
    }
}
