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

package ru.art.service.factory;

import ru.art.service.constants.*;
import ru.art.service.model.*;

import static ru.art.service.constants.RequestValidationPolicy.*;

public interface ServiceRequestFactory {
    static <T> ServiceRequest<T> newServiceRequest(ServiceMethodCommand command) {
        return new ServiceRequest<>(command, NON_VALIDATABLE);
    }

    static <T> ServiceRequest<T> newServiceRequest(ServiceMethodCommand command, T primitiveData) {
        return new ServiceRequest<>(command, NON_VALIDATABLE, primitiveData);
    }

    static <T> ServiceRequest<T> newServiceRequest(ServiceMethodCommand command, RequestValidationPolicy validationPolicy) {
        return new ServiceRequest<>(command, validationPolicy);
    }

    static <T> ServiceRequest<T> newPrimitiveRequest(ServiceMethodCommand command, T primitiveData) {
        return newServiceRequest(command, primitiveData, NOT_NULL);
    }

    static <T> ServiceRequest<T> newServiceRequest(ServiceMethodCommand command, T requestData, RequestValidationPolicy validationPolicy) {
        return new ServiceRequest<>(command, validationPolicy, requestData);
    }
}
