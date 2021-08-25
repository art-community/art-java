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

package io.art.http.configuration;

import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.http.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.http.configuration.HttpConnectorConfiguration.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static java.util.Optional.*;

@Getter
public class HttpCommunicatorConfiguration {
    private HttpConnectorConfiguration defaultConnectorConfiguration;
    private ImmutableMap<String, HttpConnectorConfiguration> connectorConfigurations;

    public boolean isLogging(String connectorId) {
        return ofNullable(connectorConfigurations)
                .map(configurations -> configurations.get(connectorId))
                .map(HttpConnectorConfiguration::isLogging)
                .orElseGet(() -> let(defaultConnectorConfiguration, HttpConnectorConfiguration::isLogging, false));
    }

    public static HttpCommunicatorConfiguration communicatorConfiguration(HttpModuleRefresher refresher, ConfigurationSource source) {
        HttpCommunicatorConfiguration configuration = new HttpCommunicatorConfiguration();
        configuration.defaultConnectorConfiguration = let(source.getNested(DEFAULT_SECTION), HttpConnectorConfiguration::connectorConfiguration, HttpConnectorConfiguration.connectorConfiguration());
        configuration.connectorConfigurations = source.getNestedMap(CONNECTORS_KEY, connector -> connectorConfiguration(refresher, configuration.defaultConnectorConfiguration, connector));
        return configuration;
    }
}
