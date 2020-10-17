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

package io.art.rsocket.model;

import io.art.entity.constants.EntityConstants.*;
import io.art.entity.immutable.*;
import lombok.*;
import static io.art.entity.constants.EntityConstants.DataFormat.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.Entity.*;
import static io.art.entity.mapping.PrimitiveMapping.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Fields.*;

@Getter
@Builder
public class RsocketSetupPayload {
    private final DataFormat dataFormat;
    private final DataFormat metadataFormat;
    private final String serviceId;
    private final String methodId;

    public Entity toEntity() {
        return entityBuilder()
                .put(SETUP_PAYLOAD_DATA_FORMAT_FIELD, stringPrimitive(dataFormat.getFormat()))
                .put(SETUP_PAYLOAD_META_DATA_FORMAT_FIELD, stringPrimitive(metadataFormat.getFormat()))
                .put(SETUP_PAYLOAD_SERVICE_ID_FIELD, stringPrimitive(serviceId))
                .put(SETUP_PAYLOAD_METHOD_ID_FIELD, stringPrimitive(methodId))
                .build();
    }

    public static RsocketSetupPayload fromEntity(Entity entity, DataFormat dataFormatFallback, DataFormat metaDataFormatFallback) {
        return RsocketSetupPayload
                .builder()
                .dataFormat(dataFormat(entity.map(SETUP_PAYLOAD_DATA_FORMAT_FIELD, toString), dataFormatFallback))
                .metadataFormat(dataFormat(entity.map(SETUP_PAYLOAD_META_DATA_FORMAT_FIELD, toString), metaDataFormatFallback))
                .serviceId(entity.map(SETUP_PAYLOAD_SERVICE_ID_FIELD, toString))
                .serviceId(entity.map(SETUP_PAYLOAD_METHOD_ID_FIELD, toString))
                .build();
    }
}
