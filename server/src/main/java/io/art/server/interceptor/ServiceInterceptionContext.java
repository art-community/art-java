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

package io.art.server.interceptor;

import io.art.server.service.implementation.*;
import lombok.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class ServiceInterceptionContext<Request, Response> {
    private final Function<ServiceInterceptionContext<Request, Response>, Response> delegate;
    @Getter
    private final ServiceMethodImplementation implementation;
    private final AtomicReference<Response> response = new AtomicReference<>();
    private final AtomicReference<Request> request = new AtomicReference<>();

    public ServiceInterceptionContext(Function<ServiceInterceptionContext<Request, Response>, Response> delegate, ServiceMethodImplementation implementation) {
        this.delegate = delegate;
        this.implementation = implementation;
    }

    public ServiceInterceptionContext(Function<ServiceInterceptionContext<Request, Response>, Response> delegate, ServiceMethodImplementation implementation, Request request) {
        this.delegate = delegate;
        this.implementation = implementation;
        this.request.set(request);
    }

    public Response getResponse() {
        return response.get();
    }

    public Request getRequest() {
        return request.get();
    }

    public void process() {
        response.set(delegate.apply(this));
    }

    public void processRequest(Request request) {
        this.request.set(request);
        response.set(delegate.apply(this));
    }

    public void processResponse(Request request, Response response) {
        this.request.set(request);
        this.response.set(response);
        this.response.set(delegate.apply(this));
    }
}
