/*
 * ART
 *
 * Copyright 2019-2021 ART
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

import io.art.core.collection.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.server.constants.ServerModuleConstants.ConfigurationKeys.*;

@Getter
@RequiredArgsConstructor
public class RsocketServiceConfiguration {
    private ImmutableMap<String, RsocketMethodConfiguration> methods;
    private final RsocketServerConfiguration serverConfiguration;

    public static RsocketServiceConfiguration from(RsocketServerConfiguration serverConfiguration, ConfigurationSource source) {
        RsocketServiceConfiguration configuration = new RsocketServiceConfiguration(serverConfiguration);
        configuration.methods = source.getNestedMap(METHODS_KEY, method -> RsocketMethodConfiguration.from(configuration, method));
        return configuration;
    }
}
