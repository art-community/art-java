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

import io.art.model.implementation.server.*;
import io.art.server.specification.ServiceMethodSpecification.*;
import io.netty.handler.codec.http.*;
import lombok.*;

import static java.util.function.Function.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Getter(value = PACKAGE)
public class HttpServiceMethodModelConfigurator {
    private final String id;
    private String name;
    private boolean deactivated = false;
    private boolean logging = false;
    private HttpMethod httpMethod = HttpMethod.GET;
    private Function<ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator = identity();

    public HttpServiceMethodModelConfigurator(String methodName) {
        this.name = methodName;
        this.id = methodName;
    }

    public HttpServiceMethodModelConfigurator pathName(String path) {
        this.name = path;
        return this;
    }

    public HttpServiceMethodModelConfigurator deactivate() {
        deactivated = false;
        return this;
    }

    public HttpServiceMethodModelConfigurator logging(boolean isLogging) {
        logging = isLogging;
        return this;
    }

    public HttpServiceMethodModelConfigurator httpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public HttpServiceMethodModelConfigurator decorate(Function<ServiceMethodSpecificationBuilder, ServiceMethodSpecificationBuilder> decorator) {
        this.decorator = decorator.andThen(decorator);
        return this;
    }

    HttpServiceMethodModel configure() {
        return HttpServiceMethodModel.builder()
                .id(id)
                .name(name)
                .deactivated(deactivated)
                .logging(logging)
                .httpMethod(httpMethod)
                .decorator(decorator)
                .build();
    }
}
