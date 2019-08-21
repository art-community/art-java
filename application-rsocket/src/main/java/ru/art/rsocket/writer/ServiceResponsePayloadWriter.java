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

package ru.art.rsocket.writer;

import io.rsocket.Payload;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;
import ru.art.entity.Entity;
import ru.art.entity.interceptor.ValueInterceptionResult;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import ru.art.rsocket.service.RsocketService.RsocketMethod;
import ru.art.service.exception.ServiceExecutionException;
import ru.art.service.model.ServiceResponse;
import static io.rsocket.util.ByteBufPayload.create;
import static io.rsocket.util.DefaultPayload.EMPTY_BUFFER;
import static java.util.Objects.isNull;
import static reactor.core.publisher.Flux.*;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.InterceptionStrategy.PROCESS_HANDLING;
import static ru.art.core.constants.InterceptionStrategy.STOP_HANDLING;
import static ru.art.rsocket.constants.RsocketModuleConstants.REACTIVE_SERVICE_EXCEPTION_ERROR_CODE;
import static ru.art.rsocket.processor.ResponseValueInterceptorProcessor.processResponseValueInterceptors;
import static ru.art.rsocket.writer.RsocketPayloadWriter.writePayload;
import static ru.art.service.factory.ServiceResponseFactory.okResponse;
import static ru.art.service.mapping.ServiceResponseMapping.fromServiceResponse;
import java.util.List;
import java.util.Optional;

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
            if (result.getNextInterceptionStrategy() == PROCESS_HANDLING) {
                break;
            }
            if (result.getNextInterceptionStrategy() == STOP_HANDLING) {
                if (isNull(result.getOutValue())) {
                    return create(EMPTY_BUFFER);
                }
                return writePayload(result.getOutValue(), dataFormat);
            }
        }
        return isNull(responseMapper) ?
                create(EMPTY_BUFFER) :
                create(writePayload(responseValue, dataFormat));
    }

    public static Flux<Payload> writeResponseReactive(RsocketMethod rsocketMethod, ServiceResponse<?> serviceResponse, RsocketDataFormat dataFormat) {
        ValueFromModelMapper<?, ?> responseMapper = rsocketMethod.responseMapper();
        return isNull(serviceResponse) || isNull(responseMapper) || isNull(serviceResponse.getResponseData()) ?
                never() :
                from(cast(serviceResponse.getResponseData()))
                        .map(response -> fromServiceResponse(responseMapper).map(cast(okResponse(serviceResponse.getCommand(), response))))
                        .map(responseValue -> processResponseValueInterceptors(responseValue, rsocketMethod.responseValueInterceptors()))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(responseValue -> writePayload(responseValue, dataFormat))
                        .onErrorResume(error -> just(writePayload(fromServiceResponse(responseMapper)
                                .map(cast(ServiceResponse.builder()
                                        .command(serviceResponse.getCommand())
                                        .serviceException(new ServiceExecutionException(serviceResponse.getCommand(),
                                                REACTIVE_SERVICE_EXCEPTION_ERROR_CODE, error))
                                        .build())), dataFormat)));
    }

}
