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

package io.art.model.configurator;

import com.google.common.collect.*;
import io.art.server.configuration.*;
import io.art.server.registry.*;
import io.art.server.specification.*;
import lombok.*;

public class ServerConfiguratorModel {
    private final ImmutableMap.Builder<String, ServiceSpecification> services = ImmutableMap.builder();

    public CustomServerModuleConfiguration getConfiguration() {
        return new CustomServerModuleConfiguration(new ServiceSpecificationRegistry(services.build()));
    }

    public ServerConfiguratorModel register(ServiceSpecification specification) {
        services.put(specification.getServiceId(), specification);
        return this;
    }

    @Getter
    @RequiredArgsConstructor
    public static class CustomServerModuleConfiguration extends ServerModuleConfiguration {
        private final ServiceSpecificationRegistry registry;
    }
}