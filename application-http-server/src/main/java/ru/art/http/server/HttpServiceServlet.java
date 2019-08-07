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

import lombok.AllArgsConstructor;
import ru.art.http.constants.HttpMethodType;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.http.mime.MimeType;
import ru.art.http.server.body.descriptor.HttpBodyDescriptor;
import ru.art.http.server.context.HttpRequestContext;
import ru.art.http.server.context.HttpRequestContext.HttpRequestContextBuilder;
import ru.art.http.server.context.MultiPartContext;
import ru.art.http.server.context.MultiPartContext.MultiPartContextBuilder;
import ru.art.http.server.exception.HttpServerException;
import ru.art.http.server.handler.HttpExceptionHandler;
import ru.art.http.server.model.HttpService;
import ru.art.logging.ServiceCallLoggingParameters;
import ru.art.service.model.ServiceMethodCommand;
import static java.nio.charset.Charset.forName;
import static java.text.MessageFormat.format;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.logging.log4j.ThreadContext.get;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.StringConstants.COMMA;
import static ru.art.core.constants.StringConstants.DOT;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.http.constants.HttpHeaders.*;
import static ru.art.http.constants.HttpMethodType.OPTIONS;
import static ru.art.http.constants.HttpMethodType.resolve;
import static ru.art.http.constants.HttpMimeTypes.ALL;
import static ru.art.http.mime.MimeType.valueOf;
import static ru.art.http.server.HttpServerRequestHandler.executeHttpService;
import static ru.art.http.server.constants.HttpServerExceptionMessages.*;
import static ru.art.http.server.constants.HttpServerLoggingMessages.HTTP_REQUEST_HANDLING_EXCEPTION_MESSAGE;
import static ru.art.http.server.constants.HttpServerLoggingMessages.HTTP_SERVLET_EVENT;
import static ru.art.http.server.constants.HttpServerModuleConstants.EMPTY_HTTP_CONTENT_LENGTH;
import static ru.art.http.server.constants.HttpServerModuleConstants.MULTIPART_PATTERN;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.http.server.module.HttpServerModule.httpServerModuleState;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.logging.LoggingModuleConstants.DEFAULT_REQUEST_ID;
import static ru.art.logging.LoggingModuleConstants.LoggingParameters.REQUEST_ID_KEY;
import static ru.art.logging.LoggingParametersManager.clearServiceCallLoggingParameters;
import static ru.art.logging.LoggingParametersManager.putServiceCallLoggingParameters;
import static ru.art.service.ServiceModule.serviceModule;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@AllArgsConstructor
@MultipartConfig
class HttpServiceServlet extends HttpServlet {
    private final Map<HttpMethodType, HttpServletCommand> commands;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        HttpMethodType httpMethodType = resolve(request.getMethod());
        HttpServletCommand command = commands.get(httpMethodType);
        if (OPTIONS == httpMethodType) {
            response.setHeader(ALLOW, commands.values().stream().map(method -> method.getHttpMethod().getMethodType().name()).collect(joining(COMMA)));
            response.setStatus(SC_OK);
            clearServiceCallLoggingParameters();
            return;
        }
        if (isNull(command)) {
            handleException(request, response, new HttpServerException(format(HTTP_METHOD_NOT_ALLOWED, request.getMethod())));
            clearServiceCallLoggingParameters();
            return;
        }
        ServiceMethodCommand serviceCommand = createServiceCommand(command.getServiceId(), command.getHttpMethod());
        clearServiceCallLoggingParameters();
        putServiceCallLoggingParameters(ServiceCallLoggingParameters.builder()
                .serviceId(command.getServiceId())
                .serviceMethodId(serviceCommand.toString())
                .serviceMethodCommand(serviceCommand.toString() + DOT + getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID))
                .loggingEventType(HTTP_SERVLET_EVENT)
                .loadedServices(serviceModule().getServiceRegistry().getServices().keySet())
                .build());
        try {
            updateRequestContext(command, request);
            byte[] responseBody = executeHttpService(serviceCommand, request, command.getHttpMethod());
            if (!command.getHttpMethod().isOverrideResponseContentType()) {
                response.addHeader(CONTENT_TYPE, httpServerModuleState().getRequestContext().getAcceptType().toString());
            }
            HttpBodyDescriptor.writeResponseBody(response, responseBody);
            clearServiceCallLoggingParameters();
        } catch (Exception e) {
            handleException(request, response, e);
            clearServiceCallLoggingParameters();
        }
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        loggingModule().getLogger(HttpServiceServlet.class).error(HTTP_REQUEST_HANDLING_EXCEPTION_MESSAGE, exception);
        Class exceptionClass = exception.getClass();
        HttpExceptionHandler<Exception> exceptionExceptionHandler = httpServerModule().getExceptionHandlers().get(exceptionClass);
        if (isNull(exceptionExceptionHandler)) {
            exceptionExceptionHandler = httpServerModule().getExceptionHandlers().get(Exception.class);
        }
        if (isNull(exceptionExceptionHandler)) throw new HttpServerException(exception);
        exceptionExceptionHandler.handle(exception, request, response);
    }

    private void updateRequestContext(HttpServletCommand command, HttpServletRequest request) {
        Map<String, String> headers = mapOf();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        List<Cookie> cookies = fixedArrayOf(request.getCookies());
        String charsetHeader = request.getHeader(ACCEPT_CHARSET);
        HttpRequestContextBuilder requestContextBuilder = HttpRequestContext.builder()
                .requestUrl(request.getRequestURL().toString())
                .headers(headers)
                .cookies(cookies)
                .acceptCharset(isEmpty(charsetHeader) ? contextConfiguration().getCharset() : forName(charsetHeader))
                .acceptEncodings(emptyIfNull(request.getHeader(ACCEPT_ENCODING)))
                .acceptLanguages(emptyIfNull(request.getHeader(ACCEPT_LANGUAGE)))
                .contentLength(request.getContentLength())
                .hasContent(request.getContentLength() == EMPTY_HTTP_CONTENT_LENGTH);
        calculateAcceptType(command, request, requestContextBuilder);
        calculateContentType(command, request, requestContextBuilder);
        String contentType;
        if (nonNull(contentType = request.getHeader(CONTENT_TYPE)) && contentType.contains(MULTIPART_PATTERN)) {
            calculateParts(request, requestContextBuilder);
        }
        httpServerModuleState().setRequestContext(requestContextBuilder.build());
    }

    private void calculateParts(HttpServletRequest request, HttpRequestContextBuilder requestContextBuilder) {
        Collection<Part> parts;
        try {
            parts = request.getParts();
        } catch (IOException ioException) {
            throw new HttpServerException(CANT_READ_MULTIPART_DATA_TERMINATE, ioException);
        } catch (ServletException servletException) {
            loggingModule()
                    .getLogger(HttpServiceServlet.class)
                    .warn(CANT_READ_MULTIPART_DATA_SKIP, servletException);
            return;
        }
        parts.forEach(part -> addPartToContext(requestContextBuilder, part));
    }

    private void calculateAcceptType(HttpServletCommand command, HttpServletRequest request, HttpRequestContextBuilder requestContextBuilder) {
        if (command.isIgnoreRequestAcceptType()) {
            requestContextBuilder.acceptType(getProducesContentTypeChecked(command));
            return;
        }
        String acceptTypeHeader = request.getHeader(ACCEPT);
        if (isEmpty(acceptTypeHeader)) {
            requestContextBuilder.acceptType(getProducesContentTypeChecked(command));
            return;
        }
        String[] acceptTypesStr = emptyIfNull(acceptTypeHeader).split(COMMA);
        if (isEmpty(acceptTypesStr)) {
            requestContextBuilder.acceptType(getProducesContentTypeChecked(command));
            return;
        }
        List<MimeType> acceptTypes = sortMimeTypes(acceptTypesStr);
        Iterator<MimeType> acceptTypeIt = acceptTypes.iterator();
        while (acceptTypeIt.hasNext()) {
            MimeType type = acceptTypeIt.next();
            if (httpServerModule().getContentMappers().containsKey(type)) {
                requestContextBuilder.acceptType(type);
                return;
            }
            if (!acceptTypeIt.hasNext()) {
                throw new HttpServerException(format(REQUEST_ACCEPT_TYPE_NOT_SUPPORTED, type.toString()));
            }
        }
    }

    private void calculateContentType(HttpServletCommand command, HttpServletRequest request, HttpRequestContextBuilder requestContextBuilder) {
        if (request.getContentLength() == EMPTY_HTTP_CONTENT_LENGTH) return;
        if (command.isIgnoreRequestContentType()) {
            requestContextBuilder.contentType(getConsumesContentTypeChecked(command));
            return;
        }
        String contentTypeHeader = request.getContentType();
        if (isEmpty(contentTypeHeader)) {
            requestContextBuilder.contentType(getConsumesContentTypeChecked(command));
            return;
        }
        String[] contentTypesStr = emptyIfNull(contentTypeHeader).split(COMMA);
        if (isEmpty(contentTypesStr)) {
            requestContextBuilder.contentType(getConsumesContentTypeChecked(command));
            return;
        }
        List<MimeType> contentTypes = sortMimeTypes(contentTypesStr);
        Iterator<MimeType> contentTypeIt = contentTypes.iterator();
        while (contentTypeIt.hasNext()) {
            MimeType type = contentTypeIt.next();
            if (httpServerModule().getContentMappers().containsKey(type)) {
                requestContextBuilder.contentType(type);
                return;
            }
            if (!contentTypeIt.hasNext()) {
                throw new HttpServerException(format(REQUEST_CONTENT_TYPE_NOT_SUPPORTED, contentTypeHeader));
            }
        }
    }

    private MimeType getConsumesContentTypeChecked(HttpServletCommand command) {
        MimeToContentTypeMapper type;
        if (isNull(type = command.getConsumesContentType())) {
            return ALL;
        }
        return type.getMimeType();
    }

    private MimeType getProducesContentTypeChecked(HttpServletCommand command) {
        MimeToContentTypeMapper type;
        if (isNull(type = command.getProducesContentType())) {
            return ALL;
        }
        return type.getMimeType();
    }

    private List<MimeType> sortMimeTypes(String[] mimeTypes) {
        return stream(mimeTypes).map(MimeType::valueOf).sorted().collect(toList());
    }

    private ServiceMethodCommand createServiceCommand(String serviceId, HttpService.HttpMethod methodConfig) {
        return new ServiceMethodCommand(serviceId, methodConfig.getMethodId());
    }

    private void addPartToContext(HttpRequestContextBuilder requestContextBuilder, Part part) {
        MultiPartContextBuilder partBuilder = MultiPartContext.builder()
                .contentLength(part.getSize())
                .contentType(valueOf(part.getContentType()))
                .fileName(part.getSubmittedFileName());
        part.getHeaderNames().forEach(headerName -> partBuilder.header(headerName, part.getHeader(headerName)));
        requestContextBuilder.part(part.getName(), partBuilder.build());
    }
}