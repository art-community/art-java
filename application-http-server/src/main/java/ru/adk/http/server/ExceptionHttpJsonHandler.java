package ru.adk.http.server;

import ru.adk.http.server.context.HttpRequestContext;
import ru.adk.http.server.handler.HttpExceptionHandler;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static ru.adk.core.context.Context.contextConfiguration;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.http.constants.HttpHeaders.CONTENT_TYPE;
import static ru.adk.http.constants.HttpMimeTypes.APPLICATION_JSON_UTF8;
import static ru.adk.http.server.body.descriptor.HttpBodyDescriptor.writeResponseBody;
import static ru.adk.http.server.constants.HttpExceptionResponses.EXCEPTION_HANDLING_ERROR_RESPONSE;
import static ru.adk.http.server.module.HttpServerModule.httpServerModuleState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ExceptionHttpJsonHandler implements HttpExceptionHandler<Exception> {
    @Override
    public void handle(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        String error = format(EXCEPTION_HANDLING_ERROR_RESPONSE, exception.getMessage());
        response.setHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8.toString());
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
        HttpRequestContext requestContext = httpServerModuleState().getRequestContext();
        writeResponseBody(response, error.getBytes(isNull(requestContext) ? contextConfiguration().getCharset() : getOrElse(requestContext.getAcceptCharset(), contextConfiguration().getCharset())));
    }
}
