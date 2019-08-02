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

package ru.art.entity.tuple;

import lombok.NoArgsConstructor;
import ru.art.entity.*;
import ru.art.entity.constants.ValueType;
import static java.util.Collections.EMPTY_LIST;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.entity.Value.*;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = PRIVATE)
public class TupleWriter {
    public static List<?> writeTuple(Value value) {
        if (isEmpty(value)) return EMPTY_LIST;
        if (isPrimitive(value)) {
            return fixedArrayOf(value.getType().ordinal(), asPrimitive(value).getValue());
        }
        List<?> tuple = dynamicArrayOf();
        switch (value.getType()) {
            case ENTITY:
                writeEntity(tuple, asEntity(value));
                break;
            case COLLECTION:
                writeCollectionValue(tuple, asCollection(value));
                break;
            case STRING_PARAMETERS_MAP:
                writeStringParameters(tuple, asStringParametersMap(value));
                break;
            case MAP:
                writeMap(tuple, asMap(value));
                break;
        }
        return tuple;
    }

    private static void writeEntity(List<?> tuple, Entity entity) {
        List<?> entityTuple = dynamicArrayOf();
        Map<String, ? extends Value> fields = entity.getFields();
        tuple.add(cast(entity.getType().ordinal()));
        for (Map.Entry<String, ?> entry : fields.entrySet()) {
            Value value = (Value) entry.getValue();
            entityTuple.add(cast(entry.getKey()));
            if (isPrimitive(value)) {
                entityTuple.add(cast(value.getType().ordinal()));
                entityTuple.add(cast(asPrimitive(value).getValue()));
                continue;
            }

            switch (value.getType()) {
                case ENTITY:
                    writeEntity(entityTuple, asEntity(value));
                    break;
                case COLLECTION:
                    writeCollectionValue(entityTuple, asCollection(value));
                    break;
                case MAP:
                    writeMap(entityTuple, asMap(value));
                    break;
                case STRING_PARAMETERS_MAP:
                    writeStringParameters(entityTuple, asStringParametersMap(value));
                    break;
            }
        }
        tuple.add(cast(entityTuple.size()));
        tuple.addAll(cast(entityTuple));
    }

    private static void writeCollectionValue(List<?> tuple, CollectionValue<?> collectionValue) {
        List<?> collectionValueTuple = dynamicArrayOf();
        List<?> valueList = collectionValue.getList();
        tuple.add(cast(collectionValue.getType().ordinal()));
        for (Object value : valueList) {
            switch (collectionValue.getElementsType()) {
                case STRING:
                    collectionValueTuple.add(cast(ValueType.STRING.ordinal()));
                    collectionValueTuple.add(cast(value));
                    break;
                case LONG:
                    collectionValueTuple.add(cast(ValueType.LONG.ordinal()));
                    collectionValueTuple.add(cast(value));
                    break;
                case DOUBLE:
                    collectionValueTuple.add(cast(ValueType.DOUBLE.ordinal()));
                    collectionValueTuple.add(cast(value));
                    break;
                case FLOAT:
                    collectionValueTuple.add(cast(ValueType.FLOAT.ordinal()));
                    collectionValueTuple.add(cast(value));
                    break;
                case INT:
                    collectionValueTuple.add(cast(ValueType.INT.ordinal()));
                    collectionValueTuple.add(cast(value));
                    break;
                case BOOL:
                    collectionValueTuple.add(cast(ValueType.BOOL.ordinal()));
                    collectionValueTuple.add(cast(value));
                    break;
                case BYTE:
                    collectionValueTuple.add(cast(ValueType.BYTE.ordinal()));
                    collectionValueTuple.add(cast(value));
                    break;
                case ENTITY:
                    writeEntity(collectionValueTuple, asEntity((Value) value));
                    break;
                case COLLECTION:
                    writeCollectionValue(collectionValueTuple, asCollection((Value) value));
                    break;
                case MAP:
                    writeMap(collectionValueTuple, asMap((Value) value));
                    break;
                case STRING_PARAMETERS_MAP:
                    writeStringParameters(collectionValueTuple, asStringParametersMap((Value) value));
                    break;
            }
        }
        tuple.add(cast(collectionValueTuple.size()));
        tuple.addAll(cast(collectionValueTuple));
    }

    private static void writeStringParameters(List<?> tuple, StringParametersMap stringParameters) {
        tuple.add(cast(stringParameters.getType().ordinal()));
        tuple.add(cast(stringParameters.getParameters().size() * 2));
        for (Map.Entry<?, ?> entry : stringParameters.getParameters().entrySet()) {
            tuple.add(cast(entry.getKey()));
            tuple.add(cast(entry.getValue()));
        }
    }

    private static void writeMap(List<?> tuple, MapValue mapValue) {
        List<?> mapTuple = dynamicArrayOf();
        for (Map.Entry<? extends Value, ? extends Value> entry : mapValue.getElements().entrySet()) {
            if (isPrimitive(entry.getKey())) {
                mapTuple.add(cast(entry.getKey().getType().ordinal()));
                mapTuple.add(cast(asPrimitive(entry.getKey()).getValue()));
                mapTuple.addAll(cast(writeTuple(entry.getValue())));
            }
        }
        tuple.add(cast(mapValue.getType().ordinal()));
        tuple.add(cast(mapTuple.size()));
        tuple.addAll(cast(mapTuple));
    }
}
