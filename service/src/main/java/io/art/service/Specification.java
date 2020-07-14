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

package io.art.service;

import io.art.service.interceptor.ServiceExecutionInterceptor.*;
import io.art.service.model.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.service.ServiceModule.*;
import static io.art.service.constants.ServiceModuleConstants.*;
import java.util.*;

public interface Specification {
    String getServiceId();

    <P, R> R executeMethod(String methodId, P request);

    default DeactivationConfig getDeactivationConfig() {
        return serviceModule().getDeactivationConfigurations().getOrDefault(getServiceId(), DeactivationConfig.builder().build());
    }

    default List<RequestInterceptor> getRequestInterceptors() {
        return ServiceModule.serviceModule().getRequestInterceptors();
    }

    default List<ResponseInterceptor> getResponseInterceptors() {
        return serviceModule().getResponseInterceptors();
    }

    default ServiceExecutionExceptionWrapper getExceptionWrapper() {
        return serviceModule().getExceptionWrapper();
    }

    default ServiceExecutionConfiguration getExecutionConfiguration() {
        return serviceModule().getExecutionConfigurations().getOrDefault(getServiceId(), ServiceExecutionConfiguration.builder().build());
    }

    default Map<String, List<RequestInterceptor>> getMethodRequestInterceptors() {
        return mapOf();
    }

    default Map<String, List<ResponseInterceptor>> getMethodResponseInterceptors() {
        return mapOf();
    }

    default List<String> getServiceTypes() {
        return fixedArrayOf(DEFAULT_SERVICE_TYPE);
    }
}
