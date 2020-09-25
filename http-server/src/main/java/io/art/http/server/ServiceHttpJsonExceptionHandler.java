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

package io.art.http.server;

import io.art.http.constants.*;
import io.art.http.mapper.*;
import io.art.http.server.context.*;
import io.art.http.server.handler.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static io.art.core.context.Context.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.http.constants.HttpMimeTypes.*;
import static io.art.http.constants.HttpStatus.*;
import static io.art.http.server.body.descriptor.HttpBodyDescriptor.*;
import static io.art.http.server.constants.HttpExceptionResponses.*;
import static io.art.http.server.module.HttpServerModule.*;
import static io.art.service.mapping.ServiceEntitiesMapping.ServiceExecutionExceptionMapping.*;
import javax.servlet.http.*;
import java.nio.charset.*;

class ServiceHttpJsonExceptionHandler implements HttpExceptionHandler<DefaultServiceExecutionException> {
    @Override
    public void handle(DefaultServiceExecutionException exception, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_UTF8.toString());
        HttpRequestContext requestContext = httpServerModuleState().getRequestContext();
        Charset charset = isNull(requestContext)
                ? contextConfiguration().getCharset()
                : getOrElse(requestContext.getAcceptCharset(), contextConfiguration().getCharset());
        try {
            response.setStatus(INTERNAL_SERVER_ERROR.getCode());
            HttpContentMapper contentMapper = httpServerModule().getContentMappers().get(APPLICATION_JSON_UTF8);
            byte[] bodyBytes = contentMapper
                    .getToContent()
                    .mapToBytes(serviceExecutionExceptionMapper.map(exception), APPLICATION_JSON_UTF8, charset);
            writeResponseBody(response, bodyBytes);
        } catch (Throwable throwable) {
            String error = format(SERVICE_EXCEPTION_HANDLING_ERROR_RESPONSE, exception.getErrorCode(), exception.getMessage(), exception.getStackTraceText());
            writeResponseBody(response, error.getBytes(charset));
        }
    }
}
