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

package io.art.rsocket.writer;

import io.art.entity.immutable.*;
import io.rsocket.*;
import io.art.entity.interceptor.*;
import static io.rsocket.util.DefaultPayload.*;
import static java.util.Objects.*;
import static io.art.core.constants.InterceptionStrategy.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.writer.RsocketPayloadWriter.*;
import java.util.*;

public interface ServiceRequestPayloadWriter {
    static Payload writeServiceRequestPayload(Entity requestValue, List<ValueInterceptor<Entity, Entity>> requestValueInterceptors, RsocketDataFormat dataFormat) {
        Entity requestEntity = requestValue;
        for (ValueInterceptor<Entity, Entity> requestValueInterceptor : requestValueInterceptors) {
            ValueInterceptionResult<Entity, Entity> result = requestValueInterceptor.intercept(requestEntity);
            if (isNull(result)) {
                break;
            }
            requestEntity = result.getOutValue();
            if (result.getNextInterceptionStrategy() == PROCESS) {
                break;
            }
            if (result.getNextInterceptionStrategy() == TERMINATE) {
                if (isNull(result.getOutValue())) {
                    return create(EMPTY_BUFFER);
                }
                return writePayloadData(result.getOutValue(), dataFormat);
            }
        }
        return writePayloadData(requestEntity, dataFormat);
    }
}
