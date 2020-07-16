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

import io.art.entity.immutable.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.immutable.Value.*;
import static java.util.Collections.*;
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
            case BINARY:
                tuple.add(cast(asBinary(value).getContent()));
                break;
            case ARRAY:
                writeArray(tuple, asArray(value));
                break;
        }
        return tuple;
    }

    private static void writeEntity(List<?> tuple, Entity entity) {
        List<?> entityTuple = dynamicArrayOf();
        Map<? extends Value, ? extends Value> fields = entity.asMap();
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
                case BINARY:
                    entityTuple.add(cast(asBinary(value).getContent()));
                    break;
                case ARRAY:
                    writeArray(entityTuple, asArray(value));
                    break;
            }
        }
        tuple.add(cast(entityTuple.size()));
        tuple.addAll(cast(entityTuple));
    }

    private static void writeArray(List<?> tuple, ArrayValue array) {
        List<?> arrayTuple = dynamicArrayOf();
        List<Value> valueList = array.asList();
        tuple.add(cast(array.getType().ordinal()));
        for (Value value : valueList) {
            arrayTuple.add(cast(writeTuple(value)));
        }
        tuple.add(cast(arrayTuple.size()));
        tuple.addAll(cast(arrayTuple));
    }
}
