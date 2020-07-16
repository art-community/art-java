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

package io.art.rsocket.service;

import io.art.entity.immutable.*;
import lombok.*;
import lombok.experimental.*;
import io.art.entity.interceptor.*;
import io.art.entity.mapper.*;
import io.art.service.constants.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.service.constants.RequestValidationPolicy.*;
import java.util.*;

@Getter
@Builder(builderMethodName = "rsocketService", buildMethodName = "serve")
public class RsocketService {
    @Singular("method")
    private final Map<String, RsocketMethod> rsocketMethods;

    @Getter
    @Setter
    @Accessors(fluent = true)
    @NoArgsConstructor(staticName = "rsocketMethod")
    public static class RsocketMethod {
        private ValueToModelMapper<?, ?> requestMapper;
        private ValueFromModelMapper<?, ?> responseMapper;
        private RequestValidationPolicy validationPolicy = NON_VALIDATABLE;
        private RsocketDataFormat overrideResponseDataFormat;
        private List<ValueInterceptor<Entity, Entity>> requestValueInterceptors = linkedListOf(rsocketModule().getRequestValueInterceptors());
        private List<ValueInterceptor<Entity, Entity>> responseValueInterceptors = linkedListOf(rsocketModule().getResponseValueInterceptors());

        public RsocketMethod addRequestValueInterceptor(ValueInterceptor<Entity, Entity> interceptor) {
            requestValueInterceptors.add(interceptor);
            return this;
        }

        public RsocketMethod addResponseValueInterceptor(ValueInterceptor<Entity, Entity> interceptor) {
            responseValueInterceptors.add(interceptor);
            return this;
        }
    }
}
