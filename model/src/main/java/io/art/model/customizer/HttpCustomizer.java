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

package io.art.model.customizer;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.http.configuration.*;
import io.art.http.module.*;
import io.art.http.refresher.*;
import io.art.model.implementation.server.*;
import io.art.server.module.*;
import lombok.*;
import reactor.netty.http.server.*;

import java.util.function.*;

import static io.art.core.caster.Caster.cast;
import static io.art.core.checker.NullityChecker.let;
import static io.art.core.collection.ImmutableMap.immutableMapBuilder;
import static io.art.core.collection.ImmutableMap.immutableMapCollector;
import static io.art.core.constants.StringConstants.SLASH;
import static io.netty.handler.codec.http.HttpMethod.GET;

@Getter
@UsedByGenerator
public class HttpCustomizer {
    private final Custom configuration;

    public HttpCustomizer(HttpModule module) {
        this.configuration = new Custom(module.getRefresher());
    }

    public HttpCustomizer server(HttpServerConfiguration serverConfiguration) {
        configuration.serverConfiguration = serverConfiguration;
        return this;
    }

    public HttpCustomizer server(HttpServerModel httpServerModel) {
        HttpServerConfiguration.HttpServerConfigurationBuilder serverConfigurationBuilder = HttpServerConfiguration.builder()
                .httpServer(HttpServer.create()
                        .host(httpServerModel.getHost())
                        .port(httpServerModel.getPort())
                        .compress(httpServerModel.isCompression()))
                .defaultDataFormat(httpServerModel.getDefaultDataFormat())
                .defaultMetaDataFormat(httpServerModel.getDefaultMetaDataFormat())
                .fragmentationMtu(httpServerModel.getFragmentationMtu())
                .logging(httpServerModel.isLogging())
                .services(httpServerModel.getServices().values().stream()
                .collect(cast(immutableMapCollector(HttpServiceModel::getId, this::buildServiceConfig))));

        let(httpServerModel.getDefaultServiceMethod(), serverConfigurationBuilder::defaultServiceMethod);

        server(serverConfigurationBuilder.build());
        return this;
    }

    public HttpCustomizer services(ImmutableMap<String, HttpServiceConfiguration> services) {
        configuration.serverConfiguration = HttpServerConfiguration.defaults().toBuilder().services(services).build();
        return this;
    }

    public HttpCustomizer activateServer() {
        configuration.activateServer = true;
        return this;
    }

    public HttpCustomizer activateCommunicator() {
        configuration.activateCommunicator = true;
        return this;
    }

    private HttpServiceConfiguration buildServiceConfig(HttpServiceModel serviceModel) {
        ImmutableMap.Builder<String, HttpMethodConfiguration> configs = immutableMapBuilder();
        ServerModule.serverModule()
                .configuration()
                .getRegistry()
                .getServices()
                .get(serviceModel.getId())
                .getMethods().keySet()
                .forEach(id -> configs.put(id, serviceModel.getHttpMethods().containsKey(id)
                        ? HttpMethodConfiguration.builder()
                                .path(serviceModel.getPath().endsWith(SLASH) ?
                                        serviceModel.getPath() + serviceModel.getHttpMethods().get(id).getName() :
                                        serviceModel.getPath() + SLASH + serviceModel.getHttpMethods().get(id).getName()
                                )
                                .deactivated(serviceModel.getHttpMethods().get(id).isDeactivated())
                                .logging(serviceModel.getHttpMethods().get(id).isLogging())
                                .method(serviceModel.getHttpMethods().get(id).getHttpMethod())
                                .build()
                        : HttpMethodConfiguration.builder()
                                .path(serviceModel.getPath().endsWith(SLASH) ?
                                        serviceModel.getPath() + id :
                                        serviceModel.getPath() + SLASH + id
                                )
                                .method(GET)
                                .build())

                );
        return HttpServiceConfiguration.builder()
                .path(serviceModel.getPath())
                .methods(configs.build())
                .build();
    }

    @Getter
    public static class Custom extends HttpModuleConfiguration {
        private HttpServerConfiguration serverConfiguration;
        private boolean activateServer;
        private boolean activateCommunicator;

        public Custom(HttpModuleRefresher refresher) {
            super(refresher);
        }
    }
}
