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

package io.art.rsocket.specification;

import io.art.reactive.service.specification.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.reactive.service.constants.ReactiveServiceModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import java.util.*;

public interface RsocketReactiveServiceSpecification extends RsocketServiceSpecification, ReactiveServiceSpecification {
    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(REACTIVE_SERVICE_TYPE, RSOCKET_SERVICE_TYPE);
    }

}
