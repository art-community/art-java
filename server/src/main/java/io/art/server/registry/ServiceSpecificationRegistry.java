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

import io.art.server.specification.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.logging.LoggingModule.*;
import static io.art.server.constants.ServerModuleConstants.LoggingMessages.*;
import static java.text.MessageFormat.*;
import java.util.*;

public class ServiceSpecificationRegistry {
    private final Map<String, ServiceSpecification> services = mapOf();

    public ServiceSpecification get(String serviceId) {
        return services.get(serviceId);
    }

    public Set<String> identifiers() {
        return services.keySet();
    }

    public ServiceSpecificationRegistry register(ServiceSpecification specification) {
        services.put(specification.getServiceId(), specification);
        logger(ServiceSpecificationRegistry.class).info(format(SERVICE_REGISTRATION_MESSAGE, specification.getServiceId()));
        return this;
    }
}
