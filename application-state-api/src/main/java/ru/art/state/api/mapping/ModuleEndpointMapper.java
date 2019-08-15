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

package ru.art.state.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.state.api.model.ModuleEndpoint;

public interface ModuleEndpointMapper {
    ValueToModelMapper<ModuleEndpoint, Entity> toModuleEndpoint = entity -> ModuleEndpoint.builder()
            .host(entity.getString("host"))
            .port(entity.getInt("port"))
            .sessions(entity.getInt("sessions"))
            .build();

    ValueFromModelMapper<ModuleEndpoint, Entity> fromModuleEndpoint = model -> Entity.entityBuilder()
            .stringField("host", model.getHost())
            .intField("port", model.getPort())
            .intField("sessions", model.getSessions())
            .build();
}
