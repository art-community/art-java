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

package ru.art.service.registry;

import lombok.*;
import ru.art.service.*;
import static java.text.MessageFormat.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.service.constants.ServiceLoggingMessages.*;
import java.util.*;

public class ServiceRegistry {
    @Getter
    private final Map<String, Specification> services = concurrentHashMap();

    public Specification getService(String serviceId) {
        return services.get(serviceId);
    }

    public ServiceRegistry registerService(Specification specification) {
        loggingModule()
                .getLogger(ServiceRegistry.class)
                .info(format(SERVICE_REGISTRATION_MESSAGE, specification.getServiceId(), specification.getClass().getName()));
        services.put(specification.getServiceId(), specification);
        return this;
    }
}