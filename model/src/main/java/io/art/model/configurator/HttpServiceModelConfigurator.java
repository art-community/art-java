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

package io.art.model.configurator;

import io.art.core.collection.*;
import io.art.model.implementation.server.*;
import lombok.*;

import java.util.*;
import java.util.function.*;

import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static lombok.AccessLevel.*;

@Getter(value = PACKAGE)
@NoArgsConstructor
public class HttpServiceModelConfigurator {
    private final Map<String, HttpServiceRouteModelConfigurator> routes = map();
    private final Set<String> existentIDs = set();
    private String host = BROADCAST_IP_ADDRESS;
    private Integer port = 80;
    private boolean logging = false;
    private boolean compression = false;

    public HttpServiceModelConfigurator host(String host) {
        this.host = host;
        return this;
    }

    public HttpServiceModelConfigurator port(Integer port){
        this.port = port;
        return this;
    }

    public HttpServiceModelConfigurator logging(boolean isLogging) {
        this.logging = isLogging;
        return this;
    }

    public HttpServiceModelConfigurator compress() {
        this.compression = true;
        return this;
    }

    public HttpServiceModelConfigurator route(String path, Class<?> serviceClass){
        addRouteIfAbsent(path, new HttpServiceRouteModelConfigurator(serviceClass));
        return this;
    }

    public HttpServiceModelConfigurator route(String path, Class<?> serviceClass,
                                              UnaryOperator<HttpServiceRouteModelConfigurator> configurator){
        addRouteIfAbsent(path, configurator.apply(new HttpServiceRouteModelConfigurator(serviceClass)));
        return this;
    }

    protected ImmutableSet<HttpServiceModel> configure() {
        return routes.entrySet().stream()
                .map(route -> HttpServiceModel.builder()
                        .id(route.getValue().getId())
                        .path(route.getKey())
                        .host(host)
                        .port(port)
                        .serviceClass(route.getValue().getServiceClass())
                        .methods(route.getValue().getMethods()
                                .entrySet()
                                .stream()
                                .collect(immutableMapCollector(entry -> entry.getValue().getId(), entry -> entry.getValue().configure())))
                        .decorator(route.getValue().getDecorator())
                        .logging(logging)
                        .compression(compression)
                        .build())
                .collect(immutableSetCollector());
    }

    private void addRouteIfAbsent(String route, HttpServiceRouteModelConfigurator configurator){
        if (!existentIDs.contains(configurator.getId()) && !routes.containsKey(route)){
            existentIDs.add(configurator.getId());
            configurator.logging(logging);
            routes.put(route, configurator);
        }
    }

}
