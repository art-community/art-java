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

package ru.art.rsocket.model;

import lombok.Builder;
import lombok.Getter;
import ru.art.reactive.service.specification.ReactiveServiceSpecification;
import ru.art.rsocket.exception.RsocketServerException;
import ru.art.rsocket.specification.RsocketServiceSpecification;
import ru.art.service.Specification;
import ru.art.service.model.ServiceMethodCommand;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.REACTIVE_SERVICE_TYPE;
import static ru.art.reactive.service.model.ReactiveService.ReactiveMethod;
import static ru.art.reactive.service.model.ReactiveService.ReactiveMethod.reactiveMethod;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_SERVICE_TYPE;
import static ru.art.rsocket.service.RsocketService.RsocketMethod;
import static ru.art.service.ServiceModule.serviceModule;

@Getter
@Builder
public class RsocketReactiveMethods {
    private final RsocketMethod rsocketMethod;
    private final ReactiveMethod reactiveMethod;

    static RsocketReactiveMethods fromCommand(ServiceMethodCommand command) {
        Specification specification = serviceModule().getServiceRegistry()
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
