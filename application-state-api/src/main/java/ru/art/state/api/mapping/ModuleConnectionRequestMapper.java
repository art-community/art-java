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

package ru.art.state.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.state.api.model.ModuleConnectionRequest;

public interface ModuleConnectionRequestMapper {
    ValueToModelMapper<ModuleConnectionRequest, Entity> toModuleConnectionRequest = entity -> ModuleConnectionRequest.builder()
            .profile(entity.getString("profile"))
            .modulePath(entity.getString("modulePath"))
            .moduleEndpoint(entity.getValue("moduleEndpoint", ModuleEndpointMapper.toModuleEndpoint))
            .build();

    ValueFromModelMapper<ModuleConnectionRequest, Entity> fromModuleConnectionRequest = model -> Entity.entityBuilder()
            .stringField("profile", model.getProfile())
            .stringField("modulePath", model.getModulePath())
            .entityField("moduleEndpoint", model.getModuleEndpoint(), ModuleEndpointMapper.fromModuleEndpoint)
            .build();
}
