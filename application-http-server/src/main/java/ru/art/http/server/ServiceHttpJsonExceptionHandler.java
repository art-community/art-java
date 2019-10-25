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

package ru.art.http.server;

import ru.art.http.constants.*;
import ru.art.http.mapper.*;
import ru.art.http.server.context.*;
import ru.art.http.server.handler.*;
import ru.art.service.exception.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.http.constants.HttpMimeTypes.*;
import static ru.art.http.constants.HttpStatus.*;
import static ru.art.http.server.body.descriptor.HttpBodyDescriptor.*;
import static ru.art.http.server.constants.HttpExceptionResponses.*;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.service.mapping.ServiceEntitiesMapping.ServiceExecutionExceptionMapping.*;
import javax.servlet.http.*;
import java.nio.charset.*;

class ServiceHttpJsonExceptionHandler implements HttpExceptionHandler<ServiceExecutionException> {
    @Override
    public void handle(ServiceExecutionException exception, HttpServletRequest request, HttpServletResponse response) {
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
            String error = format(SERVICE_EXCEPTION_HANDLING_ERROR_RESPONSE, exception.getErrorCode(), exception.getErrorMessage());
            writeResponseBody(response, error.getBytes(charset));
        }
    }
}
