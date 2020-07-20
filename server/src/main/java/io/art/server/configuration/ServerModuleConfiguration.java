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

import com.google.common.collect.*;
import io.art.core.module.*;
import io.art.server.service.model.*;
import lombok.*;
import static com.google.common.collect.ImmutableMap.*;
import static java.util.Optional.*;
import java.util.*;


@Getter
public class ServerModuleConfiguration implements ModuleConfiguration {
    private ImmutableMap<String, ServiceConfiguration> services = ImmutableMap.of();

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<ServerModuleConfiguration, Configurator> {
        private final ServerModuleConfiguration configuration;

        @Override
        public Configurator from(ModuleConfigurationSource source) {
            configuration.services = ofNullable(source.getInnerMap("server.services"))
                    .map(services -> services
                            .entrySet()
                            .stream()
                            .collect(toImmutableMap(Map.Entry::getKey, entry -> ServiceConfiguration.from(entry.getValue()))))
                    .orElse(ImmutableMap.of());
            return this;
        }
    }
}
