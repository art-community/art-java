/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.configurator.api.mapping;


import ru.art.configurator.api.entity.ModuleKey;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.mapper.ValueMapper.mapper;

public interface ModuleKeyMapping {
    EntityToModelMapper<ModuleKey> toModel = entity -> ModuleKey.builder()
            .profileId(entity.getString("profileId"))
            .moduleId(entity.getString("moduleId"))
            .build();

    EntityFromModelMapper<ModuleKey> fromModel = entity -> entityBuilder()
            .stringField("profileId", entity.getProfileId())
            .stringField("moduleId", entity.getModuleId())
            .build();

    ValueMapper<ModuleKey, Entity> moduleKeyMapper = mapper(fromModel, toModel);
}
