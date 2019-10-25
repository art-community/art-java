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

import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.information.model.*;
import static ru.art.core.checker.CheckerForEmptiness.*;

public interface StatusResponseMapper {
	String http = "http";

	String grpc = "grpc";

	String rsocketTcp = "rsocketTcp";

	String rsocketWebSocket = "rsocketWebSocket";

	ValueToModelMapper<StatusResponse, Entity> toStatusResponse = entity -> isNotEmpty(entity) ? StatusResponse.builder()
			.http(entity.getBool(http))
			.grpc(entity.getBool(grpc))
			.rsocketTcp(entity.getBool(rsocketTcp))
			.rsocketWebSocket(entity.getBool(rsocketWebSocket))
			.build() : StatusResponse.builder().build();

	ValueFromModelMapper<StatusResponse, Entity> fromStatusResponse = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.boolField(http, model.isHttp())
			.boolField(grpc, model.isGrpc())
			.boolField(rsocketTcp, model.isRsocketTcp())
			.boolField(rsocketWebSocket, model.isRsocketWebSocket())
			.build() : Entity.entityBuilder().build();
}
