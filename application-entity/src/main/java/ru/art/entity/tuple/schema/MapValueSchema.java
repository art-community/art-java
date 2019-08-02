/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.entity.tuple.schema;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.art.entity.MapValue;
import ru.art.entity.Primitive;
import ru.art.entity.Value;
import ru.art.entity.constants.ValueType;
import ru.art.entity.exception.ValueMappingException;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.entity.Value.*;
import static ru.art.entity.constants.ValueMappingExceptionMessages.VALUE_TYPE_IS_NULL;
import static ru.art.entity.constants.ValueType.MAP;
import static ru.art.entity.constants.ValueType.asPrimitiveType;
import java.util.List;
import java.util.Objects;

@Getter
@EqualsAndHashCode(callSuper = true)
public class MapValueSchema extends ValueSchema {
    private final List<MapEntrySchema> entriesSchema = dynamicArrayOf();

    MapValueSchema(MapValue value) {
        super(MAP);
        value.getElements()
                .entrySet()
                .stream()
                .filter(entry -> isPrimitive(entry.getKey()))
                .forEach(entry -> entriesSchema.add(new MapEntrySchema(
                        entry.getKey().getType(),
                        asPrimitive(entry.getKey()),
                        entry.getValue().getType(),
                        fromValue(entry.getValue()))));
    }

    private MapValueSchema() {
        super(MAP);
    }

    @Override
    public List<?> toTuple() {
        List<?> tuple = dynamicArrayOf(getType().name());
        entriesSchema.stream().map(MapEntrySchema::toTuple).forEach(value -> tuple.add(cast(value)));
        return tuple;

    }

    public static MapValueSchema fromTuple(List<?> tuple) {
        MapValueSchema schema = new MapValueSchema();
        tuple.stream()
                .skip(1)
                .map(element -> (List<?>) element)
                .map(MapEntrySchema::fromTuple)
                .filter(Objects::nonNull)
                .forEach(schema.entriesSchema::add);
        return schema;
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class MapEntrySchema {
        private final ValueType keyType;
        private final Primitive key;
        private final ValueType valueType;
        private final ValueSchema valueSchema;

        List<?> toTuple() {
            return dynamicArrayOf(keyType.name(), key.getValue(), valueType.name(), valueSchema.toTuple());
        }

        static MapEntrySchema fromTuple(List<?> element) {
            ValueType keyType = ValueType.valueOf((String) element.get(0));
            if (!isPrimitiveType(keyType)) {
                return null;
            }
            Object key = element.get(1);
            ValueType valueType = ValueType.valueOf((String) element.get(2));
            if (isPrimitiveType(valueType)) {
                return new MapEntrySchema(keyType, new Primitive(key, asPrimitiveType(keyType)), valueType, new ValueSchema(valueType));
            }
            switch (valueType) {
                case ENTITY:
                    return new MapEntrySchema(keyType, new Primitive(key, asPrimitiveType(keyType)), valueType, EntitySchema.fromTuple((List<?>) element.get(3)));
                case COLLECTION:
                    return new MapEntrySchema(keyType, new Primitive(key, asPrimitiveType(keyType)), valueType, CollectionValueSchema.fromTuple((List<?>) element.get(3)));
                case MAP:
                    return new MapEntrySchema(keyType, new Primitive(key, asPrimitiveType(keyType)), valueType, MapValueSchema.fromTuple((List<?>) element.get(3)));
                case STRING_PARAMETERS_MAP:
                    return new MapEntrySchema(keyType, new Primitive(key, asPrimitiveType(keyType)), valueType, StringParametersSchema.fromTuple((List<?>) element.get(3)));
            }
            throw new ValueMappingException(VALUE_TYPE_IS_NULL);
        }
    }
}