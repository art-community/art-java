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

package io.art.server.configuration;

import io.art.core.model.*;
import io.art.server.refresher.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.transport.payload.*;
import lombok.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.*;
import java.util.function.*;


@Builder
public class ServerConfiguration {
    private final ServerRefresher refresher;

    @Getter(lazy = true)
    private final ServerRefresher.Consumer consumer = refresher.consumer();

    @Getter
    @Singular("service")
    private final Map<String, ServiceConfiguration> services;

    @Getter
    @Builder.Default
    private final Function<DataFormat, TransportPayloadReader> reader = TransportPayloadReader::new;

    @Getter
    @Builder.Default
    private final Function<DataFormat, TransportPayloadWriter> writer = TransportPayloadWriter::new;

    public TransportPayloadReader getReader(ServiceMethodIdentifier id, DataFormat dataFormat) {
        return getMethodConfiguration(id)
                .map(ServiceMethodConfiguration::getReader)
                .orElseGet(() -> ofNullable(services.get(id.getServiceId()))
                        .map(ServiceConfiguration::getReader)
                        .orElse(reader)).apply(dataFormat);
    }

    public TransportPayloadWriter getWriter(ServiceMethodIdentifier id, DataFormat dataFormat) {
        return getMethodConfiguration(id)
                .map(ServiceMethodConfiguration::getWriter)
                .orElseGet(() -> ofNullable(services.get(id.getServiceId()))
                        .map(ServiceConfiguration::getWriter)
                        .orElse(writer)).apply(dataFormat);
    }

    public Optional<ServiceMethodConfiguration> getMethodConfiguration(ServiceMethodIdentifier id) {
        return ofNullable(services.get(id.getServiceId())).map(configuration -> configuration.getMethods().get(id.getMethodId()));
    }

    public boolean isLogging(ServiceMethodIdentifier identifier) {
        boolean service = checkService(identifier, ServiceConfiguration::isLogging, true);
        boolean method = checkMethod(identifier, ServiceMethodConfiguration::isLogging, true);
        return service && method;
    }

    public boolean isValidating(ServiceMethodIdentifier identifier) {
        boolean service = checkService(identifier, ServiceConfiguration::isValidating, true);
        boolean method = checkMethod(identifier, ServiceMethodConfiguration::isValidating, true);
        return service && method;
    }

    public boolean isDeactivated(ServiceMethodIdentifier identifier) {
        boolean service = checkService(identifier, ServiceConfiguration::isDeactivated, false);
        boolean method = checkMethod(identifier, ServiceMethodConfiguration::isDeactivated, false);
        return service || method;
    }

    private <T> T checkService(ServiceMethodIdentifier identifier, Function<ServiceConfiguration, T> mapper, T defaultValue) {
        ServiceConfiguration serviceConfiguration = services.get(identifier.getServiceId());
        if (isNull(serviceConfiguration)) {
            return defaultValue;
        }
        return mapper.apply(serviceConfiguration);
    }

    private <T> T checkMethod(ServiceMethodIdentifier identifier, Function<ServiceMethodConfiguration, T> mapper, T defaultValue) {
        ServiceConfiguration serviceConfiguration = services.get(identifier.getServiceId());
        if (isNull(serviceConfiguration)) {
            return defaultValue;
        }
        ServiceMethodConfiguration methodConfiguration = serviceConfiguration.getMethods().get(identifier.getMethodId());
        if (isNull(methodConfiguration)) {
            return defaultValue;
        }
        return mapper.apply(methodConfiguration);
    }
}
