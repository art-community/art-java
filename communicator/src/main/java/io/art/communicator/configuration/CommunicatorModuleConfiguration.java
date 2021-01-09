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

package io.art.communicator.configuration;

import io.art.communicator.registry.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.module.*;
import io.art.core.source.*;
import lombok.*;
import reactor.core.scheduler.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.ConfigurationKeys.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.Defaults.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static java.util.Optional.*;
import java.util.*;

@Getter
public class CommunicatorModuleConfiguration implements ModuleConfiguration {
    private ImmutableMap<String, CommunicatorProxyConfiguration> configurations = emptyImmutableMap();
    private CommunicatorProxyRegistry registry = new CommunicatorProxyRegistry();
    private Scheduler scheduler;

    public Optional<String> findConnectorId(String protocol, CommunicatorActionIdentifier id) {
        Optional<String> connectorId = ofNullable(configurations.get(id.getCommunicatorId())).map(configuration -> configuration.getConnectors().get(protocol));
        if (connectorId.isPresent()) return connectorId;
        return getActionConfiguration(id).map(configuration -> configuration.getConnectors().get(protocol));
    }

    public Optional<CommunicatorActionConfiguration> getActionConfiguration(CommunicatorActionIdentifier id) {
        return ofNullable(configurations.get(id.getCommunicatorId())).map(configuration -> configuration.getActions().get(id.getActionId()));
    }

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<CommunicatorModuleConfiguration, Configurator> {
        private final CommunicatorModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            configuration.scheduler = DEFAULT_COMMUNICATOR_SCHEDULER;
            configuration.configurations = ofNullable(source.getNested(COMMUNICATOR_SECTION))
                    .map(server -> server.getNestedMap(PROXIES_SECTION, CommunicatorProxyConfiguration::from))
                    .orElse(emptyImmutableMap());
            return this;
        }

        @Override
        public Configurator override(CommunicatorModuleConfiguration configuration) {
            ifNotEmpty(configuration.getConfigurations(), configurations -> this.configuration.configurations = configurations);
            apply(configuration.getRegistry(), registry -> this.configuration.registry = registry);
            return this;
        }
    }
}
