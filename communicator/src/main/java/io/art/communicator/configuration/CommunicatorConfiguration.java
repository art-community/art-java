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
import io.art.core.model.*;
import io.art.resilience.configuration.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.transport.payload.*;
import lombok.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.*;
import java.util.function.*;

@Builder
public class CommunicatorConfiguration {
    private final CommunicatorRefresher refresher;

    @Getter(lazy = true)
    private final CommunicatorRefresher.Consumer consumer = refresher.consumer();

    @Getter
    @Singular("communicator")
    private final Map<String, CommunicatorProxyConfiguration> communicators;

    @Getter
    @Builder.Default
    private final Function<DataFormat, TransportPayloadReader> reader = TransportPayloadReader::new;

    @Getter
    @Builder.Default
    private final Function<DataFormat, TransportPayloadWriter> writer = TransportPayloadWriter::new;

    public Optional<String> findConnectorId(String protocol, CommunicatorActionIdentifier id) {
        CommunicatorProxyConfiguration proxyConfiguration = communicators.get(id.getCommunicatorId());
        Optional<String> connectorId = ofNullable(proxyConfiguration).map(configuration -> configuration.getConnectors().get(protocol));
        if (connectorId.isPresent()) return connectorId;
        return getActionConfiguration(id).map(configuration -> configuration.getConnectors().get(protocol));
    }

    public TransportPayloadReader getReader(CommunicatorActionIdentifier id, DataFormat dataFormat) {
        return getActionConfiguration(id)
                .map(CommunicatorActionConfiguration::getReader)
                .orElseGet(() -> ofNullable(getCommunicators().get(id.getCommunicatorId()))
                        .map(CommunicatorProxyConfiguration::getReader)
                        .orElse(reader)).apply(dataFormat);
    }

    public TransportPayloadWriter getWriter(CommunicatorActionIdentifier id, DataFormat dataFormat) {
        return getActionConfiguration(id)
                .map(CommunicatorActionConfiguration::getWriter)
                .orElseGet(() -> ofNullable(getCommunicators().get(id.getCommunicatorId()))
                        .map(CommunicatorProxyConfiguration::getWriter)
                        .orElse(writer)).apply(dataFormat);
    }

    public Optional<CommunicatorActionConfiguration> getActionConfiguration(CommunicatorActionIdentifier id) {
        CommunicatorProxyConfiguration proxyConfiguration = communicators.get(id.getCommunicatorId());
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
        CommunicatorProxyConfiguration proxyConfiguration = communicators.get(identifier.getCommunicatorId());
        if (isNull(proxyConfiguration)) {
            return defaultValue;
        }
        return mapper.apply(proxyConfiguration);
    }

    private <T> T checkAction(CommunicatorActionIdentifier identifier, Function<CommunicatorActionConfiguration, T> mapper, T defaultValue) {
        CommunicatorProxyConfiguration proxyConfiguration = communicators.get(identifier.getCommunicatorId());
        if (isNull(proxyConfiguration)) {
            return defaultValue;
        }
        CommunicatorActionConfiguration actionConfiguration = proxyConfiguration.getActions().get(identifier.getActionId());
        if (isNull(actionConfiguration)) {
            return defaultValue;
        }
        return mapper.apply(actionConfiguration);
    }
}
