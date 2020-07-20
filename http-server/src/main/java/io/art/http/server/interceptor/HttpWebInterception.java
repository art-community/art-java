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

package io.art.http.server.interceptor;

import io.art.core.constants.*;
import static io.art.core.constants.InterceptionStrategy.*;
import static io.art.http.constants.HttpHeaders.*;
import static io.art.http.server.HttpServerModuleConfiguration.*;
import static io.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.*;
import static io.art.http.server.module.HttpServerModule.*;
import javax.servlet.http.*;

public class HttpWebInterception implements HttpServerInterception {
    @Override
    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) {
        HttpResourceConfiguration resourceConfiguration = httpServerModule().getResourceConfiguration();
        if (resourceConfiguration.isAllowOriginParameterFromRequest()) {
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_KEY, request.getHeader(ORIGIN));
        }
        resourceConfiguration
                .getAccessControlParameters()
                .forEach(response::setHeader);
        return NEXT;
    }
}
