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

import lombok.*;
import io.art.reactive.service.specification.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.specification.*;
import io.art.service.*;
import io.art.service.model.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.reactive.service.constants.ReactiveServiceModuleConstants.*;
import static io.art.reactive.service.model.ReactiveService.*;
import static io.art.reactive.service.model.ReactiveService.ReactiveMethod.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.service.RsocketService.*;
import static io.art.service.ServerModule.*;

@Getter
@Builder
public class RsocketReactiveMethods {
    private final RsocketMethod rsocketMethod;
    private final ReactiveMethod reactiveMethod;

    static RsocketReactiveMethods fromCommand(ServiceMethodCommand command) {
        Specification specification = serviceModuleState().getServiceRegistry()
                .getServices()
                .get(command.getServiceId());
        if (isNull(specification)) {
            throw new RsocketServerException(format(SERVICE_NOT_EXISTS, command.getServiceId()));
        }
        if (!specification.getServiceTypes().contains(RSOCKET_SERVICE_TYPE)) {
            throw new RsocketServerException(format(SERVICE_NOT_SUPPORTED_RSOCKET, command.getServiceId()));
        }
        RsocketMethod rsocketMethod;
        if (isNull(rsocketMethod = ((RsocketServiceSpecification) specification).getRsocketService().getRsocketMethods().get(command.getMethodId()))) {
            throw new RsocketServerException(format(METHOD_NOT_EXISTS, command.getMethodId(), command.getServiceId()));
        }

        if (!specification.getServiceTypes().contains(REACTIVE_SERVICE_TYPE)) {
            return RsocketReactiveMethods.builder().rsocketMethod(rsocketMethod).reactiveMethod(reactiveMethod()).build();
        }

        return RsocketReactiveMethods.builder()
                .rsocketMethod(rsocketMethod)
                .reactiveMethod(getOrElse(((ReactiveServiceSpecification) specification).getReactiveService().getMethods().get(command.getMethodId()), reactiveMethod()))
                .build();
    }
}
