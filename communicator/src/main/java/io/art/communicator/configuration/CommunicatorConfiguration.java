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

package io.art.communicator.configuration;

import io.art.communicator.refresher.*;
import io.art.communicator.registry.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.source.*;
import io.art.resilience.configuration.*;
import lombok.Builder;
import lombok.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.property.LazyProperty.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.*;
import java.util.function.*;

@Builder(toBuilder = true)
public class CommunicatorConfiguration {
    private final CommunicatorRefresher refresher;

    @Getter
    private final CommunicatorRefresher.Consumer consumer;

    @Getter
    private ImmutableMap<String, CommunicatorProxyConfiguration> proxies;

    @Getter
    private final ConnectorRegistry connectors;


    public Optional<CommunicatorActionConfiguration> getActionConfiguration(CommunicatorActionIdentifier id) {
        CommunicatorProxyConfiguration proxyConfiguration = proxies.get(id.getCommunicatorId());
        return ofNullable(proxyConfiguration).map(configuration -> configuration.getActions().get(id.getActionId()));
    }

    public ResilienceConfiguration getResilienceConfiguration(CommunicatorActionIdentifier id) {
        return getActionConfiguration(id)
                .map(CommunicatorActionConfiguration::getResilienceConfiguration)
                .orElse(ResilienceConfiguration.builder().build());
    }

    public boolean isLogging(CommunicatorActionIdentifier identifier) {
        boolean communicator = checkCommunicator(identifier, CommunicatorProxyConfiguration::isLogging, true);
        boolean action = checkAction(identifier, CommunicatorActionConfiguration::isLogging, true);
        return communicator && action;
    }

    public boolean isDeactivated(CommunicatorActionIdentifier identifier) {
        boolean communicator = checkCommunicator(identifier, CommunicatorProxyConfiguration::isDeactivated, false);
        boolean action = checkAction(identifier, CommunicatorActionConfiguration::isDeactivated, false);
        return communicator && action;
    }


    private <T> T checkCommunicator(CommunicatorActionIdentifier identifier, Function<CommunicatorProxyConfiguration, T> mapper, T defaultValue) {
        CommunicatorProxyConfiguration proxyConfiguration = proxies.get(identifier.getCommunicatorId());
        if (isNull(proxyConfiguration)) {
            return defaultValue;
        }
        return mapper.apply(proxyConfiguration);
    }

    private <T> T checkAction(CommunicatorActionIdentifier identifier, Function<CommunicatorActionConfiguration, T> mapper, T defaultValue) {
        CommunicatorProxyConfiguration proxyConfiguration = proxies.get(identifier.getCommunicatorId());
        if (isNull(proxyConfiguration)) {
            return defaultValue;
        }
        CommunicatorActionConfiguration actionConfiguration = proxyConfiguration.getActions().get(identifier.getActionId());
        if (isNull(actionConfiguration)) {
            return defaultValue;
        }
        return mapper.apply(actionConfiguration);
    }

    public static CommunicatorConfiguration defaults(CommunicatorRefresher refresher) {
        return CommunicatorConfiguration.builder()
                .refresher(refresher)
                .consumer(refresher.consumer())
                .connectors(new ConnectorRegistry(lazy(ImmutableMap::emptyImmutableMap)))
                .proxies(emptyImmutableMap())
                .build();
    }

    public static CommunicatorConfiguration from(CommunicatorRefresher refresher, ConfigurationSource source) {
        CommunicatorConfiguration configuration = defaults(refresher);
        configuration.proxies = ofNullable(source.getNested(COMMUNICATOR_SECTION))
                .map(server -> server.getNestedMap(TARGETS_SECTION, service -> CommunicatorProxyConfiguration.from(configuration.refresher, service)))
                .orElse(emptyImmutableMap());
        refresher.produce();
        return configuration;
    }

    public static CommunicatorConfiguration from(CommunicatorRefresher refresher, CommunicatorConfiguration current, ConfigurationSource source) {
        CommunicatorConfigurationBuilder builder = current.toBuilder();
        builder.proxies(ofNullable(source.getNested(COMMUNICATOR_SECTION))
                .map(server -> server.getNestedMap(TARGETS_SECTION, service -> CommunicatorProxyConfiguration.from(builder.refresher, service)))
                .orElse(emptyImmutableMap()));
        refresher.produce();
        return builder.build();
    }
}
