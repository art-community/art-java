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

package io.art.server.registry;

import io.art.server.model.*;
import io.art.server.specification.*;
import io.art.value.immutable.*;
import io.art.value.mapping.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.server.constants.ServerModuleConstants.*;
import static java.util.Optional.*;
import java.util.*;

public class ServiceSpecificationRegistry {
    private final Map<String, ServiceSpecification> services = map();

    public Optional<ServiceSpecification> get(String serviceId) {
        return ofNullable(services.get(serviceId));
    }

    public Optional<ServiceSpecification> findServiceByValue(Value value) {
        Entity setupEntity = Value.asEntity(value);
        String serviceId = setupEntity.map(SERVICE_ID, PrimitiveMapping.toString);
        return get(serviceId);
    }

    public Optional<ServiceMethodSpecification> findMethodByValue(Value value) {
        String methodId = Value.asEntity(value).map(METHOD_ID, PrimitiveMapping.toString);
        return findServiceByValue(value).flatMap(service -> ofNullable(service.getMethods().get(methodId)));
    }

    public Optional<ServiceMethodSpecification> findMethodById(ServiceMethodIdentifier id) {
        return get(id.getServiceId()).map(service -> service.getMethods().get(id.getMethodId()));
    }

    public Set<String> identifiers() {
        return services.keySet();
    }

    public ServiceSpecificationRegistry register(String id, ServiceSpecification specification) {
        services.put(id, specification);
        return this;
    }

    public Map<String, ServiceSpecification> getServices() {
        return immutableMapOf(services);
    }
}
