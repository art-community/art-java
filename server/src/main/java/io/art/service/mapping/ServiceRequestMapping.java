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

package io.art.service.mapping;

import io.art.entity.immutable.*;
import io.art.entity.mapper.*;
import io.art.service.constants.*;
import io.art.service.exception.*;
import io.art.service.model.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static java.util.Objects.*;
import static org.apache.logging.log4j.core.util.Assert.isEmpty;
import static io.art.entity.immutable.Entity.*;
import static io.art.entity.mapper.ValueMapper.*;
import static io.art.service.constants.RequestValidationPolicy.*;
import static io.art.service.constants.ServiceExceptionsMessages.*;

public interface ServiceRequestMapping {
    String SERVICE_METHOD_COMMAND = "serviceMethodCommand";
    String SERVICE_ID = "serviceId";
    String METHOD_ID = "methodId";
    String VALIDATION_POLICY = "validationPolicy";
    String REQUEST_DATA = "requestData";

    @SuppressWarnings("Duplicates")
    static <D> ValueToModelMapper.EntityToModelMapper<ServiceRequest<D>> toServiceRequest(final ValueToModelMapper<D, Value> requestDataMapper) {
        return value -> {
            Entity serviceMethodCommandEntity = asEntity(value.get(SERVICE_METHOD_COMMAND));
            if (valueIsNull(serviceMethodCommandEntity)) throw new ServiceMappingException(SERVICE_COMMAND_IS_NULL);
            String serviceId = asPrimitive(serviceMethodCommandEntity.get(SERVICE_ID)).getString();
            if (isNull(serviceId)) throw new ServiceMappingException(SERVICE_ID_IS_NULL);
            String methodId = asPrimitive(serviceMethodCommandEntity.get(METHOD_ID)).getString();
            if (isNull(methodId)) throw new ServiceMappingException(METHOD_ID_IS_NULL);
            String validationPolicyAsString = asPrimitive(value.get(VALIDATION_POLICY)).getString();
            RequestValidationPolicy requestValidationPolicy = isEmpty(validationPolicyAsString)
                    ? NON_VALIDATABLE :
                    RequestValidationPolicy.valueOf(validationPolicyAsString);
            D requestData = isNull(requestDataMapper) ? null : value.map(REQUEST_DATA, requestDataMapper);
            return new ServiceRequest<>(new ServiceMethodCommand(serviceId, methodId), requestValidationPolicy, requestData);
        };
    }

    static <D> ValueFromModelMapper.EntityFromModelMapper<ServiceRequest<D>> fromServiceRequest(final ValueFromModelMapper<D, Value> requestDataMapper) {
        return model -> {
            ServiceMethodCommand command = model.getServiceMethodCommand();
            if (isNull(command)) throw new ServiceMappingException(SERVICE_COMMAND_IS_NULL);
            String serviceId = command.getServiceId();
            if (isNull(serviceId)) throw new ServiceMappingException(SERVICE_ID_IS_NULL);
            String methodId = command.getMethodId();
            if (isNull(methodId)) throw new ServiceMappingException(METHOD_ID_IS_NULL);
            RequestValidationPolicy validationPolicy = isNull(validationPolicy = model.getValidationPolicy()) ? NON_VALIDATABLE : validationPolicy;
            return entityBuilder()
                    .put(SERVICE_METHOD_COMMAND, entityBuilder()
                            .put(SERVICE_ID, stringPrimitive(serviceId))
                            .put(METHOD_ID, stringPrimitive(methodId))
                            .build())
                    .put(VALIDATION_POLICY, stringPrimitive(validationPolicy.toString()))
                    .lazyPut(REQUEST_DATA, () -> isNull(requestDataMapper) ? null : requestDataMapper.map(model.getRequestData()))
                    .build();
        };
    }

    static <D> ValueToModelMapper.EntityToModelMapper<ServiceRequest<D>> toServiceRequest() {
        return toServiceRequest(null);
    }

    static <D> ValueFromModelMapper.EntityFromModelMapper<ServiceRequest<D>> fromServiceRequest() {
        return fromServiceRequest(null);
    }

    static <V> ValueMapper<ServiceRequest<V>, Entity> serviceRequestMapper(ValueToModelMapper<V, Value> requestToModelMapper, ValueFromModelMapper<V, Value> requestFromModelMapper) {
        return mapper(fromServiceRequest(requestFromModelMapper), toServiceRequest(requestToModelMapper));
    }
}
