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

package ru.art.rsocket.function;

import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode;
import ru.art.reactive.service.model.ReactiveService;
import ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import ru.art.rsocket.service.RsocketService;
import ru.art.rsocket.service.RsocketService.RsocketMethod;
import ru.art.service.constants.RequestValidationPolicy;
import static java.util.UUID.randomUUID;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.CharConstants.UNDERSCORE;
import static ru.art.reactive.service.model.ReactiveService.ReactiveMethod;
import static ru.art.rsocket.constants.RsocketModuleConstants.EXECUTE_RSOCKET_FUNCTION;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_SERVICE_TYPE;
import static ru.art.service.ServiceModule.serviceModule;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RsocketServiceFunction {
    private RsocketMethod rsocketMethod = RsocketMethod.rsocketMethod();
    private ReactiveMethod reactiveMethod = ReactiveMethod.reactiveMethod();
    private final String serviceId;

    private RsocketServiceFunction(String serviceId) {
        this.serviceId = serviceId;
    }

    public RsocketServiceFunction responseMapper(ValueFromModelMapper responseMapper) {
        rsocketMethod.responseMapper(responseMapper);
        return this;
    }

    public RsocketServiceFunction overrideResponseDataFormat(RsocketDataFormat dataFormat) {
        rsocketMethod.overrideResponseDataFormat(dataFormat);
        return this;
    }

    public RsocketServiceFunction requestMapper(ValueToModelMapper requestMapper) {
        rsocketMethod.requestMapper(requestMapper);
        return this;
    }

    public RsocketServiceFunction requestProcessingMode(ReactiveMethodProcessingMode methodProcessingMode) {
        reactiveMethod.requestProcessingMode(methodProcessingMode);
        return this;
    }

    public RsocketServiceFunction responseProcessingMode(ReactiveMethodProcessingMode methodProcessingMode) {
        reactiveMethod.responseProcessingMode(methodProcessingMode);
        return this;
    }

    public RsocketServiceFunction validationPolicy(RequestValidationPolicy policy) {
        rsocketMethod.validationPolicy(policy);
        return this;
    }

    public void handle(Function<?, ?> function) {
        serviceModule()
                .getServiceRegistry()
                .registerService(new RsocketFunctionalServiceSpecification(serviceId,
                        RsocketService.rsocketService()
                                .method(EXECUTE_RSOCKET_FUNCTION, rsocketMethod)
                                .serve(),
                        ReactiveService.reactiveService()
                                .method(EXECUTE_RSOCKET_FUNCTION, reactiveMethod)
                                .serve(),
                        function));
    }

    public void consume(Consumer<?> consumer) {
        handle(request -> {
            consumer.accept(cast(request));
            return null;
        });
    }

    public void produce(Supplier<?> producer) {
        handle(request -> producer.get());
    }

    public static RsocketServiceFunction rsocket(String serviceId) {
        return new RsocketServiceFunction(serviceId);
    }
}