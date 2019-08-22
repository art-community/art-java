/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.service.model;

import lombok.*;
import ru.art.core.constants.*;

import static ru.art.core.constants.InterceptionStrategy.*;

@Getter
@Builder(builderMethodName = "interceptionResult")
public class ServiceInterceptionResult {
    @Builder.Default
    private final InterceptionStrategy nextInterceptionStrategy = NEXT_INTERCEPTOR;
    private final ServiceRequest<?> request;
    private ServiceResponse<?> response;

    public static ServiceInterceptionResult nextInterceptor(ServiceRequest<?> request) {
        return interceptionResult().request(request).build();
    }

    public static ServiceInterceptionResult nextInterceptor(ServiceRequest<?> request, ServiceResponse<?> response) {
        return interceptionResult().request(request).response(response).build();
    }

    public static ServiceInterceptionResult processHandling(ServiceRequest<?> request) {
        return interceptionResult().request(request).nextInterceptionStrategy(PROCESS_HANDLING).build();
    }

    public static ServiceInterceptionResult processHandling(ServiceRequest<?> request, ServiceResponse<?> response) {
        return interceptionResult().request(request).nextInterceptionStrategy(PROCESS_HANDLING).response(response).build();
    }

    public static ServiceInterceptionResult stopHandling(ServiceRequest<?> request) {
        return interceptionResult().request(request).nextInterceptionStrategy(STOP_HANDLING).build();
    }

    public static ServiceInterceptionResult stopHandling(ServiceRequest<?> request, ServiceResponse<?> response) {
        return interceptionResult().request(request).nextInterceptionStrategy(STOP_HANDLING).response(response).build();
    }
}
