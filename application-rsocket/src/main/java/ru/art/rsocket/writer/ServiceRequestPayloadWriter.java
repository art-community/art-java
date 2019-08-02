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

package ru.art.rsocket.writer;

import io.rsocket.Payload;
import ru.art.entity.Value;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.writer.RsocketPayloadWriter.writePayload;

public interface ServiceRequestPayloadWriter {
    static Payload writeServiceRequestPayload(String serviceId, String methodId, RsocketDataFormat dataFormat) {
        return writePayload(entityBuilder().stringField(SERVICE_ID, serviceId).stringField(METHOD_ID, methodId).build(), dataFormat);
    }

    static Payload writeServiceRequestPayload(String serviceId, String methodId, Value requestData, RsocketDataFormat dataFormat) {
        return writePayload(entityBuilder().stringField(SERVICE_ID, serviceId).stringField(METHOD_ID, methodId).valueField(REQUEST_DATA, requestData).build(), dataFormat);
    }
}
