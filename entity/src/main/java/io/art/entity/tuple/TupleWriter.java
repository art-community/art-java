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

package io.art.entity.tuple;

import lombok.experimental.*;
import io.art.entity.*;
import io.art.entity.constants.*;
import static java.util.Collections.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.Value.*;
import java.util.*;

@UtilityClass
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
            case ARRAY:
                writeCollectionValue(tuple, asCollection(value));
                break;
        }
        return tuple;
    }

    private static void writeEntity(List<?> tuple, Entity entity) {
        List<?> entityTuple = dynamicArrayOf();
        Map<? extends Value, ? extends Value> fields = entity.getFields();
        tuple.add(cast(entity.getType().ordinal()));
        for (Map.Entry<? extends Value, ? extends Value> entry : fields.entrySet()) {
            Value key = entry.getKey();
            if (!isPrimitive(key)) continue;
            Value value = entry.getValue();
            entityTuple.add(cast(asPrimitive(key).getValue()));
            if (isPrimitive(value)) {
                entityTuple.add(cast(value.getType().ordinal()));
                entityTuple.add(cast(asPrimitive(value).getValue()));
                continue;
            }

            switch (value.getType()) {
                case ENTITY:
                    writeEntity(entityTuple, asEntity(value));
                    break;
                case ARRAY:
                    writeCollectionValue(entityTuple, asCollection(value));
                    break;
            }
        }
        tuple.add(cast(entityTuple.size()));
        tuple.addAll(cast(entityTuple));
    }

    private static void writeCollectionValue(List<?> tuple, ArrayValue<?> collectionValue) {
        List<?> collectionValueTuple = dynamicArrayOf();
        List<?> valueList = collectionValue.getValueList();
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
            }
        }
        tuple.add(cast(collectionValueTuple.size()));
        tuple.addAll(cast(collectionValueTuple));
    }
}
