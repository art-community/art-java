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

import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import lombok.*;
import reactor.core.scheduler.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.ConfigurationKeys.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.Defaults.*;
import static io.art.core.collection.ImmutableMap.*;
import static java.util.Optional.*;
import java.util.*;

@Getter
public class CommunicatorModuleConfiguration implements ModuleConfiguration {
    private ImmutableMap<String, CommunicatorConfiguration> communicators = emptyImmutableMap();
    private Scheduler scheduler;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<CommunicatorModuleConfiguration, Configurator> {
        private final CommunicatorModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            configuration.scheduler = DEFAULT_COMMUNICATOR_SCHEDULER;
            configuration.communicators = ofNullable(source.getNested(COMMUNICATOR_SECTION))
                    .map(server -> server.getNestedMap(TARGETS_KEY))
                    .map(services -> services
                            .entrySet()
                            .stream()
                            .collect(immutableMapCollector(Map.Entry::getKey, entry -> CommunicatorConfiguration.from(entry.getValue()))))
                    .orElse(emptyImmutableMap());
            return this;
        }
    }
}
