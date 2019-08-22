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
import java.util.function.*;

import static ru.art.core.caster.Caster.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class RsocketFunctionalServiceSpecification implements RsocketReactiveServiceSpecification {
    private final String serviceId;
    private final RsocketService rsocketService;
    private ReactiveService reactiveService;
    private final Function<?, ?> function;


    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        return cast(function.apply(cast(request)));
    }
}