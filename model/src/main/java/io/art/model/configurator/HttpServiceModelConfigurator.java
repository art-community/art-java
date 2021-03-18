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
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.set;
import static lombok.AccessLevel.*;

@Getter(value = PACKAGE)
@NoArgsConstructor
public class HttpServiceModelConfigurator {
    private final Map<String, HttpServiceRouteModelConfigurator> routes = map();
    private final Set<String> existentIDs = set();
    private Integer port = 80;

    public HttpServiceModelConfigurator(Class<?> baseServiceClass) {
        putRouteIfAbsent(SLASH, new HttpServiceRouteModelConfigurator(baseServiceClass));
    }

    public HttpServiceModelConfigurator port(Integer port){
        this.port = port;
        return this;
    }

    public HttpServiceModelConfigurator route(String path, Class<?> serviceClass){
        putRouteIfAbsent(path, new HttpServiceRouteModelConfigurator(serviceClass));
        return this;
    }

    public HttpServiceModelConfigurator route(String path, Class<?> serviceClass,
                                              UnaryOperator<HttpServiceRouteModelConfigurator> configurator){
        putRouteIfAbsent(path, configurator.apply(new HttpServiceRouteModelConfigurator(serviceClass)));
        return this;
    }

    protected ImmutableSet<HttpServiceModel> configure() {
        return routes.entrySet().stream()
                .map(route -> HttpServiceModel.builder()
                        .id(route.getValue().getId())
                        .path(route.getKey())
                        .port(port)
                        .serviceClass(route.getValue().getServiceClass())
                        .methods(route.getValue().getMethods()
                                .entrySet()
                                .stream()
                                .collect(immutableMapCollector(entry -> entry.getValue().getId(), entry -> entry.getValue().configure())))
                        .decorator(route.getValue().getDecorator())
                        .build())
                .collect(immutableSetCollector());
    }

    private void putRouteIfAbsent(String route, HttpServiceRouteModelConfigurator configurator){
        if (!existentIDs.contains(configurator.getId()) && !routes.containsKey(route)){
            existentIDs.add(configurator.getId());
            routes.put(route, configurator);
        }
    }

}
