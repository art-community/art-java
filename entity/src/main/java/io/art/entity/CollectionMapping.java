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

package io.art.entity;

import lombok.experimental.*;
import io.art.entity.mapper.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.entity.CollectionValuesFactory.*;
import static io.art.entity.mapper.ValueMapper.*;
import java.util.*;

@UtilityClass
public class CollectionMapping {
    public static ValueMapper<Collection<String>, ArrayValue> stringCollectionMapper = mapper(CollectionValuesFactory::stringCollection, ArrayValue::getElements);
    public static ValueMapper<Collection<Integer>, ArrayValue> intCollectionMapper = mapper(CollectionValuesFactory::intCollection, ArrayValue::getElements);
    public static ValueMapper<Collection<Double>, ArrayValue> doubleCollectionMapper = mapper(CollectionValuesFactory::doubleCollection, ArrayValue::getElements);
    public static ValueMapper<Collection<Float>, ArrayValue> floatCollectionMapper = mapper(CollectionValuesFactory::floatCollection, ArrayValue::getElements);
    public static ValueMapper<Collection<Boolean>, ArrayValue> boolCollectionMapper = mapper(CollectionValuesFactory::boolCollection, ArrayValue::getElements);
    public static ValueMapper<Collection<Long>, ArrayValue> longCollectionMapper = mapper(CollectionValuesFactory::longCollection, ArrayValue::getElements);
    public static ValueMapper<Collection<Entity>, ArrayValue> entityCollectionMapper = mapper(CollectionValuesFactory::entityCollection, ArrayValue::getElements);
    public static ValueMapper<Collection<Value>, ArrayValue> valueCollectionMapper = mapper(CollectionValuesFactory::valueCollection, ArrayValue::getElements);

    public static <T> ValueToModelMapper<Collection<T>, ArrayValue> collectionValueToModel(ValueToModelMapper<T, Value> elementMapper) {
        return collection -> isEmpty(collection) ? emptyList() : collection.toList(elementMapper)
                .stream()
                .filter(Objects::nonNull)
                .map(element -> elementMapper.map(cast(element)))
                .collect(toList());
    }

    public static <T> ValueFromModelMapper<Collection<T>, ArrayValue> collectionValueFromModel(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return collection -> isEmpty(collection) ? cast(emptyCollection()) : valueCollection(collection
                .stream()
                .filter(Objects::nonNull)
                .map(element -> elementMapper.map(cast(element)))
                .collect(toList()));
    }
}
