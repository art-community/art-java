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

package ru.art.entity.tuple.schema;

import lombok.*;
import ru.art.entity.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.entity.constants.ValueType.*;
import java.util.*;

@Getter
@EqualsAndHashCode(callSuper = true)
public class StringParametersSchema extends ValueSchema {
    private final List<String> stringParametersSchema = dynamicArrayOf();

    StringParametersSchema(StringParametersMap parameters) {
        super(STRING_PARAMETERS_MAP);
        stringParametersSchema.addAll(parameters.getParameters().keySet());
    }

    private StringParametersSchema() {
        super(STRING_PARAMETERS_MAP);
    }

    @Override
    public List<?> toTuple() {
        List<?> tuple = dynamicArrayOf(getType().name());
        tuple.add(cast(stringParametersSchema));
        return tuple;
    }

    public static StringParametersSchema fromTuple(List<?> tuple) {
        StringParametersSchema schema = new StringParametersSchema();
        tuple.stream()
                .skip(1)
                .map(element -> (List<?>) element)
                .map(element -> (String) element.get(0))
                .forEach(schema.stringParametersSchema::add);
        return schema;
    }
}