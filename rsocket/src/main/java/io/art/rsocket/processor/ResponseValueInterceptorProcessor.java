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

package io.art.rsocket.processor;

import io.art.entity.immutable.*;
import lombok.experimental.*;
import io.art.entity.interceptor.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import static io.art.core.constants.InterceptionStrategy.*;
import java.util.*;

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
            if (result.getNextInterceptionStrategy() == PROCESS) {
                break;
            }
            if (result.getNextInterceptionStrategy() == TERMINATE) {
                return ofNullable(result.getOutValue());
            }
        }
        return ofNullable(responseEntity);
    }
}
