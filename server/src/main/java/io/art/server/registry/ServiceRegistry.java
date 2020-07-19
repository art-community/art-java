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

import io.art.server.service.specification.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.logging.LoggingModule.*;
import static io.art.server.constants.ServerModuleConstants.LoggingMessages.*;
import static java.text.MessageFormat.*;
import java.util.*;

public class ServiceRegistry {
    private final Map<String, ServiceSpecification<?>> services = concurrentHashMap();

    public ServiceSpecification<?> get(String serviceId) {
        return services.get(serviceId);
    }

    public Set<String> services() {
        return services.keySet();
    }

    public ServiceRegistry register(ServiceSpecification<?> specification) {
        logger(ServiceRegistry.class)
                .info(format(SERVICE_REGISTRATION_MESSAGE, specification.getServiceId(), specification.getClass().getName()));
        services.put(specification.getServiceId(), specification);
        return this;
    }
}
