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

package io.art.server.configuration;

import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.server.model.*;
import io.art.server.registry.*;
import lombok.*;
import reactor.core.scheduler.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.server.constants.ServerModuleConstants.ConfigurationKeys.*;
import static io.art.server.constants.ServerModuleConstants.Defaults.*;
import static java.util.Optional.*;


@Getter
public class ServerModuleConfiguration implements ModuleConfiguration {
    private ImmutableMap<String, ServiceConfiguration> configurations = emptyImmutableMap();
    private Scheduler scheduler;
    private ServiceSpecificationRegistry registry = new ServiceSpecificationRegistry();

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<ServerModuleConfiguration, Configurator> {
        private final ServerModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            configuration.scheduler = DEFAULT_SERVICE_METHOD_SCHEDULER;
            configuration.configurations = ofNullable(source.getNested(SERVER_SECTION))
                    .map(server -> server.getNestedMap(SERVER_SERVICES_KEY, ServiceConfiguration::from))
                    .orElse(emptyImmutableMap());
            return this;
        }

        @Override
        public Configurator override(ServerModuleConfiguration configuration) {
            apply(configuration.getRegistry(), registry -> this.configuration.registry = registry);
            return this;
        }
    }
}
