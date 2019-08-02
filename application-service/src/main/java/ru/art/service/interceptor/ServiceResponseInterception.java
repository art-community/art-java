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
import ru.art.service.model.ServiceResponse;
import static ru.art.service.model.ServiceInterceptionResult.*;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface ServiceResponseInterception {
    static ServiceResponseInterception interceptAndContinue(BiConsumer<ServiceRequest<?>, ServiceResponse<?>> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return nextInterceptor(request, response);
        };
    }

    static ServiceResponseInterception interceptAndCall(BiConsumer<ServiceRequest<?>, ServiceResponse<?>> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return processHandling(request, response);
        };
    }

    static ServiceResponseInterception interceptAndReturn(BiConsumer<ServiceRequest<?>, ServiceResponse<?>> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return stopHandling(request, response);
        };
    }

    ServiceInterceptionResult intercept(ServiceRequest<?> request, ServiceResponse<?> response);

}