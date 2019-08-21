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

package ru.art.rsocket.processor;

import lombok.experimental.UtilityClass;
import ru.art.entity.Entity;
import ru.art.entity.interceptor.ValueInterceptionResult;
import ru.art.entity.interceptor.ValueInterceptor;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static ru.art.core.constants.InterceptionStrategy.PROCESS_HANDLING;
import static ru.art.core.constants.InterceptionStrategy.STOP_HANDLING;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class ResponseValueInterceptorProcessor {
    public static Optional<Entity> processResponseValueInterceptors(Entity responseValue, List<ValueInterceptor<Entity, Entity>> responseValueInterceptors) {
        Entity responseEntity = responseValue;
        for (ValueInterceptor<Entity, Entity> responseValueInterceptor : responseValueInterceptors) {
            ValueInterceptionResult<Entity, Entity> result = responseValueInterceptor.intercept(responseEntity);
            if (isNull(result)) {
                break;
            }
            responseEntity = result.getOutValue();
            if (result.getNextInterceptionStrategy() == PROCESS_HANDLING) {
                break;
            }
            if (result.getNextInterceptionStrategy() == STOP_HANDLING) {
                return ofNullable(result.getOutValue());
            }
        }
        return ofNullable(responseEntity);
    }
}
