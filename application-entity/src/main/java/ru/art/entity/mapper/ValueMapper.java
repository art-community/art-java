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

package ru.art.entity.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.art.entity.Value;
import ru.art.entity.exception.ValueMappingException;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.entity.constants.ValueMappingExceptionMessages.FROM_MODULE_MAPPER_IS_NULL;
import static ru.art.entity.constants.ValueMappingExceptionMessages.TO_MODULE_MAPPER_IS_NULL;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class ValueMapper<T, V extends Value> {
    private final ValueFromModelMapper<T, V> fromModel;
    private final ValueToModelMapper<T, V> toModel;

    public static <T, V extends Value> ValueMapper<T, V> mapper(ValueFromModelMapper<T, V> fromModel, ValueToModelMapper<T, V> toModel) {
        if (isNull(fromModel)) throw new ValueMappingException(FROM_MODULE_MAPPER_IS_NULL);
        if (isNull(toModel)) throw new ValueMappingException(TO_MODULE_MAPPER_IS_NULL);
        return new ValueMapper<>(fromModel, toModel);
    }

    public T map(V value) {
        return toModel.map(value);
    }

    public V map(T value) {
        return fromModel.map(value);
    }
}
