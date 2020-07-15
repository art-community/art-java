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
import io.art.protobuf.exception.*;
import static com.google.protobuf.Value.KindCase.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.entity.CollectionValuesFactory.*;
import static io.art.entity.Entity.*;
import static io.art.entity.PrimitivesFactory.*;
import static io.art.protobuf.constants.ProtobufExceptionMessages.*;
import java.io.*;
import java.nio.file.*;

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
                return readStructFromProtobuf(protobufValue.getStructValue());
            case LIST_VALUE:
                return readCollectionFromProtobuf(protobufValue.getListValue());
            case KIND_NOT_SET:
                return null;
        }
        throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, protobufValue.getKindCase()));
    }

    private static Value readCollectionFromProtobuf(ListValue protobufCollection) {
        return valueCollection(protobufCollection.getValuesList().stream().map(ProtobufEntityReader::readProtobuf).collect(toList()));
    }

    private static Value readStructFromProtobuf(Struct protobufEntity) {
        EntityBuilder entityBuilder = entityBuilder();
        protobufEntity.getFieldsMap().forEach((key, value) -> entityBuilder.valueField(key, readProtobuf(value)));
        return entityBuilder.build();
    }
}
