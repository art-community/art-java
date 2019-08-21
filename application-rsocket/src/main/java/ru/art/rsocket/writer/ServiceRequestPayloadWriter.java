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

package ru.art.rsocket.writer;

import io.rsocket.*;
import ru.art.entity.*;
import ru.art.entity.interceptor.*;
import java.util.*;

import static io.rsocket.util.DefaultPayload.*;
import static java.util.Objects.*;
import static ru.art.core.constants.InterceptionStrategy.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.writer.RsocketPayloadWriter.*;

public interface ServiceRequestPayloadWriter {
    static Payload writeServiceRequestPayload(Entity requestValue, List<ValueInterceptor<Entity, Entity>> requestValueInterceptors, RsocketDataFormat dataFormat) {
        Entity requestEntity = requestValue;
        for (ValueInterceptor<Entity, Entity> requestValueInterceptor : requestValueInterceptors) {
            ValueInterceptionResult<Entity, Entity> result = requestValueInterceptor.intercept(requestEntity);
            if (isNull(result)) {
                break;
            }
            requestEntity = result.getOutValue();
            if (result.getNextInterceptionStrategy() == PROCESS_HANDLING) {
                break;
            }
            if (result.getNextInterceptionStrategy() == STOP_HANDLING) {
                if (isNull(result.getOutValue())) {
                    return create(EMPTY_BUFFER);
                }
                return writePayload(result.getOutValue(), dataFormat);
            }
        }
        return writePayload(requestEntity, dataFormat);
    }
}
