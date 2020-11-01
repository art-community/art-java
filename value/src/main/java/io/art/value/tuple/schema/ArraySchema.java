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

import com.google.common.collect.*;
import io.art.value.immutable.*;
import lombok.*;
import static com.google.common.collect.ImmutableList.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static io.art.value.constants.ValueConstants.ValueType.*;
import java.util.*;

@Getter
public class ArraySchema extends ValueSchema {
    private final ImmutableList<ValueSchema> elements;

    ArraySchema(ArrayValue array) {
        super(ARRAY);
        elements = array.asStream()
                .map(ValueSchema::fromValue)
                .filter(Objects::nonNull)
                .collect(toImmutableList());
    }

    ArraySchema(ImmutableList<ValueSchema> elements) {
        super(ARRAY);
        this.elements = elements;
    }

    @Override
    public List<?> toTuple() {
        List<?> tuple = dynamicArrayOf(getType().ordinal());
        elements.stream().map(ValueSchema::toTuple).forEach(value -> tuple.add(cast(value)));
        return tuple;
    }

    public static ValueSchema fromTuple(List<?> tuple) {
        return new ArraySchema(tuple.stream()
                .skip(1)
                .map(element -> (List<?>) element)
                .map(ValueSchema::fromTuple)
                .filter(Objects::nonNull)
                .collect(toImmutableList()));
    }
}
