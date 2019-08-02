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
import ru.art.state.api.model.LockRequest;

public interface LockRequestMapper {
    ValueToModelMapper<LockRequest, Entity> toLockRequest = entity -> LockRequest.builder()
            .name(entity.getString("name"))
            .build();

    ValueFromModelMapper<LockRequest, Entity> fromLockRequest = model -> Entity.entityBuilder()
            .stringField("name", model.getName())
            .build();
}
