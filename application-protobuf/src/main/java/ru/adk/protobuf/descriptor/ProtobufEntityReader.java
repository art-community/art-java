package ru.adk.protobuf.descriptor;

import com.google.protobuf.*;
import lombok.NoArgsConstructor;
import ru.adk.entity.Value;
import ru.adk.entity.*;
import ru.adk.protobuf.exception.ProtobufException;
import static java.lang.Integer.valueOf;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.extension.FileExtensions.readFileBytes;
import static ru.adk.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.adk.entity.CollectionValuesFactory.*;
import static ru.adk.entity.Entity.EntityBuilder;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.entity.MapValue.MapValueBuilder;
import static ru.adk.entity.MapValue.builder;
import static ru.adk.entity.PrimitivesFactory.*;
import static ru.adk.protobuf.constants.ProtobufExceptionMessages.VALUE_TYPE_NOT_SUPPORTED;
import static ru.adk.protobuf.entity.ProtobufValueMessage.*;
import static ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.parseFrom;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
