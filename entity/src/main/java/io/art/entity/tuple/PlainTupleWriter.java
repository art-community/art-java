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

import io.art.entity.immutable.Value;
import io.art.entity.immutable.*;
import io.art.entity.tuple.schema.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.constants.ValueType.*;
import static io.art.entity.immutable.Value.*;
import static io.art.entity.tuple.schema.ValueSchema.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import java.util.*;

@UtilityClass
public class PlainTupleWriter {
    public static PlainTupleWriterResult writeTuple(Value value) {
        ValueSchema schema = fromValue(value);
        if (isNull(schema) || isEmpty(value)) return null;
        if (isPrimitive(value)) {
            return new PlainTupleWriterResult(fixedArrayOf(asPrimitive(value).getValue()), schema);
        }
        if (value.getType() == BINARY) {
            return new PlainTupleWriterResult(fixedArrayOf(asBinary(value).getContent()), schema);

        }
        return new PlainTupleWriterResult(writeValue(value), schema);
    }

    private static List<?> writeValue(Value value) {
        if (isEmpty(value)) return emptyList();

        switch (value.getType()) {
            case ENTITY:
                return writeEntity(asEntity(value));
            case ARRAY:
                return writeArray(asArray(value));
        }

        return emptyList();
    }

    private static List<?> writeEntity(Entity entity) {
        List<?> tuple = dynamicArrayOf();
        Map<Primitive, ? extends Value> fields = entity.asMap();
        for (Primitive key : fields.keySet()) {
            if (isEmpty(key)) continue;
            Value value = entity.get(key);
            if (isNull(value)) continue;
            if (isPrimitive(value)) {
                tuple.add(cast(asPrimitive(value).getValue()));
                continue;
            }
            if (value.getType() == BINARY) {
                tuple.add(cast(asBinary(value).getContent()));
                continue;
            }
            tuple.add(cast(writeValue(value)));
        }
        return tuple;
    }

    private static List<?> writeArray(ArrayValue array) {
        List<?> tuple = dynamicArrayOf();
        List<Value> valueList = array.asList();
        for (Value value : valueList) {
            if (isPrimitive(value)) {
                tuple.add(cast(asPrimitive(value).getValue()));
                continue;
            }
            if (value.getType() == BINARY) {
                tuple.add(cast(asBinary(value).getContent()));
                continue;
            }
            tuple.add(cast(writeValue(value)));
        }
        return tuple;
    }

    @Getter
    @AllArgsConstructor
    public static class PlainTupleWriterResult {
        private final List<?> tuple;
        private final ValueSchema schema;
    }
}
