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

package io.art.value.tuple.schema;

import io.art.core.factory.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.art.value.immutable.Value;
import lombok.*;
import static io.art.core.checker.EmptinessChecker.*;
import static java.util.Objects.isNull;

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
            case BINARY:
                return new ValueSchema(value.getType());
            case ARRAY:
                return new ArraySchema(Value.asArray(value));
        }
        return null;
    }

    public List<?> toTuple() {
        return ArrayFactory.dynamicArrayOf(type.ordinal());
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
            case BINARY:
                return ValueSchema.fromTuple(tuple);
            case ARRAY:
                return ArraySchema.fromTuple(tuple);
        }

        return null;
    }
}
