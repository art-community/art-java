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

package io.art.rsocket.configuration;

import com.google.common.collect.*;
import io.art.core.source.*;
import io.art.entity.constants.EntityConstants.*;
import lombok.*;
import static com.google.common.collect.ImmutableMap.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.entity.constants.EntityConstants.DataFormat.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.server.constants.ServerModuleConstants.ConfigurationKeys.*;
import static java.util.Optional.*;

@Getter
@RequiredArgsConstructor
public class RsocketServiceConfiguration {
    private DataFormat defaultDataFormat;
    private boolean tracing;
    private ImmutableMap<String, RsocketMethodConfiguration> methods;
    private final RsocketModuleConfiguration moduleConfiguration;

    public static RsocketServiceConfiguration from(RsocketModuleConfiguration moduleConfiguration, ConfigurationSource source) {
        RsocketServiceConfiguration configuration = new RsocketServiceConfiguration(moduleConfiguration);
        configuration.tracing = orElse(source.getBool(TRACING_KEY), false);
        configuration.defaultDataFormat = dataFormat(source.getString(DATA_FORMAT_KEY), moduleConfiguration.getDefaultDataFormat());
        configuration.methods = ofNullable(source.getNestedMap(METHODS_KEY))
                .map(configurations -> configurations.entrySet()
                        .stream()
                        .collect(toImmutableMap(Entry::getKey, entry -> RsocketMethodConfiguration.from(configuration, entry.getValue()))))
                .orElse(ImmutableMap.of());
        return configuration;
    }
}
