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

package io.art.http.server;

import io.art.core.constants.*;
import io.art.http.server.interceptor.*;
import io.art.logging.*;
import static java.lang.System.*;
import static java.util.UUID.*;
import static io.art.core.checker.CheckerForEmptiness.*;
import static io.art.core.constants.InterceptionStrategy.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.http.server.constants.HttpServerModuleConstants.*;
import static io.art.logging.LoggingParametersManager.*;
import javax.servlet.http.*;
import java.util.*;

public class HttpServerTracingIdentifierInterception implements HttpServerInterception {
    @Override
    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> traceIdHeader = request.getHeaders(TRACE_ID_HEADER);
        Enumeration<String> profileHeader = request.getHeaders(PROFILE_HEADER);
        clearProtocolLoggingParameters();
        putProtocolCallLoggingParameters(ProtocolCallLoggingParameters.builder()
                .protocol(request.getScheme())
                .requestId(randomUUID().toString())
                .traceId(isEmpty(traceIdHeader) || !traceIdHeader.hasMoreElements() ? randomUUID().toString() : traceIdHeader.nextElement())
                .profile(extractProfile(profileHeader))
                .environment(getProperty(ENVIRONMENT_PROPERTY))
                .build());
        return NEXT_INTERCEPTOR;
    }

    private String extractProfile(Enumeration<String> profileHeaders) {
        if (isNotEmpty(profileHeaders) && profileHeaders.hasMoreElements()) {
            return profileHeaders.nextElement();
        }
        return EMPTY_STRING;
    }
}
