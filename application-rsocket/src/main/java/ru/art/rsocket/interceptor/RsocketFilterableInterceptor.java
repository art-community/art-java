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

package ru.art.rsocket.interceptor;

import io.rsocket.*;
import io.rsocket.plugins.*;
import io.rsocket.util.*;
import lombok.*;
import lombok.experimental.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import ru.art.entity.Value;
import ru.art.entity.*;
import ru.art.service.exception.*;
import ru.art.service.model.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;
import static ru.art.entity.Value.*;
import static ru.art.rsocket.interceptor.RsocketFilterableInterceptor.InterceptingDataType.*;
import static ru.art.rsocket.module.RsocketModule.*;
import static ru.art.rsocket.reader.RsocketPayloadReader.*;
import static ru.art.rsocket.selector.RsocketDataFormatMimeTypeConverter.*;
import static ru.art.service.constants.ServiceExceptionsMessages.*;
import static ru.art.service.mapping.ServiceRequestMapping.*;
import javax.annotation.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "rsocketInterceptor")
public class RsocketFilterableInterceptor implements RSocketInterceptor {
    private Predicate<ServiceMethodCommand> predicate = request -> true;
    private final RsocketPayloadValueInterceptor interceptor;
    private InterceptingDataType dataType = META_DATA;

    public static RsocketFilterableInterceptor rsocketInterceptor(Predicate<ServiceMethodCommand> predicate, RsocketPayloadValueInterceptor interceptor) {
        RsocketFilterableInterceptor filterableInterceptor = new RsocketFilterableInterceptor(interceptor);
        filterableInterceptor.predicate = predicate;
        return filterableInterceptor;
    }

    public enum InterceptingDataType {
        DATA,
        META_DATA
    }

    @Override
    public RSocket apply(RSocket rsocket) {
       return new RSocketProxy(rsocket) {
            @Override
            public Mono<Void> fireAndForget(@Nonnull Payload payload) {
                Value data = getDataByType(payload);
                if (!isEntity(data)) {
                    return super.fireAndForget(payload);
                }
                if (testServiceMethodCommand(data)) {
                    return interceptor.apply(rsocket, asEntity(data).getValue(REQUEST_DATA)).fireAndForget(payload);
                }
                return super.fireAndForget(payload);
            }

            @Override
            public Mono<Payload> requestResponse(@Nonnull Payload payload) {
                Value data = getDataByType(payload);
                if (!isEntity(data)) {
                    return super.requestResponse(payload);
                }
                if (testServiceMethodCommand(data)) {
                    return interceptor.apply(rsocket, asEntity(data).getValue(REQUEST_DATA)).requestResponse(payload);
                }
                return super.requestResponse(payload);
            }

            @Override
            public Flux<Payload> requestStream(@Nonnull Payload payload) {
                Value data = getDataByType(payload);
                if (!isEntity(data)) {
                    return super.requestStream(payload);
                }
                if (testServiceMethodCommand(data)) {
                    return interceptor.apply(rsocket, asEntity(data).getValue(REQUEST_DATA)).requestStream(payload);
                }
                return super.requestStream(payload);
            }

            @Override
            public Flux<Payload> requestChannel(@Nonnull Publisher<Payload> payloads) {
                AtomicReference<Value> payloadValue = new AtomicReference<>();
                return from(payloads)
                        .doOnNext(payload -> payloadValue.set(getDataByType(payload)))
                        .filter(payload -> nonNull(payloadValue.get()))
                        .filter(payload -> testServiceMethodCommand(payloadValue.get()))
                        .switchIfEmpty(super.requestChannel(payloads))
                        .flatMap(data -> interceptor.apply(rsocket, asEntity(payloadValue.get()).getValue(REQUEST_DATA)).requestChannel(payloads));
            }

            @Override
            public Mono<Void> metadataPush(@Nonnull Payload payload) {
                Value data = getDataByType(payload);
                if (!isEntity(data)) {
                    return super.metadataPush(payload);
                }
                if (testServiceMethodCommand(data)) {
                    return interceptor.apply(rsocket, asEntity(data).getValue(REQUEST_DATA)).metadataPush(payload);
                }
                return super.metadataPush(payload);
            }
        };
    }

    private Value getDataByType(@Nonnull Payload payload) {
        switch (dataType) {
            case DATA:
                return readPayloadData(payload, fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType()));
            case META_DATA:
                return readPayloadMetaData(payload, fromMimeType(rsocketModuleState().currentRocketState().getMetadataMimeType()));
        }
        return null;
    }


    private boolean testServiceMethodCommand(Value data) {
        Entity serviceMethodCommandEntity = Value.asEntity(data).getEntity(SERVICE_METHOD_COMMAND);
        if (isNull(serviceMethodCommandEntity)) throw new ServiceMappingException(SERVICE_COMMAND_IS_NULL);
        String serviceId = serviceMethodCommandEntity.getString(SERVICE_ID);
        if (isNull(serviceId)) throw new ServiceMappingException(SERVICE_ID_IS_NULL);
        String methodId = serviceMethodCommandEntity.getString(METHOD_ID);
        if (isNull(methodId)) throw new ServiceMappingException(METHOD_ID_IS_NULL);
        return predicate.test(new ServiceMethodCommand(serviceId, methodId));
    }

}
