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

package ru.art.rsocket.function;

import lombok.*;
import ru.art.reactive.service.model.*;
import ru.art.rsocket.service.*;
import ru.art.rsocket.specification.*;
import static java.util.Objects.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.reactive.service.model.ReactiveService.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.service.RsocketService.*;
import java.util.*;
import java.util.function.*;

@Getter
public class RsocketFunctionalServiceSpecification implements RsocketReactiveServiceSpecification {
    private final String serviceId = RSOCKET_FUNCTION_SERVICE;
    private final Map<String, Function<?, ?>> functions = mapOf();
    private final ReactiveService.ReactiveServiceBuilder reactiveServiceBuilder = ReactiveService.reactiveService();
    private final RsocketService.RsocketServiceBuilder rsocketServiceBuilder = RsocketService.rsocketService();

    @Override
    public ReactiveService getReactiveService() {
        return reactiveServiceBuilder.serve();
    }

    @Override
    public RsocketService getRsocketService() {
        return rsocketServiceBuilder.serve();
    }

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        Function<?, ?> function;
        if (isNull(function = functions.get(methodId))) {
            return null;
        }
        return cast(function.apply(cast(request)));
    }

    public void addFunction(String id, RsocketMethod rsocketMethod, ReactiveMethod reactiveMethod, Function<?, ?> function) {
        reactiveServiceBuilder.method(id, reactiveMethod);
        rsocketServiceBuilder.method(id, rsocketMethod);
        functions.put(id, function);
    }
}