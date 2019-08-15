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

package ru.art.network.manager.interceptor;

import ru.art.http.server.interceptor.HttpServerInterceptor;
import static ru.art.http.server.interceptor.HttpServerInterception.interceptAndContinue;
import static ru.art.http.server.interceptor.HttpServerInterceptor.intercept;
import static ru.art.network.manager.client.ApplicationStateClient.decrementSession;
import static ru.art.network.manager.client.ApplicationStateClient.incrementSession;

public interface HttpServerSessionInterceptor {
    HttpServerInterceptor httpServerSessionRequestInterceptor = intercept(interceptAndContinue((request, response) -> incrementSession()));
    HttpServerInterceptor httpServerSessionResponseInterceptor = intercept(interceptAndContinue((request, response) -> decrementSession()));
}
