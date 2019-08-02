/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.service.interceptor;

import ru.art.service.model.ServiceInterceptionResult;
import ru.art.service.model.ServiceRequest;
import static ru.art.service.model.ServiceInterceptionResult.*;
import java.util.function.Consumer;

@FunctionalInterface
public interface ServiceRequestInterception {
    static ServiceRequestInterception interceptAndContinue(Consumer<ServiceRequest<?>> runnable) {
        return request -> {
            runnable.accept(request);
            return nextInterceptor(request);
        };
    }

    static ServiceRequestInterception interceptAndCall(Consumer<ServiceRequest<?>> runnable) {
        return request -> {
            runnable.accept(request);
            return processHandling(request);
        };
    }

    static ServiceRequestInterception interceptAndReturn(Consumer<ServiceRequest<?>> runnable) {
        return request -> {
            runnable.accept(request);
            return stopHandling(request);
        };
    }

    ServiceInterceptionResult intercept(ServiceRequest<?> request);
}