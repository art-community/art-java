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

package io.art.entity.tuple.schema;

import lombok.*;
import io.art.entity.Value;
import io.art.entity.constants.*;
import static java.util.Objects.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.factory.CollectionsFactory.*;
import java.util.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ValueSchema {
    private final ValueType type;

    public static ValueSchema fromValue(Value value) {
        if (isNull(value)) {
            return null;
        }

        if (Value.isPrimitive(value)) {
            return new ValueSchema(value.getType());
        }

        switch (value.getType()) {
            case ENTITY:
                return new EntitySchema(Value.asEntity(value));
            case ARRAY:
                return new CollectionValueSchema(Value.asCollection(value), Value.asCollection(value).getElementsType());
        }
        return null;
    }

    public List<?> toTuple() {
        return dynamicArrayOf(type.ordinal());
    }

    public static ValueSchema fromTuple(List<?> tuple) {
        if (isEmpty(tuple)) {
            return null;
        }

        if (tuple.size() == 1) {
            return new ValueSchema(ValueType.values()[((Integer) tuple.get(0))]);
        }

        ValueType valueType = ValueType.values()[((Integer) tuple.get(0))];
        if (Value.isPrimitiveType(valueType)) {
            return new ValueSchema(valueType);
        }

        switch (valueType) {
            case ENTITY:
                return EntitySchema.fromTuple(tuple);
            case ARRAY:
                return CollectionValueSchema.fromTuple(tuple);
        }

        return null;
    }
}
