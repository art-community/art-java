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

package io.art.rsocket.writer;

import io.art.entity.immutable.*;
import io.rsocket.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import io.art.entity.interceptor.*;
import io.art.entity.mapper.*;
import io.art.reactive.service.wrapper.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.model.*;
import io.art.rsocket.service.RsocketService.*;
import static io.rsocket.util.ByteBufPayload.create;
import static io.rsocket.util.DefaultPayload.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.InterceptionStrategy.*;
import static io.art.rsocket.processor.ResponseValueInterceptorProcessor.*;
import static io.art.rsocket.writer.RsocketPayloadWriter.*;
import static io.art.service.factory.ServiceResponseFactory.*;
import static io.art.service.mapping.ServiceResponseMapping.*;
import java.util.*;

@UtilityClass
public class ServiceResponsePayloadWriter {
    public static Payload writeServiceResponse(RsocketMethod rsocketMethod, ServiceResponse<?> serviceResponse, RsocketDataFormat dataFormat) {
        ValueFromModelMapper<?, ?> responseMapper = rsocketMethod.responseMapper();
        Entity responseValue = fromServiceResponse(responseMapper).map(cast(serviceResponse));
        List<ValueInterceptor<Entity, Entity>> responseValueInterceptors = rsocketMethod.responseValueInterceptors();
        for (ValueInterceptor<Entity, Entity> responseValueInterceptor : responseValueInterceptors) {
            ValueInterceptionResult<Entity, Entity> result = responseValueInterceptor.intercept(responseValue);
            if (isNull(result)) {
                break;
            }
            responseValue = result.getOutValue();
            if (result.getNextInterceptionStrategy() == PROCESS) {
                break;
            }
            if (result.getNextInterceptionStrategy() == TERMINATE) {
                if (isNull(result.getOutValue())) {
                    return create(EMPTY_BUFFER);
                }
                return writePayloadData(result.getOutValue(), dataFormat);
            }
        }
        return create(writePayloadData(responseValue, dataFormat));
    }

    public static Flux<Payload> writeResponseReactive(RsocketReactiveMethods rsocketReactiveMethods, ServiceResponse<?> serviceResponse, RsocketDataFormat dataFormat) {
        if (isNull(serviceResponse)) {
            return empty();
        }
        if (nonNull(serviceResponse.getServiceException())) {
            return error(serviceResponse.getServiceException());
        }
        ValueFromModelMapper<?, ?> responseMapper = rsocketReactiveMethods.getRsocketMethod().responseMapper();
        Flux<Payload> flux = from(cast(serviceResponse.getResponseData()))
                .map(response -> fromServiceResponse(responseMapper).map(cast(okResponse(serviceResponse.getCommand(), response))))
                .map(responseValue -> processResponseValueInterceptors(responseValue, rsocketReactiveMethods.getRsocketMethod().responseValueInterceptors()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> writePayloadData(responseValue, dataFormat));
        ReactiveServiceExceptionWrappers exceptionWrappers = rsocketReactiveMethods.getReactiveMethod().reactiveServiceExceptionWrappers();
        return exceptionWrappers
                .getReactiveServiceExceptionWrappers()
                .entrySet()
                .stream()
                .reduce(flux, (resultFlux, entry) -> resultFlux
                        .onErrorResume(entry.getKey(), exception -> cast(just(writePayloadData(fromServiceResponse(responseMapper).map(cast(ServiceResponse.builder()
                                .command(serviceResponse.getCommand())
                                .serviceException(entry.getValue().wrap(serviceResponse.getCommand(), cast(exception)))
                                .build())), dataFormat)))), (current, next) -> next)
                .onErrorResume(Throwable.class, exception -> just(writePayloadData(fromServiceResponse(responseMapper).map(cast(ServiceResponse.builder()
                        .command(serviceResponse.getCommand())
                        .serviceException(exceptionWrappers.getUndeclaredExceptionWrapper().wrap(serviceResponse.getCommand(), exception))
                        .build())), dataFormat)));
    }

}
