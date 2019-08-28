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

import ru.art.entity.mapper.*;
import static ru.art.entity.mapper.ValueMapper.*;
import java.util.*;

public interface CollectionMapping {
    ValueMapper<Collection<String>, CollectionValue<String>> stringCollectionMapper = mapper(CollectionValuesFactory::stringCollection, CollectionValue::getElements);
    ValueMapper<Collection<Integer>, CollectionValue<Integer>> intCollectionMapper = mapper(CollectionValuesFactory::intCollection, CollectionValue::getElements);
    ValueMapper<Collection<Double>, CollectionValue<Double>> doubleCollectionMapper = mapper(CollectionValuesFactory::doubleCollection, CollectionValue::getElements);
    ValueMapper<Collection<Float>, CollectionValue<Float>> floatCollectionMapper = mapper(CollectionValuesFactory::floatCollection, CollectionValue::getElements);
    ValueMapper<Collection<Boolean>, CollectionValue<Boolean>> boolCollectionMapper = mapper(CollectionValuesFactory::boolCollection, CollectionValue::getElements);
    ValueMapper<Collection<Long>, CollectionValue<Long>> longCollectionMapper = mapper(CollectionValuesFactory::longCollection, CollectionValue::getElements);
    ValueMapper<Collection<Entity>, CollectionValue<Entity>> entityCollectionMapper = mapper(CollectionValuesFactory::entityCollection, CollectionValue::getElements);
    ValueMapper<Collection<Value>, CollectionValue<Value>> valueCollectionMapper = mapper(CollectionValuesFactory::valueCollection, CollectionValue::getElements);
}
