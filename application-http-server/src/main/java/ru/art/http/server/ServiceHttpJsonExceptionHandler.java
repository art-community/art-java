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

package ru.art.http.server;

import ru.art.http.constants.HttpHeaders;
import ru.art.http.mapper.HttpContentMapper;
import ru.art.http.server.context.HttpRequestContext;
import ru.art.http.server.handler.HttpExceptionHandler;
import ru.art.service.exception.ServiceExecutionException;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.http.constants.HttpMimeTypes.APPLICATION_JSON_UTF8;
import static ru.art.http.constants.HttpStatus.INTERNAL_SERVER_ERROR;
import static ru.art.http.server.body.descriptor.HttpBodyDescriptor.writeResponseBody;
import static ru.art.http.server.constants.HttpExceptionResponses.SERVICE_EXCEPTION_HANDLING_ERROR_RESPONSE;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.http.server.module.HttpServerModule.httpServerModuleState;
import static ru.art.service.mapping.ServiceEntitiesMapping.ServiceExecutionExceptionMapping.serviceExecutionExceptionMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

class ServiceHttpJsonExceptionHandler implements HttpExceptionHandler<ServiceExecutionException> {
    @Override
    public void handle(ServiceExecutionException exception, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_UTF8.toString());
        HttpRequestContext requestContext = httpServerModuleState().getRequestContext();
        Charset charset = isNull(requestContext) ? contextConfiguration().getCharset() : getOrElse(requestContext.getAcceptCharset(), contextConfiguration().getCharset());
        try {
            response.setStatus(INTERNAL_SERVER_ERROR.getCode());
            HttpContentMapper contentMapper = httpServerModule().getContentMappers().get(APPLICATION_JSON_UTF8);
            byte[] bodyBytes = contentMapper.getToContent().mapToBytes(serviceExecutionExceptionMapper.map(exception), APPLICATION_JSON_UTF8, charset);
            writeResponseBody(response, bodyBytes);
        } catch (Throwable e) {
            String error = format(SERVICE_EXCEPTION_HANDLING_ERROR_RESPONSE, exception.getErrorCode(), exception.getErrorMessage());
            writeResponseBody(response, error.getBytes(charset));
        }
    }
}
