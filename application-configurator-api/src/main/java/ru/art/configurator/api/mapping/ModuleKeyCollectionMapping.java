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

package ru.art.configurator.api.mapping;


import ru.art.configurator.api.entity.*;
import ru.art.entity.*;
import ru.art.entity.mapper.*;
import static java.util.stream.Collectors.*;
import static ru.art.configurator.api.mapping.ModuleKeyMapping.*;
import static ru.art.entity.CollectionValuesFactory.*;
import static ru.art.entity.mapper.ValueMapper.*;
import java.util.*;

public interface ModuleKeyCollectionMapping {
    ValueToModelMapper<Set<ModuleKey>, CollectionValue<Entity>> toModel = collectionValue -> collectionValue.getEntityList().stream().map(moduleKeyMapper.getToModel()::map).collect(toSet());
    ValueFromModelMapper<Set<ModuleKey>, CollectionValue<Entity>> fromModel = keyCollection -> entityCollection(keyCollection.stream().map(moduleKeyMapper.getFromModel()::map).collect(toSet()));
    ValueMapper<Set<ModuleKey>, CollectionValue<Entity>> moduleKeyCollectionMapper = mapper(fromModel, toModel);
}
