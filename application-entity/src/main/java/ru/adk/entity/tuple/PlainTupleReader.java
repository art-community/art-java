package ru.adk.entity.tuple;

import lombok.NoArgsConstructor;
import ru.adk.entity.*;
import ru.adk.entity.constants.ValueType;
import ru.adk.entity.tuple.schema.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.adk.entity.CollectionValuesFactory.*;
import static ru.adk.entity.Entity.EntityBuilder;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.entity.PrimitivesFactory.*;
import static ru.adk.entity.Value.isPrimitiveType;
import java.util.List;

@NoArgsConstructor(access = PRIVATE)
public class PlainTupleReader {
    public static Value readTuple(List<?> tuple, ValueSchema schema) {
        if (isEmpty(tuple) || isNull(schema) || isEmpty(schema)) return null;
        if (isPrimitiveType(schema.getType())) {
            return readPrimitive(schema.getType(), tuple.get(0));
        }
        switch (schema.getType()) {
            case ENTITY:
                return readEntity(tuple, (EntitySchema) schema);
            case COLLECTION:
                return readCollectionValue(tuple, (CollectionValueSchema) schema);
            case MAP:
                return readMapValue(tuple, (MapValueSchema) schema);
            case STRING_PARAMETERS_MAP:
                return readStringParameters(tuple, (StringParametersSchema) schema);
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    private static Primitive readPrimitive(ValueType type, Object value) {
        switch (type) {
            case STRING:
                return stringPrimitive((String) value);
            case LONG:
                return longPrimitive(((Number) value).longValue());
            case DOUBLE:
                return doublePrimitive((Double) value);
            case FLOAT:
                return floatPrimitive((Float) value);
            case INT:
                return intPrimitive((Integer) value);
            case BOOL:
                return boolPrimitive((Boolean) value);
            case BYTE:
                return bytePrimitive((Byte) value);
        }
        return null;
    }

    private static Entity readEntity(List<?> entity, EntitySchema schema) {
        if (isNull(schema)) return null;
        EntityBuilder entityBuilder = entityBuilder();
        if (isEmpty(entity)) return entityBuilder.build();
        List<EntitySchema.EntityFieldSchema> fieldsSchema = schema.getFieldsSchema();
        for (int i = 0, fieldsSchemaSize = fieldsSchema.size(); i < fieldsSchemaSize; i++) {
            EntitySchema.EntityFieldSchema fieldSchema = fieldsSchema.get(i);
            switch (fieldSchema.getType()) {
                case STRING:
                    entityBuilder.stringField(fieldSchema.getName(), (String) entity.get(i));
                    break;
                case LONG:
                    Number number = (Number) entity.get(i);
                    if (nonNull(number)) {
                        entityBuilder.longField(fieldSchema.getName(), number.longValue());
                        break;
                    }
                    entityBuilder.longField(fieldSchema.getName(), null);
                    break;
                case DOUBLE:
                    entityBuilder.doubleField(fieldSchema.getName(), (Double) entity.get(i));
                    break;
                case FLOAT:
                    entityBuilder.floatField(fieldSchema.getName(), (Float) entity.get(i));
                    break;
                case INT:
                    entityBuilder.intField(fieldSchema.getName(), (Integer) entity.get(i));
                    break;
                case BOOL:
                    entityBuilder.boolField(fieldSchema.getName(), (Boolean) entity.get(i));
                    break;
                case BYTE:
                    entityBuilder.byteField(fieldSchema.getName(), (Byte) entity.get(i));
                    break;
                case ENTITY:
                    entityBuilder.entityField(fieldSchema.getName(), readEntity((List<?>) entity.get(i), (EntitySchema) fieldSchema.getSchema()));
                    break;
                case COLLECTION:
                    entityBuilder.valueField(fieldSchema.getName(), readCollectionValue((List<?>) entity.get(i), (CollectionValueSchema) fieldSchema.getSchema()));
                    break;
                case MAP:
                    entityBuilder.valueField(fieldSchema.getName(), readMapValue((List<?>) entity.get(i), (MapValueSchema) fieldSchema.getSchema()));
                    break;
                case STRING_PARAMETERS_MAP:
                    entityBuilder.valueField(fieldSchema.getName(), readStringParameters((List<?>) entity.get(i), (StringParametersSchema) fieldSchema.getSchema()));
                    break;
            }
        }
        return entityBuilder.build();
    }

    private static CollectionValue<?> readCollectionValue(List<?> collection, CollectionValueSchema schema) {
        if (isNull(schema)) return null;
        if (isEmpty(collection)) return emptyCollection();
        List<?> elements = dynamicArrayOf();
        List<ValueSchema> elementsSchema = schema.getElementsSchema();
        switch (schema.getElementsType()) {
            case STRING:
            case LONG:
            case DOUBLE:
            case FLOAT:
            case INT:
            case BOOL:
            case BYTE:
                return collectionValue(schema.getElementsType(), cast(collection));
            case ENTITY:
                for (int i = 0; i < elementsSchema.size(); i++) {
                    elements.add(cast(readEntity((List<?>) collection.get(i), (EntitySchema) elementsSchema.get(i))));
                }
                return entityCollection(cast(elements));
            case COLLECTION:
                for (int i = 0; i < elementsSchema.size(); i++) {
                    elements.add(cast(readCollectionValue((List<?>) collection.get(i), (CollectionValueSchema) elementsSchema.get(i))));
                }
                return collectionOfCollections(cast(elements));
            case MAP:
                for (int i = 0; i < elementsSchema.size(); i++) {
                    elements.add(cast(readMapValue((List<?>) collection.get(i), (MapValueSchema) elementsSchema.get(i))));
                }
                return mapCollection(cast(elements));
            case STRING_PARAMETERS_MAP:
                for (int i = 0; i < elementsSchema.size(); i++) {
                    elements.add(cast(readStringParameters((List<?>) collection.get(i), (StringParametersSchema) elementsSchema.get(i))));
                }
                return stringParametersCollection(cast(elements));
            case VALUE:
                for (int i = 0; i < elementsSchema.size(); i++) {
                    elements.add(cast(readTuple((List<?>) collection.get(i), elementsSchema.get(i))));
                }
                return valueCollection(cast(elements));

        }
        return emptyCollection();
    }

    private static StringParametersMap readStringParameters(List<?> stringParameters, StringParametersSchema schema) {
        if (isNull(schema)) return null;
        if (isEmpty(stringParameters)) return StringParametersMap.builder().build();
        StringParametersMap.StringParametersMapBuilder stringParametersMapBuilder = StringParametersMap.builder();
        List<String> stringParametersSchema = schema.getStringParametersSchema();
        for (int i = 0; i < stringParametersSchema.size(); i++) {
            stringParametersMapBuilder.parameter(stringParametersSchema.get(i), (String) stringParameters.get(i));
        }
        return stringParametersMapBuilder.build();
    }

    private static MapValue readMapValue(List<?> map, MapValueSchema schema) {
        if (isNull(schema)) return null;
        if (isEmpty(map)) return MapValue.builder().build();
        MapValue.MapValueBuilder mapValueBuilder = MapValue.builder();
        List<MapValueSchema.MapEntrySchema> entriesSchema = schema.getEntriesSchema();
        for (int i = 0; i < entriesSchema.size(); i++) {
            MapValueSchema.MapEntrySchema entrySchema = entriesSchema.get(i);
            ValueType keyType = entrySchema.getKeyType();
            if (!isPrimitiveType(keyType)) {
                continue;
            }
            if (isPrimitiveType(entrySchema.getValueType())) {
                mapValueBuilder.element(entrySchema.getKey(), readPrimitive(entrySchema.getValueType(), map.get(i)));
                continue;
            }

            mapValueBuilder.element(entrySchema.getKey(), readTuple((List<?>) map.get(i), entrySchema.getValueSchema()));
        }
        return mapValueBuilder.build();
    }
}
