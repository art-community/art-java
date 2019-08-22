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

package ru.art.http.server.interceptor;

import ru.art.core.constants.*;
import javax.servlet.http.*;

import static ru.art.core.constants.InterceptionStrategy.*;
import static ru.art.http.constants.HttpHeaders.*;
import static ru.art.http.server.HttpServerModuleConfiguration.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.*;
import static ru.art.http.server.module.HttpServerModule.*;

public class HttpWebInterception implements HttpServerInterception {
    @Override
    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) {
        HttpWebConfiguration webConfiguration = httpServerModule().getWebConfiguration();
        if (webConfiguration.isAllowOriginParameterFromRequest()) {
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_KEY, request.getHeader(ORIGIN));
        }
        webConfiguration
                .getAccessControlParameters()
                .forEach(response::setHeader);
        return NEXT_INTERCEPTOR;
    }
}
