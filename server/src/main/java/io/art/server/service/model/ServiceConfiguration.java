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

package io.art.server.service.model;

import com.google.common.collect.*;
import io.art.core.module.*;
import io.art.resilience.model.*;
import lombok.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.*;
import java.util.*;

@Getter
@AllArgsConstructor
public class ServiceConfiguration {
    private final boolean deactivated;
    private final ResilienceConfiguration resilienceConfiguration;
    private final Map<String, ServiceMethodConfiguration> methods;

    public static ServiceConfiguration from(ModuleConfigurationSource source) {
        boolean deactivated = getOrElse(source.getBool("deactivated"), false);
        ResilienceConfiguration resilience = let(source.getInner("resilience"), ResilienceConfiguration::from);
        Map<String, ServiceMethodConfiguration> methods = ofNullable(source.getInnerMap("methods"))
                .map(configurations -> configurations.entrySet()
                        .stream()
                        .collect(toMap(Map.Entry::getKey, entry -> ServiceMethodConfiguration.from(entry.getValue()))))
                .orElse(ImmutableMap.of());
        return new ServiceConfiguration(deactivated, resilience, methods);
    }
}
