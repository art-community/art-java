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

package io.art.entity.mapping;

import io.art.entity.immutable.*;
import io.art.entity.mapper.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.entity.factory.ArrayFactory.*;
import static java.util.stream.Collectors.*;
import java.util.*;

@UtilityClass
public class ArrayMapping {
    public static <T> ValueToModelMapper<Collection<T>, ArrayValue> toArray(ValueToModelMapper<T, Value> elementMapper) {
        return collection -> isEmpty(collection) ? null : collection.mapToList(elementMapper)
                .stream()
                .map(element -> elementMapper.map(cast(element)))
                .collect(toList());
    }

    public static <T> ValueFromModelMapper<Collection<T>, ArrayValue> fromArray(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return collection -> let(collection, notNullCollection -> array(collection
                .stream()
                .map(element -> elementMapper.map(cast(element)))
                .collect(toList())));
    }
}
