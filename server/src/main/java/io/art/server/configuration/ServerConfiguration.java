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

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.core.source.*;
import io.art.server.method.*;
import io.art.server.refresher.*;
import lombok.Builder;
import lombok.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.server.constants.ServerConstants.ConfigurationKeys.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.*;
import java.util.function.*;


@Builder(toBuilder = true)
public class ServerConfiguration {
    private final ServerRefresher refresher;

    @Getter
    private final ServerRefresher.Consumer consumer;

    @Getter
    private final LazyProperty<ImmutableMap<ServiceMethodIdentifier, ServiceMethod>> methods;

    @Getter
    private LazyProperty<ImmutableMap<String, ServiceMethodsConfiguration>> configurations;

    public Optional<ServiceMethodConfiguration> getMethodConfiguration(ServiceMethodIdentifier id) {
        return ofNullable(configurations.get().get(id.getServiceId())).map(configuration -> configuration.getMethods().get(id.getMethodId()));
    }

    public boolean isLogging(ServiceMethodIdentifier identifier) {
        boolean service = checkService(identifier, ServiceMethodsConfiguration::isLogging, false);
        boolean method = checkMethod(identifier, ServiceMethodConfiguration::isLogging, false);
        return service && method;
    }

    public boolean isValidating(ServiceMethodIdentifier identifier) {
        boolean service = checkService(identifier, ServiceMethodsConfiguration::isValidating, true);
        boolean method = checkMethod(identifier, ServiceMethodConfiguration::isValidating, true);
        return service && method;
    }

    public boolean isDeactivated(ServiceMethodIdentifier identifier) {
        boolean service = checkService(identifier, ServiceMethodsConfiguration::isDeactivated, false);
        boolean method = checkMethod(identifier, ServiceMethodConfiguration::isDeactivated, false);
        return service || method;
    }

    private <T> T checkService(ServiceMethodIdentifier identifier, Function<ServiceMethodsConfiguration, T> mapper, T defaultValue) {
        ServiceMethodsConfiguration serviceConfiguration = configurations.get().get(identifier.getServiceId());
        if (isNull(serviceConfiguration)) {
            return defaultValue;
        }
        return mapper.apply(serviceConfiguration);
    }

    private <T> T checkMethod(ServiceMethodIdentifier identifier, Function<ServiceMethodConfiguration, T> mapper, T defaultValue) {
        ServiceMethodsConfiguration serviceConfiguration = configurations.get().get(identifier.getServiceId());
        if (isNull(serviceConfiguration)) {
            return defaultValue;
        }
        ServiceMethodConfiguration methodConfiguration = serviceConfiguration.getMethods().get(identifier.getMethodId());
        if (isNull(methodConfiguration)) {
            return defaultValue;
        }
        return mapper.apply(methodConfiguration);
    }

    public static ServerConfiguration defaults(ServerRefresher refresher) {
        return ServerConfiguration.builder().refresher(refresher)
                .consumer(refresher.consumer())
                .methods(lazy(ImmutableMap::emptyImmutableMap))
                .configurations(lazy(ImmutableMap::emptyImmutableMap))
                .build();
    }

    public static ServerConfiguration from(ServerRefresher refresher, ConfigurationSource source) {
        ServerConfiguration configuration = defaults(refresher);
        configuration.configurations = lazy(() -> ofNullable(source.getNested(SERVER_SECTION))
                .map(server -> server.getNestedMap(SERVER_SERVICES_KEY, service -> ServiceMethodsConfiguration.from(configuration.refresher, service)))
                .orElse(emptyImmutableMap()));
        configuration.refresher.produce();
        return configuration;
    }

    public static ServerConfiguration from(ServerRefresher refresher, ServerConfiguration current, ConfigurationSource source) {
        ServerConfigurationBuilder builder = current.toBuilder();
        builder.configurations = lazy(() -> ofNullable(source.getNested(SERVER_SECTION))
                .map(server -> server.getNestedMap(SERVER_SERVICES_KEY, service -> ServiceMethodsConfiguration.from(builder.refresher, service)))
                .orElse(emptyImmutableMap()));
        refresher.produce();
        return builder.build();
    }
}
