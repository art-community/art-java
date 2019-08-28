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

package ru.art.protobuf.descriptor;

import com.google.protobuf.*;
import lombok.*;
import ru.art.entity.Value;
import ru.art.entity.*;
import ru.art.protobuf.exception.*;
import static java.lang.Integer.valueOf;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.extension.FileExtensions.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.entity.CollectionValuesFactory.*;
import static ru.art.entity.Entity.*;
import static ru.art.entity.MapValue.*;
import static ru.art.entity.PrimitivesFactory.*;
import static ru.art.protobuf.constants.ProtobufExceptionMessages.*;
import static ru.art.protobuf.entity.ProtobufValueMessage.*;
import static ru.art.protobuf.entity.ProtobufValueMessage.ProtobufValue.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

@NoArgsConstructor(access = PRIVATE)
public class ProtobufEntityReader {
    public static Value readProtobuf(ProtobufValue protobufValue) {
        if (isNull(protobufValue) || isNull(protobufValue.getValue())) return null;
        try {
            switch (protobufValue.getValueType()) {
                case NULL:
                    return null;
                case STRING:
                    return stringPrimitive(StringValue.parseFrom(protobufValue.getValue().getValue()).getValue());
                case LONG:
                    return longPrimitive(Int64Value.parseFrom(protobufValue.getValue().getValue()).getValue());
                case DOUBLE:
                    return doublePrimitive(DoubleValue.parseFrom(protobufValue.getValue().getValue()).getValue());
                case INT:
                    return intPrimitive(Int32Value.parseFrom(protobufValue.getValue().getValue()).getValue());
                case BOOL:
                    return boolPrimitive(BoolValue.parseFrom(protobufValue.getValue().getValue()).getValue());
                case BYTE:
                    return bytePrimitive(valueOf(Int32Value.parseFrom(protobufValue.getValue().getValue()).getValue()).byteValue());
                case FLOAT:
                    return floatPrimitive(FloatValue.parseFrom(protobufValue.getValue().getValue()).getValue());
                case ENTITY:
                    return readEntityFromProtobuf(ProtobufEntity.parseFrom(protobufValue.getValue().getValue()));
                case BYTE_STRING:
                    return byteCollection(protobufValue.getValue().getValue().toByteArray());
                case COLLECTION:
                    return readCollectionFromProtobuf(ProtobufCollection.parseFrom(protobufValue.getValue().getValue()));
                case STRING_PARAMETERS_MAP:
                    return StringParametersMap.builder().parameters(ProtobufStringParametersMap.parseFrom(protobufValue.getValue().getValue()).getParametersMap()).build();
                case MAP:
                    return readMapFromProtobuf(ProtobufEntity.parseFrom(protobufValue.getValue().getValue()));
            }
        } catch (InvalidProtocolBufferException e) {
            throw new ProtobufException(e);
        }
        throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, protobufValue.getValueType()));
    }

    public static Value readProtobuf(Path path) {
        try {
            return readProtobuf(parseFrom(readFileBytes(path)));
        } catch (InvalidProtocolBufferException e) {
            throw new ProtobufException(e);
        }
    }

    private static Value readCollectionFromProtobuf(ProtobufCollection protobufCollection) throws InvalidProtocolBufferException {
        switch (protobufCollection.getElementsType()) {
            case NULL:
                return null;
            case VALUE:
                return valueCollection(readValueCollectionFromProtobuf(protobufCollection));
            case ENTITY:
                return entityCollection(readEntityCollectionFromProtobuf(protobufCollection));
            case COLLECTION:
                return collectionOfCollections(readCollectionOfCollectionsFromProtobuf(protobufCollection));
            case STRING_PARAMETERS_MAP:
                return stringParametersCollection(readStringParametersCollectionFromProtobuf(protobufCollection));
            case STRING:
                return stringCollection(readStringCollectionFromProtobuf(protobufCollection));
            case LONG:
                return longCollection(readLongCollectionValueFromProtobuf(protobufCollection));
            case DOUBLE:
                return doubleCollection(readDoubleCollectionValueFromProtobuf(protobufCollection));
            case INT:
                return intCollection(readIntCollectionValueFromProtobuf(protobufCollection));
            case BOOL:
                return boolCollection(readBoolCollectionValueFromProtobuf(protobufCollection));
            case FLOAT:
                return floatCollection(readFloatCollectionValueFromProtobuf(protobufCollection));
            case UNRECOGNIZED:
                break;
        }
        throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, protobufCollection.getElementsType()));
    }

    private static Collection<Value> readValueCollectionFromProtobuf(ProtobufCollection protobufCollection) {
        return cast(protobufCollection.getValuesList().stream().map(ProtobufEntityReader::readProtobuf).collect(Collectors.toList()));
    }

    private static Collection<Entity> readEntityCollectionFromProtobuf(ProtobufCollection protobufCollection) {
        return cast(protobufCollection.getValuesList().stream().map(ProtobufEntityReader::readProtobuf).collect(Collectors.toList()));
    }

    private static Collection<StringParametersMap> readStringParametersCollectionFromProtobuf(ProtobufCollection protobufCollection) {
        return cast(protobufCollection.getValuesList().stream().map(ProtobufEntityReader::readProtobuf).collect(Collectors.toList()));
    }

    private static Collection<CollectionValue<?>> readCollectionOfCollectionsFromProtobuf(ProtobufCollection protobufCollection) {
        return cast(protobufCollection.getValuesList().stream().map(ProtobufEntityReader::readProtobuf).collect(Collectors.toList()));
    }

    private static Collection<String> readStringCollectionFromProtobuf(ProtobufCollection protobufCollection) throws InvalidProtocolBufferException {
        List<String> list = dynamicArrayOf();
        for (ProtobufValue protobufValue : protobufCollection.getValuesList()) {
            list.add(StringValue.parseFrom(protobufValue.getValue().getValue()).getValue());
        }
        return list;
    }

    private static Collection<Long> readLongCollectionValueFromProtobuf(ProtobufCollection protobufCollection) throws InvalidProtocolBufferException {
        List<Long> list = dynamicArrayOf();
        for (ProtobufValue protobufValue : protobufCollection.getValuesList()) {
            list.add(Int64Value.parseFrom(protobufValue.getValue().getValue()).getValue());
        }
        return list;
    }

    private static Collection<Double> readDoubleCollectionValueFromProtobuf(ProtobufCollection protobufCollection) throws InvalidProtocolBufferException {
        List<Double> list = dynamicArrayOf();
        for (ProtobufValue protobufValue : protobufCollection.getValuesList()) {
            list.add(DoubleValue.parseFrom(protobufValue.getValue().getValue()).getValue());
        }
        return list;
    }

    private static Collection<Float> readFloatCollectionValueFromProtobuf(ProtobufCollection protobufCollection) throws InvalidProtocolBufferException {
        List<Float> list = dynamicArrayOf();
        for (ProtobufValue protobufValue : protobufCollection.getValuesList()) {
            list.add(FloatValue.parseFrom(protobufValue.getValue().getValue()).getValue());
        }
        return list;
    }

    private static Collection<Integer> readIntCollectionValueFromProtobuf(ProtobufCollection protobufCollection) throws InvalidProtocolBufferException {
        List<Integer> list = dynamicArrayOf();
        for (ProtobufValue protobufValue : protobufCollection.getValuesList()) {
            list.add(Int32Value.parseFrom(protobufValue.getValue().getValue()).getValue());
        }
        return list;
    }

    private static Collection<Boolean> readBoolCollectionValueFromProtobuf(ProtobufCollection protobufCollection) throws InvalidProtocolBufferException {
        List<Boolean> list = dynamicArrayOf();
        for (ProtobufValue protobufValue : protobufCollection.getValuesList()) {
            list.add(BoolValue.parseFrom(protobufValue.getValue().getValue()).getValue());
        }
        return list;
    }

    private static Value readEntityFromProtobuf(ProtobufEntity protobufEntity) {
        EntityBuilder entityBuilder = entityBuilder();
        protobufEntity.getValuesMap().forEach((key, value) -> entityBuilder.valueField(key, readProtobuf(value)));
        return entityBuilder.build();
    }

    private static MapValue readMapFromProtobuf(ProtobufEntity protobufEntity) {
        MapValueBuilder builder = builder();
        protobufEntity.getValuesMap().forEach((key, value) -> builder.element(stringPrimitive(key), readProtobuf(value)));
        return builder.build();
    }
}
