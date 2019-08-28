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

package ru.art.example.interceptor.http;

import ru.art.core.constants.*;
import ru.art.http.server.interceptor.*;
import static ru.art.core.constants.InterceptionStrategy.*;
import static ru.art.logging.LoggingModule.*;
import javax.servlet.http.*;

/**
 * Http interceptors can do some logic before executing http service method and after that
 */
public class ExampleServiceHttpInterception implements HttpServerInterception {

    /**
     * This method processes http requests and responses
     *
     * @param request  - http request with service incoming data and http request data
     * @param response - http response with service method response and http response data
     * @return InterceptionStrategy - as usual it's  NEXT_INTERCEPTOR
     */
    @Override
    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) {
        loggingModule().getLogger().info("Http service interceptor executed");
        return NEXT_INTERCEPTOR;
    }
}
