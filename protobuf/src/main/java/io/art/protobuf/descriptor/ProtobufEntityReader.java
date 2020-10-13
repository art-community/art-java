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
import io.art.core.stream.*;
import io.art.entity.builder.*;
import io.art.entity.immutable.Value;
import io.art.protobuf.exception.*;
import io.netty.buffer.*;
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
import java.nio.*;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class ProtobufEntityReader {
    public static Value readProtobuf(byte[] bytes) {
        return readProtobuf(new ByteArrayInputStream(bytes));
    }

    public static Value readProtobuf(ByteBuffer nioBuffer) {
        return readProtobuf(new NioByteBufferInputStream(nioBuffer));
    }

    public static Value readProtobuf(ByteBuf nettyBuffer) {
        return readProtobuf(new ByteBufInputStream(nettyBuffer));
    }

    public static Value readProtobuf(Path path) {
        return readProtobuf(fileInputStream(path));
    }

    public static Value readProtobuf(InputStream inputStream) {
        try {
            return readProtobuf(com.google.protobuf.Value.parseFrom(inputStream));
        } catch (Throwable throwable) {
            throw new ProtobufException(throwable);
        }
    }

    public static Value readProtobuf(com.google.protobuf.Value value) {
        if (isNull(value) || value.getKindCase() == NULL_VALUE) return null;
        switch (value.getKindCase()) {
            case NUMBER_VALUE:
                return doublePrimitive(value.getNumberValue());
            case STRING_VALUE:
                return stringPrimitive(value.getStringValue());
            case BOOL_VALUE:
                return boolPrimitive(value.getBoolValue());
            case STRUCT_VALUE:
                return readEntity(value.getStructValue());
            case LIST_VALUE:
                return readArray(value.getListValue());
            case KIND_NOT_SET:
                UnknownFieldSet.Field bytesField = value.getUnknownFields().getField(BINARY_UNKNOWN_FIELD_NUMBER);
                if (isNull(bytesField)) {
                    return null;
                }
                ByteString bytes = bytesField.getLengthDelimitedList().get(0);
                if (isNull(bytes)) {
                    return null;
                }
                return binary(toByteArray(bytes.asReadOnlyByteBuffer()));
        }
        throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, value.getKindCase()));
    }

    private static Value readArray(ListValue protobufCollection) {
        List<com.google.protobuf.Value> values = protobufCollection.getValuesList();
        return array(index -> readProtobuf(values.get(index)), values::size);
    }

    private static Value readEntity(Struct protobufEntity) {
        EntityBuilder entityBuilder = entityBuilder();
        Map<String, com.google.protobuf.Value> fields = protobufEntity.getFieldsMap();
        for (Map.Entry<String, com.google.protobuf.Value> entry : fields.entrySet()) {
            String key = entry.getKey();
            if (isNull(key)) continue;
            com.google.protobuf.Value value = entry.getValue();
            if (isNull(value) || value.getKindCase() == NULL_VALUE) continue;
            entityBuilder.lazyPut(key, () -> readProtobuf(value));
        }
        return entityBuilder.build();
    }
}
