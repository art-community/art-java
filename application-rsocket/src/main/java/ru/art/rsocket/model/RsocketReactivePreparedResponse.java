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
import reactor.core.publisher.GroupedFlux;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.service.model.ServiceRequest;
import static ru.art.service.factory.ServiceRequestFactory.newServiceRequest;

@Getter
@Builder
public class RsocketReactivePreparedResponse {
    private final ServiceRequest<?> serviceRequest;
    private ValueFromModelMapper<?, ?> responseMapper;

    @SuppressWarnings("ConstantConditions")
    public static RsocketReactivePreparedResponse fromGroupedFlux(GroupedFlux<RsocketReactiveGroupKey, RsocketRequestReactiveContext> group) {
        return RsocketReactivePreparedResponse.builder()
                .responseMapper(group.key().getResponseMapper())
                .serviceRequest(newServiceRequest(group.key().getServiceMethodCommand(), group.map(RsocketRequestReactiveContext::getRequestData), group.key().getValidationPolicy()))
                .build();
    }
}
