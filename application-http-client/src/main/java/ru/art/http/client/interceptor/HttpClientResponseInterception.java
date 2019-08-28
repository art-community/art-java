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

package ru.art.http.client.interceptor;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import ru.art.core.constants.*;
import static ru.art.core.constants.InterceptionStrategy.*;
import java.util.function.*;

@FunctionalInterface
public interface HttpClientResponseInterception {
    static HttpClientResponseInterception interceptAndContinue(BiConsumer<HttpUriRequest, HttpResponse> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return NEXT_INTERCEPTOR;
        };
    }

    static HttpClientResponseInterception interceptAndCall(BiConsumer<HttpUriRequest, HttpResponse> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return PROCESS_HANDLING;
        };
    }

    static HttpClientResponseInterception interceptAndReturn(BiConsumer<HttpUriRequest, HttpResponse> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return STOP_HANDLING;
        };
    }

    InterceptionStrategy intercept(HttpUriRequest request, HttpResponse response);
}
