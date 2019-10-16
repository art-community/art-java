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

package ru.art.entity;

import lombok.experimental.*;
import ru.art.entity.mapper.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.entity.CollectionValuesFactory.*;
import static ru.art.entity.mapper.ValueMapper.*;
import java.util.*;

@UtilityClass
public class CollectionMapping {
    public static ValueMapper<Collection<String>, CollectionValue<String>> stringCollectionMapper = mapper(CollectionValuesFactory::stringCollection, CollectionValue::getElements);
    public static ValueMapper<Collection<Integer>, CollectionValue<Integer>> intCollectionMapper = mapper(CollectionValuesFactory::intCollection, CollectionValue::getElements);
    public static ValueMapper<Collection<Double>, CollectionValue<Double>> doubleCollectionMapper = mapper(CollectionValuesFactory::doubleCollection, CollectionValue::getElements);
    public static ValueMapper<Collection<Float>, CollectionValue<Float>> floatCollectionMapper = mapper(CollectionValuesFactory::floatCollection, CollectionValue::getElements);
    public static ValueMapper<Collection<Boolean>, CollectionValue<Boolean>> boolCollectionMapper = mapper(CollectionValuesFactory::boolCollection, CollectionValue::getElements);
    public static ValueMapper<Collection<Long>, CollectionValue<Long>> longCollectionMapper = mapper(CollectionValuesFactory::longCollection, CollectionValue::getElements);
    public static ValueMapper<Collection<Entity>, CollectionValue<Entity>> entityCollectionMapper = mapper(CollectionValuesFactory::entityCollection, CollectionValue::getElements);
    public static ValueMapper<Collection<Value>, CollectionValue<Value>> valueCollectionMapper = mapper(CollectionValuesFactory::valueCollection, CollectionValue::getElements);

    public static <T> ValueToModelMapper<Collection<T>, CollectionValue<? extends Value>> collectionValueToModel(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return collection -> isEmpty(collection) ? emptyList(): collection.getElements()
                .stream()
                .filter(Objects::nonNull)
                .map(element -> elementMapper.map(cast(element)))
                .collect(toList());
    }

    public static <T> ValueFromModelMapper<Collection<T>, CollectionValue<? extends Value>> collectionValueFromModel(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return collection -> isEmpty(collection) ? cast(emptyCollection()) : valueCollection(collection
                .stream()
                .filter(Objects::nonNull)
                .map(element -> elementMapper.map(cast(element)))
                .collect(toList()));
    }
}
