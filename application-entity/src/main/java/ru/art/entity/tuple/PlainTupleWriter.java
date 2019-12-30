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

package ru.art.entity.tuple;

import lombok.*;
import lombok.experimental.*;
import ru.art.entity.Value;
import ru.art.entity.*;
import ru.art.entity.tuple.schema.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.entity.Value.*;
import static ru.art.entity.tuple.schema.ValueSchema.*;
import java.util.*;

@UtilityClass
public class PlainTupleWriter {
    public static PlainTupleWriterResult writeTuple(Value value) {
        ValueSchema schema = fromValue(value);
        if (isEmpty(value)) return null;
        if (isPrimitive(value)) {
            return new PlainTupleWriterResult(fixedArrayOf(asPrimitive(value).getValue()), schema);
        }
        return new PlainTupleWriterResult(writeComplexTypeValue(value), schema);
    }

    private static List<?> writeComplexTypeValue(Value value) {
        if (isEmpty(value)) return emptyList();

        switch (value.getType()) {
            case ENTITY:
                return writeEntity(asEntity(value));
            case COLLECTION:
                return writeCollectionValue(asCollection(value));
            case STRING_PARAMETERS_MAP:
                return writeStringParameters(asStringParametersMap(value));
            case MAP:
                return writeMap(asMap(value));
        }

        return emptyList();
    }

    private static List<?> writeEntity(Entity entity) {
        List<?> tuple = dynamicArrayOf();
        Map<String, ? extends Value> fields = entity.getFields();
        for (Value value : fields.values()) {
            if (isPrimitive(value)) {
                tuple.add(cast(asPrimitive(value).getValue()));
                continue;
            }
            if (nonNull(value)) {
                tuple.add(cast(writeComplexTypeValue(value)));
            }
        }
        return tuple;
    }

    private static List<?> writeCollectionValue(CollectionValue<?> collectionValue) {
        List<?> tuple = dynamicArrayOf();
        List<?> valueList = collectionValue.getList();
        for (Object value : valueList) {
            if (isNull(value)) continue;

            switch (collectionValue.getElementsType()) {
                case STRING:
                case LONG:
                case DOUBLE:
                case FLOAT:
                case INT:
                case BOOL:
                case BYTE:
                    tuple.add(cast(value));
                    break;
                case ENTITY:
                case COLLECTION:
                case MAP:
                case STRING_PARAMETERS_MAP:
                    tuple.add(cast(writeComplexTypeValue((Value) value)));
                    break;
            }
        }
        return tuple;
    }

    private static List<?> writeStringParameters(StringParametersMap stringParameters) {
        List<?> tuple = dynamicArrayOf();
        for (Map.Entry<?, ?> entry : stringParameters.getParameters().entrySet()) {
            tuple.add(cast(entry.getValue()));
        }
        return tuple;
    }

    private static List<?> writeMap(MapValue mapValue) {
        List<?> tuple = dynamicArrayOf();
        for (Map.Entry<? extends Value, ? extends Value> entry : mapValue.getElements().entrySet()) {
            Value key = entry.getKey();
            Value value = entry.getValue();
            if (isNull(value)) continue;

            if (isPrimitive(key)) {
                if (isPrimitive(value)) {
                    tuple.add(cast(asPrimitive(value).getValue()));
                    continue;
                }
                tuple.add(cast(writeComplexTypeValue(value)));
            }
        }
        return tuple;
    }

    @Getter
    @AllArgsConstructor
    public static class PlainTupleWriterResult {
        private List<?> tuple;
        private final ValueSchema schema;
    }
}
