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
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.server.configuration.ServiceMethodsConfiguration.*;
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
    private final LazyProperty<ImmutableMap<String, ServiceMethodsConfiguration>> configurations;

    public Optional<ServiceMethodConfiguration> getMethodConfiguration(ServiceMethodIdentifier id) {
        return ofNullable(configurations.get().get(id.getServiceId())).map(configuration -> configuration.getMethods().get(id.getMethodId()));
    }

    public boolean isLogging(ServiceMethodIdentifier identifier) {
        boolean hasMethod = getMethodConfiguration(identifier).isPresent();
        boolean service = checkService(identifier, ServiceMethodsConfiguration::getLogging, false);
        if (!hasMethod) return service;
        return checkMethod(identifier, ServiceMethodConfiguration::getLogging, service);
    }

    public boolean isValidating(ServiceMethodIdentifier identifier) {
        boolean hasMethod = getMethodConfiguration(identifier).isPresent();
        boolean service = checkService(identifier, ServiceMethodsConfiguration::getValidating, true);
        if (!hasMethod) return service;
        return checkMethod(identifier, ServiceMethodConfiguration::getValidating, service);
    }

    public boolean isDeactivated(ServiceMethodIdentifier identifier) {
        boolean hasMethod = getMethodConfiguration(identifier).isPresent();
        boolean service = checkService(identifier, ServiceMethodsConfiguration::getDeactivated, false);
        if (!hasMethod) return service;
        return checkMethod(identifier, ServiceMethodConfiguration::getDeactivated, service);
    }

    private <T> T checkService(ServiceMethodIdentifier identifier, Function<ServiceMethodsConfiguration, T> mapper, T defaultValue) {
        ServiceMethodsConfiguration serviceConfiguration = configurations.get().get(identifier.getServiceId());
        if (isNull(serviceConfiguration)) {
            return defaultValue;
        }
        return orElse(mapper.apply(serviceConfiguration), defaultValue);
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
        return orElse(mapper.apply(methodConfiguration), defaultValue);
    }

    private static ServiceMethodsConfiguration getService(ServerConfiguration currentConfiguration, ServerConfigurationBuilder builder, NestedConfiguration service) {
        ServiceMethodsConfiguration current = currentConfiguration.configurations.get().get(service.getParent());
        return serviceMethodsConfiguration(builder.refresher, current, service);
    }

    public static ServerConfiguration serverConfiguration(ServerRefresher refresher, ServerConfiguration current, ConfigurationSource source) {
        ServerConfigurationBuilder builder = current.toBuilder();
        builder.configurations = lazy(() -> merge(current.configurations.get(), ofNullable(source)
                .map(server -> server.getNestedMap(SERVICES_SECTION, service -> getService(current, builder, service)))
                .orElse(emptyImmutableMap())));
        refresher.produce();
        return builder.build();
    }

    public static ServerConfiguration serverConfiguration(ServerRefresher refresher) {
        return ServerConfiguration.builder().refresher(refresher)
                .consumer(refresher.consumer())
                .methods(lazy(ImmutableMap::emptyImmutableMap))
                .configurations(lazy(ImmutableMap::emptyImmutableMap))
                .build();
    }
}
