package ru.art.http.server;

import ru.art.http.server.context.HttpRequestContext;
import ru.art.http.server.handler.HttpExceptionHandler;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.http.constants.HttpHeaders.CONTENT_TYPE;
import static ru.art.http.constants.HttpMimeTypes.APPLICATION_JSON_UTF8;
import static ru.art.http.server.body.descriptor.HttpBodyDescriptor.writeResponseBody;
import static ru.art.http.server.constants.HttpExceptionResponses.EXCEPTION_HANDLING_ERROR_RESPONSE;
import static ru.art.http.server.module.HttpServerModule.httpServerModuleState;
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
