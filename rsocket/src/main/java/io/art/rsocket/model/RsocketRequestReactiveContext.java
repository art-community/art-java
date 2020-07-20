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

import io.art.entity.immutable.*;
import io.art.entity.immutable.Value;
import io.rsocket.*;
import lombok.*;
import io.art.entity.interceptor.*;
import io.art.entity.mapper.*;
import static java.text.MessageFormat.format;
import static java.util.Objects.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.constants.InterceptionStrategy.*;
import static io.art.entity.immutable.Value.*;
import static io.art.logging.LoggingModule.loggingModule;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.FAILED_TO_READ_PAYLOAD;
import static io.art.rsocket.constants.RsocketModuleConstants.REQUEST_DATA;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.model.RsocketReactiveMethods.*;
import static io.art.rsocket.module.RsocketModule.rsocketModule;
import static io.art.rsocket.reader.RsocketPayloadReader.*;
import static io.art.service.mapping.ServiceRequestMapping.*;
import java.util.*;

@Getter
@Builder
public class RsocketRequestReactiveContext {
    private final Object requestData;
    private final RsocketReactiveGroupKey rsocketReactiveGroupKey;
    private final RsocketReactiveMethods rsocketReactiveMethods;
    @Builder.Default
    private final boolean stopHandling = false;
    private Entity alternativeResponse;

    @SuppressWarnings("Duplicates")
    public static RsocketRequestReactiveContext fromPayload(Payload payload, RsocketDataFormat dataFormat) {
        Value payloadValue;
        try {
            payloadValue = readPayloadData(payload, dataFormat);
            payload.release(payload.refCnt());
        } catch (Throwable throwable) {
            if (rsocketModule().isEnableRawDataTracing()) {
                loggingModule().getLogger(RsocketRequestContext.class).error(format(FAILED_TO_READ_PAYLOAD, throwable.getMessage(), throwable));
            }
            return RsocketRequestReactiveContext.builder().stopHandling(true).build();
        }
        Entity requestValue = asEntity(payloadValue);
        ServiceMethodCommand command = toServiceRequest().map(requestValue).getServiceMethodCommand();
        RsocketReactiveMethods rsocketServiceMethods = fromCommand(command);
        List<ValueInterceptor<Entity, Entity>> requestValueInterceptors = rsocketServiceMethods.getRsocketMethod().requestValueInterceptors();
        for (ValueInterceptor<Entity, Entity> requestValueInterceptor : requestValueInterceptors) {
            ValueInterceptionResult<Entity, Entity> result = requestValueInterceptor.intercept(requestValue);
            if (isNull(result)) {
                break;
            }
            requestValue = result.getOutValue();
            if (result.getNextInterceptionStrategy() == PROCESS) {
                break;
            }
            if (result.getNextInterceptionStrategy() == TERMINATE) {
                if (valueIsNull(result.getOutValue())) {
                    return RsocketRequestReactiveContext.builder().stopHandling(true).build();
                }
                return RsocketRequestReactiveContext.builder()
                        .alternativeResponse(result.getOutValue())
                        .stopHandling(true)
                        .build();
            }
        }
        ValueToModelMapper<?, ?> requestMapper;
        Value requestDataValue;
        if (valueIsNull(requestValue) ||
                isNull(requestMapper = rsocketServiceMethods.getRsocketMethod().requestMapper()) ||
                isEmpty(requestDataValue = requestValue.get(REQUEST_DATA))) {
            return RsocketRequestReactiveContext.builder()
                    .rsocketReactiveGroupKey(RsocketReactiveGroupKey.builder()
                            .serviceMethodCommand(command)
                            .rsocketReactiveMethods(rsocketServiceMethods)
                            .build())
                    .rsocketReactiveMethods(rsocketServiceMethods)
                    .build();
        }
        Object requestData = requestMapper.map(cast(requestDataValue));
        return RsocketRequestReactiveContext.builder()
                .requestData(requestData)
                .rsocketReactiveGroupKey(RsocketReactiveGroupKey.builder()
                        .serviceMethodCommand(command)
                        .rsocketReactiveMethods(rsocketServiceMethods)
                        .build())
                .rsocketReactiveMethods(rsocketServiceMethods)
                .build();
    }
}
