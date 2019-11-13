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

package ru.art.information.mapping;

import ru.art.entity.Entity;
import ru.art.entity.PrimitiveMapping;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.RsocketInformation;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

public interface RsocketInformationMapper {
    String webSocketUrl = "webSocketUrl";

    String tcpUrl = "tcpUrl";

    String services = "services";

    ValueToModelMapper<RsocketInformation, Entity> toRsocketInformation = entity -> isNotEmpty(entity) ? RsocketInformation.builder()
            .webSocketUrl(entity.getString(webSocketUrl))
            .tcpUrl(entity.getString(tcpUrl))
            .services(entity.getMap(services, PrimitiveMapping.StringPrimitive.toModel, RsocketServiceInformationMapper.toRsocketServiceInformation))
            .build() : RsocketInformation.builder().build();

    ValueFromModelMapper<RsocketInformation, Entity> fromRsocketInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
            .stringField(webSocketUrl, model.getWebSocketUrl())
            .stringField(tcpUrl, model.getTcpUrl())
            .mapField(services, model.getServices(), PrimitiveMapping.StringPrimitive.fromModel, RsocketServiceInformationMapper.fromRsocketServiceInformation)
            .build() : Entity.entityBuilder().build();
}
